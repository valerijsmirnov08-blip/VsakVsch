<script setup>
import { ref, nextTick, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { API_URL } from '@/composables/apiConfig.js';

const router = useRouter();
const windowHelp = ref(false);
const chatMesseges = ref([]);
const isBotTyping = ref(false);
const userId = ref('');
const customInput = ref('');
const escalatedChatId = ref(null);

const defaultQuickReplies = [
    'Я не помню свой пароль',
    'Почему мой заказ задерживается?',
    'Как стать продавцом?',
    'Как зарегистрироваться?',
    'Позвать оператора'
];

const quickReplies = ref([...defaultQuickReplies]);

onMounted(() => {
    const session = localStorage.getItem('user_session');
    if (session) {
        const userData = JSON.parse(session);
        userId.value = String(userData.id || userData.userId || '');
    }
    if (!userId.value) {
        let guestId = localStorage.getItem('guest_bot_session');
        if (!guestId) {
            guestId = 'guest_' + Date.now();
            localStorage.setItem('guest_bot_session', guestId);
        }
        userId.value = guestId;
    }
});

const scrollToBottom = async () => {
    await nextTick();
    const container = document.querySelector('.chat_messeges');
    if (container) {
        container.scrollTop = container.scrollHeight;
    }
};

const windowHelpBot = async () => {
    windowHelp.value = !windowHelp.value;
    if (windowHelp.value) {
        await loadChatHistory();
        await scrollToBottom();
    }
};

const sendMessegeForBot = async (text) => {
    const trimmed = (text || '').trim();
    if (!trimmed || isBotTyping.value) return;

    chatMesseges.value.push({
        id: Date.now(),
        sender: 'user',
        text: trimmed
    });
    customInput.value = '';
    isBotTyping.value = true;
    await scrollToBottom();

    try {
        const response = await fetch(`${API_URL}/chat/bot`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ message: trimmed, userId: userId.value })
        });

        if (response.ok) {
            const data = await response.json();

            chatMesseges.value.push({
                id: Date.now() + 1,
                sender: 'bot',
                text: data.reply
            });

            if (Array.isArray(data.quickReplies) && data.quickReplies.length > 0) {
                quickReplies.value = data.quickReplies;
            }

            if (data.escalatedChatId) {
                escalatedChatId.value = data.escalatedChatId;
            }
        } else {
            console.error('Ошибка сервера:', response.status);
        }
    } catch (error) {
        console.error('Ошибка связи с сервером:', error);
    } finally {
        isBotTyping.value = false;
        await scrollToBottom();
    }
};

const openLiveSupportChat = () => {
    if (!escalatedChatId.value) return;
    const targetId = escalatedChatId.value;
    windowHelp.value = false;
    router.push({ path: '/messenger', query: { chatId: targetId } });
};

const loadChatHistory = async () => {
    if (!userId.value) return;
    try {
        const response = await fetch(`${API_URL}/chat/history/${userId.value}`);
        if (response.ok) {
            const history = await response.json();
            chatMesseges.value = history;
            if (chatMesseges.value.length === 0) {
                chatMesseges.value.push({
                    id: Date.now(),
                    sender: 'bot',
                    text: 'Здравствуйте! Чем я могу вам помочь?'
                });
            }
        }
    } catch (error) {
        console.error('Ошибка загрузки истории:', error);
    }
};
</script>

<template>
    <button v-if="!windowHelp" class="floating-chat-btn" @click="windowHelpBot">
        Поддержка
    </button>

    <div v-if="windowHelp" class="windowChat">
        <div class="chat_head">
            <h3>Поддержка</h3>
            <button @click="windowHelpBot" class="close_bot">✖</button>
        </div>

        <div class="chat_messeges">
            <div
                v-for="msg in chatMesseges"
                :key="msg.id"
                :class="['bot_messeges', msg.sender === 'bot' ? 'bot_msg_style' : 'user_msg_style']"
            >
                <p :class="msg.sender === 'bot' ? 'bot_messege' : 'user_messege'">
                    {{ msg.text }}
                </p>
            </div>

            <div v-if="isBotTyping" class="bot_msg_style loader-container">
                <div class="spinner"></div>
            </div>
        </div>

        <button
            v-if="escalatedChatId"
            class="live_chat_btn"
            @click="openLiveSupportChat"
        >
            Перейти в чат с оператором →
        </button>

        <div class="chat_quick_replaies">
            <button
                v-for="btnText in quickReplies"
                :key="btnText"
                @click="sendMessegeForBot(btnText)"
                :class="['replay_btn', btnText.toLowerCase().includes('оператор') ? 'alert' : '']"
            >
                {{ btnText }}
            </button>
        </div>

        <form class="chat_input_form" @submit.prevent="sendMessegeForBot(customInput)">
            <input
                v-model="customInput"
                type="text"
                placeholder="Напишите сообщение..."
                :disabled="isBotTyping"
            />
            <button type="submit" :disabled="isBotTyping || !customInput.trim()">➤</button>
        </form>
    </div>
