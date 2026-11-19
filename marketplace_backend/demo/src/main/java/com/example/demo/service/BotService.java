package com.example.demo.service;

import com.example.demo.dto.BotReplyDTO;
import com.example.demo.entity.ChatMessage;
import com.example.demo.entity.Conversation;
import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class BotService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private MessengerService messengerService;

    @Autowired
    private UsersRepository usersRepository;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern DIGITS_PATTERN = Pattern.compile("\\d+");

    private final Map<String, BotSession> sessions = new ConcurrentHashMap<>();

    private final Map<String, DialogNode> graph = new HashMap<>();

    private static class BotSession {
        String currentNodeId = "START";
        final Map<String, Object> slots = new HashMap<>();
        int failedAttempts = 0;
    }

    private static class TurnContext {
        final String userId;
        final String rawText;
        final String normalizedText;
        final BotSession session;
        Long escalatedChatId = null;

        TurnContext(String userId, String rawText, BotSession session) {
            this.userId = userId;
            this.rawText = rawText == null ? "" : rawText.trim();
            this.normalizedText = this.rawText.toLowerCase(Locale.ROOT);
            this.session = session;
        }

        boolean matchesAny(String... keywords) {
            for (String kw : keywords) {
                if (normalizedText.contains(kw.toLowerCase(Locale.ROOT))) {
                    return true;
                }
            }
            return false;
        }

        boolean isValidEmail() {
            return EMAIL_PATTERN.matcher(rawText).matches();
        }

        boolean hasDigits() {
            return DIGITS_PATTERN.matcher(rawText).find();
        }

        Long parseNumericUserId() {
            try {
                return Long.parseLong(userId);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    private record Transition(
            Predicate<TurnContext> condition,
            Consumer<TurnContext> action,
            String nextNodeId
    ) {}

    private record DialogNode(
            String id,
            String replyText,
            List<String> quickReplies,
            List<Transition> transitions,
            String fallbackText
    ) {}

    @PostConstruct
    public void initDialogGraph() {
        List<String> mainMenuButtons = List.of(
                "Я не помню свой пароль",
                "Почему мой заказ задерживается?",
                "Как стать продавцом?",
                "Как зарегистрироваться?",
                "Позвать оператора"
        );

        graph.put("START", new DialogNode(
                "START",
                "Чем я могу вам помочь? Выберите вопрос или опишите проблему:",
                mainMenuButtons,
                List.of(
                        new Transition(
                                ctx -> ctx.matchesAny("парол", "не помню", "сброс", "войти"),
                                ctx -> {},
                                "FORGOT_PASSWORD_ASK_EMAIL"
                        ),
                        new Transition(
                                ctx -> ctx.matchesAny("задержива", "где заказ", "доставк", "посылк"),
                                ctx -> {},
                                "ORDER_DELAY_ASK_ID"
                        ),
                        new Transition(
                                ctx -> ctx.matchesAny("зарегистрир", "регистрац"),
                                ctx -> {},
                                "INFO_REGISTER"
                        ),
                        new Transition(
                                ctx -> ctx.matchesAny("продавц", "магазин"),
                                ctx -> {},
                                "INFO_VENDOR"
                        ),
                        new Transition(
                                ctx -> ctx.matchesAny("оператор", "человек", "поддержк", "админ"),
                                this::escalateToHumanSupport,
                                "ESCALATED_TO_HUMAN"
                        )
                ),
                "Извините, я не совсем понял запрос. Выберите один из вариантов ниже или позовите оператора:"
        ));

        graph.put("FORGOT_PASSWORD_ASK_EMAIL", new DialogNode(
                "FORGOT_PASSWORD_ASK_EMAIL",
                "Введите почту, привязанную к вашему аккаунту:",
                List.of("Я не имею доступ к почте", "В главное меню"),
                List.of(
                        new Transition(
                                ctx -> ctx.matchesAny("нет доступа", "не имею доступ", "потерял", "забыл почту"),
                                ctx -> {
                                    ctx.session.slots.put("escalationReason", "Утерян доступ к почте при сбросе пароля");
                                    escalateToHumanSupport(ctx);
                                },
                                "ESCALATED_TO_HUMAN"
                        ),
                        new Transition(
                                TurnContext::isValidEmail,
                                ctx -> {
                                    ctx.session.slots.put("guestEmail", ctx.rawText);
                                    boolean exists = usersRepository.findByEmail(ctx.rawText).isPresent();
                                    ctx.session.slots.put("emailExists", exists);
                                },
                                "PASSWORD_RESET_SENT"
                        )
                ),
                "Это не похоже на корректный email. Введите почту (например, user@mail.ru) или нажмите «Я не имею доступ к почте»:"
        ));

        graph.put("PASSWORD_RESET_SENT", new DialogNode(
                "PASSWORD_RESET_SENT",
                "Почта введена, сообщение со сбросом пароля отправлено! Проверьте папку «Входящие» и «Спам».",
                List.of("В главное меню", "Письмо не пришло — позвать оператора"),
                List.of(
                        new Transition(
                                ctx -> ctx.matchesAny("не пришл", "оператор", "помощ"),
                                this::escalateToHumanSupport,
                                "ESCALATED_TO_HUMAN"
                        ),
                        new Transition(
                                ctx -> true,
                                ctx -> {},
                                "START"
                        )
                ),
                ""
        ));

        graph.put("ORDER_DELAY_ASK_ID", new DialogNode(
                "ORDER_DELAY_ASK_ID",
                "Обычно доставка занимает от 2 до 5 дней. Напишите номер вашего заказа (цифрами), чтобы я проверил статус:",
                List.of("Не помню номер заказа", "В главное меню"),
                List.of(
                        new Transition(
                                TurnContext::hasDigits,
                                ctx -> ctx.session.slots.put("orderNumber", ctx.rawText),
                                "ORDER_DELAY_CHECKED"
                        ),
                        new Transition(
                                ctx -> ctx.matchesAny("не помню", "оператор"),
                                ctx -> {
                                    ctx.session.slots.put("escalationReason", "Проблема с доставкой заказа");
                                    escalateToHumanSupport(ctx);
                                },
                                "ESCALATED_TO_HUMAN"
                        )
                ),
                "Пожалуйста, введите номер заказа цифрами или нажмите «Не помню номер заказа»:"
        ));

        graph.put("ORDER_DELAY_CHECKED", new DialogNode(
                "ORDER_DELAY_CHECKED",
                "Заказ принят в обработку службой доставки. Если срок в 5 дней уже истёк, я могу подключить оператора для уточнения у продавца.",
                List.of("Подключить оператора", "В главное меню"),
                List.of(
                        new Transition(
                                ctx -> ctx.matchesAny("оператор", "подключ"),
                                this::escalateToHumanSupport,
                                "ESCALATED_TO_HUMAN"
                        ),
                        new Transition(
                                ctx -> true,
                                ctx -> {},
                                "START"
                        )
                ),
                ""
        ));
        graph.put("INFO_REGISTER", new DialogNode(
                "INFO_REGISTER",
                "Для регистрации перейдите на страницу входа и нажмите вкладку «Регистрация». Вам понадобится email и надёжный пароль.",
                List.of("Я не помню свой пароль", "В главное меню"),
                List.of(
                        new Transition(
                                ctx -> ctx.matchesAny("парол"),
                                ctx -> {},
                                "FORGOT_PASSWORD_ASK_EMAIL"
                        ),
                        new Transition(
                                ctx -> true,
                                ctx -> {},
                                "START"
                        )
                ),
                ""
        ));

        graph.put("INFO_VENDOR", new DialogNode(
                "INFO_VENDOR",
                "В вашем профиле есть кнопка «Стать продавцом». Нажмите её, заполните реквизиты магазина, и мы проверим заявку за 1 день.",
                mainMenuButtons,
                graph.get("START").transitions(),
                ""
        ));

        graph.put("ESCALATED_TO_HUMAN", new DialogNode(
                "ESCALATED_TO_HUMAN",
                "Я создал обращение в службу поддержки и передал историю диалога оператору. Нажмите кнопку ниже, чтобы перейти в чат со специалистом.",
                List.of("В главное меню"),
                List.of(
                        new Transition(
                                ctx -> true,
                                ctx -> {},
                                "START"
                        )
                ),
                ""
        ));
    }

    public BotReplyDTO processMessage(String userId, String userMessage) {
        chatMessageRepository.save(new ChatMessage(userId, "user", userMessage));

        if (userMessage == null || userMessage.isBlank()) {
            String err = "Ошибка: пустой запрос. Выберите вариант из меню.";
            chatMessageRepository.save(new ChatMessage(userId, "bot", err));
            return new BotReplyDTO(err, graph.get("START").quickReplies(), null);
        }

        BotSession session = sessions.computeIfAbsent(userId, k -> new BotSession());
        TurnContext ctx = new TurnContext(userId, userMessage, session);

        if (ctx.matchesAny("в главное меню", "в меню", "отмена", "сначала", "назад")) {
            session.currentNodeId = "START";
            session.failedAttempts = 0;
            DialogNode startNode = graph.get("START");
            chatMessageRepository.save(new ChatMessage(userId, "bot", startNode.replyText()));
            return new BotReplyDTO(startNode.replyText(), startNode.quickReplies(), null);
        }

        DialogNode currentNode = graph.getOrDefault(session.currentNodeId, graph.get("START"));

        for (Transition transition : currentNode.transitions()) {
            if (transition.condition().test(ctx)) {
                transition.action().accept(ctx);
                session.currentNodeId = transition.nextNodeId();
                session.failedAttempts = 0;

                DialogNode nextNode = graph.get(session.currentNodeId);
                chatMessageRepository.save(new ChatMessage(userId, "bot", nextNode.replyText()));
                return new BotReplyDTO(
                        nextNode.replyText(),
                        nextNode.quickReplies(),
                        ctx.escalatedChatId
                );
            }
        }
        session.failedAttempts++;
        if (session.failedAttempts >= 3) {
            session.failedAttempts = 0;
            escalateToHumanSupport(ctx);
            session.currentNodeId = "ESCALATED_TO_HUMAN";
            DialogNode escNode = graph.get("ESCALATED_TO_HUMAN");
            chatMessageRepository.save(new ChatMessage(userId, "bot", escNode.replyText()));
            return new BotReplyDTO(escNode.replyText(), escNode.quickReplies(), ctx.escalatedChatId);
        }

        String fallback = currentNode.fallbackText().isBlank()
                ? graph.get("START").fallbackText()
                : currentNode.fallbackText();
        chatMessageRepository.save(new ChatMessage(userId, "bot", fallback));
        return new BotReplyDTO(fallback, currentNode.quickReplies(), null);
    }

    private void escalateToHumanSupport(TurnContext ctx) {
        Long buyerId = ctx.parseNumericUserId();
        String guestEmail = (String) ctx.session.slots.getOrDefault("guestEmail", "guest@marketplace.local");
        String reason = (String) ctx.session.slots.getOrDefault("escalationReason", ctx.rawText);

        try {
            Conversation supportChat = messengerService.openSupportChat(buyerId, guestEmail);
            messengerService.sendMessage(
                    supportChat.getId(),
                    buyerId,
                    "[Перевод от бота] Причина обращения: " + reason
            );
            ctx.escalatedChatId = supportChat.getId();
        } catch (Exception e) {
            System.err.println("Не удалось создать чат поддержки: " + e.getMessage());
        }
    }
}