package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.ChatMessage;
import com.example.demo.repository.ChatMessageRepository;

@Service 
public class BotService
{
    @Autowired 
    private  ChatMessageRepository chatMessageRepository;


    public String procceseAndSaveMessege(String userId,String userMessage)
    {
        chatMessageRepository.save(new ChatMessage(userId, "user", userMessage));
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException a)
        {
            Thread.currentThread().interrupt();
        }
        String botReplay;
        if(userMessage == null)
        {
            botReplay = "Ошибка: пустой запрос.";
        }
        else
        {
            switch (userMessage)
            {
            case "Как зарегистрироваться?":
                botReplay = "Для регистрации перейдите на страницу входа и нажмите вкладку 'Регистрация'. Вам понадобится только email и придумать свой надёжный пароль.";
                break;
            case "Как стать продавцом?":
                botReplay = "В вашем профиле есть кнопка 'Стать продавцом'. Нажмите её, заполните реквизиты магазина, и мы проверим вашу заявку за 1 день.";
                break;
            case "Почему мой заказ задерживается?":
                botReplay = "Обычно доставка занимает от 2 до 5 дней. Если прошло больше времени, напишите номер вашего заказа ниже.";
                break;
            case "Не отправляется заказ":
                botReplay = "Пожалуйста, проверьте, заполнен ли адрес доставки в профиле и привязана ли банковская карта.";
                break;
            case "Позвать оператора":
                botReplay = "Перевожу диалог на свободного специалиста. Пожалуйста, подождите... (в разработке)";
                break;
            default:
                botReplay = "Извините, я вас не понял. Пожалуйста, выберите один из вариантов меню.";
                break;
            }
        }
        chatMessageRepository.save(new ChatMessage(userId, "bot", botReplay));
        return botReplay;
    }





    // public String getBotResponse(String userMessege)
    // {
    //     try
    //     {
    //         Thread.sleep(1000); 
    //     } catch (InterruptedException e)
    //     {
    //         Thread.currentThread().interrupt();
    //     }
    //     if(userMessege == null)
    //     {
    //         return "Ошибка: пустой запрос. Пожалуйста, выберите вариант из меню.";
    //     }
    //     switch (userMessege) {
    //         case "Как зарегистрироваться?":
    //            return "Для регистрации перейдите на страницу входа и нажмите вкладку 'Регистрация'. Вам понадобится только email и придумать свой надёжный пароь.";
    //         case "Как стать продавцом?":
    //             return "В вашем профиле есть кнопка 'Стать продавцом'. Нажмите её, заполните реквизиты магазина, и мы проверим вашу заявку за 1 день.";
    //         case "Почему мой заказ задерживается?":
    //             return "Обычно доставка занимает от 2 до 5 дней. Если прошло больше времени, напишите номер вашего заказа ниже.";
    //         case "Не отправляется заказ":
    //             return "Пожалуйста, проверьте, заполнен ли адрес доставки в профиле и привязана ли банковская карта.";
    //         case "Позвать оператора":
    //             return "Перевожу диалог на свободного специалиста. Пожалуйста, подождите... (в разработке)";
    //         default:
    //             return "Извините, я вас не понял. Пожалуйста, выберите один из вариантов меню.";
    //     }
    // }
}