</template>

<style scoped>
.windowChat {
    display: flex;
    flex-direction: column;
    padding: 16px;
    box-sizing: border-box;
    position: fixed;
    bottom: 20px;
    right: 20px;
    width: 370px;
    height: 540px;
    background-color: #ffffff;
    border: 1px solid #ccc;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
    z-index: 1000;
}
.chat_head {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #eee;
    padding-bottom: 10px;
    margin-bottom: 10px;
}
.chat_head h3 {
    margin: 0;
    font-size: 18px;
    color: #333;
}
.close_bot {
    background: none;
    border: none;
    font-size: 20px;
    cursor: pointer;
    color: #888;
}
.close_bot:hover {
    color: #333;
}
.chat_messeges {
    flex: 1;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    gap: 10px;
    margin-bottom: 10px;
    padding-right: 4px;
}
.bot_messeges {
    max-width: 85%;
    font-size: 14px;
    line-height: 1.4;
}
.bot_messege {
    background-color: #f0ebf4;
    padding: 10px 14px;
    border-radius: 15px;
    border-bottom-left-radius: 5px;
    color: #333;
    margin: 0;
}
.bot_msg_style {
    align-self: flex-start;
}
.user_msg_style {
    align-self: flex-end;
}
.user_messege {
    background-color: #ff7e5f;
    color: #fff;
    padding: 10px 14px;
    border-radius: 15px;
    border-bottom-right-radius: 5px;
    margin: 0;
}
.live_chat_btn {
    width: 100%;
    padding: 10px;
    margin-bottom: 10px;
    background: #10b981;
    color: #fff;
    border: none;
    border-radius: 8px;
    font-weight: 600;
    cursor: pointer;
    transition: background 0.15s;
}
.live_chat_btn:hover {
    background: #059669;
}
.chat_quick_replaies {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
    margin-bottom: 10px;
    max-height: 110px;
    overflow-y: auto;
}
.replay_btn {
    background-color: #fff;
    border: 1px solid #feb47b;
    color: #ff7e5f;
    padding: 6px 11px;
    border-radius: 20px;
    font-size: 12px;
    cursor: pointer;
    transition: all 0.15s;
}
.replay_btn:hover {
    background-color: #ff7e5f;
    color: #fff;
}
.replay_btn.alert {
    border-color: #ccc;
    color: #666;
}
.replay_btn.alert:hover {
    background-color: #666;
    color: #fff;
}
.chat_input_form {
    display: flex;
    gap: 8px;
    border-top: 1px solid #eee;
    padding-top: 10px;
}
.chat_input_form input {
    flex: 1;
    padding: 8px 12px;
    border: 1px solid #ddd;
    border-radius: 20px;
    font-size: 13px;
    outline: none;
}
.chat_input_form input:focus {
    border-color: #ff7e5f;
}
.chat_input_form button {
    background: #ff7e5f;
    color: #fff;
    border: none;
    width: 36px;
    height: 36px;
    border-radius: 50%;
    cursor: pointer;
    font-size: 14px;
}
.chat_input_form button:disabled {
    background: #ccc;
    cursor: not-allowed;
}
.loader-container {
    padding: 10px 15px;
    align-self: flex-start;
}
.spinner {
    width: 22px;
    height: 22px;
    border: 3px solid #f0ebf4;
    border-top: 3px solid #ff7e5f;
    border-radius: 50%;
    animation: spin 1s linear infinite;
}
@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
.floating-chat-btn {
    position: fixed;
    bottom: 20px;
    right: 20px;
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    color: white;
    border: none;
    padding: 15px 25px;
    border-radius: 30px;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    box-shadow: 0 4px 15px rgba(255, 126, 95, 0.3);
    z-index: 1000;
    transition: transform 0.2s;
}
.floating-chat-btn:hover {
    transform: scale(1.05);
}
</style>