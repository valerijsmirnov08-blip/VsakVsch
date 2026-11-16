<script setup>
import {ref, onMounted, onUnmounted} from 'vue';
import { useRoute } from 'vue-router';
import {messengerApi} from '@/composables/messengerApi.js'
const session = localStorage.getItem('user_session');
const parsedUser = session ? JSON.parse(session) : null;
const currentUser = ref(
    parsedUser ? { ...parsedUser, id: Number(parsedUser.id || parsedUser.userId) } : null
);
const conversations = ref([]);
const activeChat = ref(null);
const messages = ref([]);
const newMessageText = ref('');
let pollTimer = null;
const route = useRoute();

const loadConversations = async() =>
{
    if(!currentUser.value?.id)
    {
        return;
    }
    conversations.value = await messengerApi.getConversations(currentUser.value.id);
}
const selectChat = async(chat) =>
{
    activeChat.value = chat;
    messages.value = await messengerApi.getMessege(chat.id, currentUser.value.id);
}
const handleSend = async() =>
{
    const text = newMessageText.value.trim();
    if(!text || !activeChat.value)
    {
        return;
    }
    const saved = await messengerApi.sendMessege
    (
        activeChat.value.id,
        currentUser.value.id,
        text       
    );
    messages.value.push(saved);
    newMessageText.value = '';
    await loadConversations();
}
onMounted( async () =>
{
    await loadConversations();
    const targetChatId = Number(route.query.chatId);
    if(targetChatId)
    {
        const found = conversations.value.find(c => c.id === targetChatId);
        if(found)
        {
            await selectChat(found);
        }
    }
    pollTimer = setInterval(async () =>
    {
        await loadConversations();
        if(activeChat.value)
        {
            messages.value = await messengerApi.getMessege
            (
                activeChat.value.id,
                currentUser.value.id
            );
        }
    }, 3000);  
})
onUnmounted(() =>
{
    if(pollTimer)
    {
        clearInterval(pollTimer);
    }
})
</script>
<template>
    <div class="messenger_container">
        <aside class="chat_list">
            <h3>Сообщения</h3>
            <div v-if="conversations.length === 0" class="empty">
                <p>Диалогов пока нет</p>
            </div>
            <div v-for="chat in conversations" :key="chat.id" class="chat_item" :class="{active: activeChat?.id === chat.id}"
            @click="selectChat(chat)">
                <div class="chat_title">
                    <span v-if="chat.type === 'SUPPORT'" class="badge support">Поддержка</span>
                    <span v-else class="badge product">Товар</span>
                    <strong>
                        {{ chat.type === 'SUPPORT' ? (chat.buyer?.userName || chat.guestEmail || 'Гость') 
                            : (chat.product?.name || 'Чат по товару') }}
                    </strong>
                </div>
                <small class="chat_sub">
                    {{ chat.buyer?.id === currentUser.id ? 'Продавец: ' + (chat.vendor?.userName || 'Админ')
                            : 'Покупатель: ' + (chat.buyer?.userName || chat.guestEmail) }}
                </small>
            </div>
        </aside>
        <main class="chat_window">
            <div v-if="!activeChat" class="placeholder">
                <p>Выберите диалог слева, чтобы начать общение</p>
            </div>
            <template v-else>
                <div class="messeges_area">
                    <div v-for="msg in messages" :key="msg.id" class="messege_bubble" :class="{mine: msg.senderUser?.id === currentUser.id}">
                        <div class="msg_author">
                            {{ msg.sender }}
                        </div>
                        <div class="msg_text">
                            {{ msg.text }}
                        </div>
                        <span v-if="msg.senderUser?.id === currentUser.id" class="read_mark">
                            {{ msg.isRead ? '✓✓' : '✓' }}
                        </span>
                    </div>
                </div>
                <form class="input_bar" @submit.prevent="handleSend">
                    <input v-model="newMessageText" type="text" placeholder="Введите сообщение">
                    <button type="submit">Отправить</button>
                </form>
            </template>
        </main>
    </div>
</template>
<style scoped>
.messenger_container
{
    display: flex;
    height: 75vh;
    border: 1px solid #ddd;
    border-radius: 8px;
    background: #fff;
    overflow: hidden;
}
.chat_list
{
    width: 300px;
    border-right: 1px solid #ddd;
    overflow-y: auto;
    padding: 12px;
    background: #f9fafb;
}
.chat_item
{
    padding: 10px;
    border-radius: 6px;
    cursor: pointer;
    margin-bottom: 8px;
    border: 1px solid transparent;
    transition: background 0.15s;
}
.chat_item:hover
{
    background: #eef2f7;
}
.chat_item.active
{
    background: #e0ecff;
    border-color: #3b82f6;
}
.badge
{
    font-size: 11px;
    padding: 2px 6px;
    border-radius: 4px;
    margin-right: 6px;
    color: #fff;
}
.badge.support
{
    background: #ef4444;
}
.badge.product
{
    background: #10b981;
}
.chat_sub
{
    display: block;
    color: #6b7280;
    margin-top: 4px;
}
.chat_window
{
    flex: 1;
    display: flex;
    flex-direction: column;
}
.placeholder, .empty
{
    margin: auto;
    color: #9ca3af;
    text-align: center;
}
.messeges_area
{
    flex: 1;
    padding: 16px;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    gap: 10px;
}
.messege_bubble
{
    max-width: 65%;
    padding: 10px 14px;
    border-radius: 12px;
    background: #f3f4f6;
    align-self: flex-start;
    position: relative;
}
.messege_bubble.mine
{
    background: #3b82f6;
    color: #fff;
    align-self: flex-end;
}
.msg_author
{
    font-size: 11px;
    opacity: 0.75;
    margin-bottom: 4px;
}
.read_mark
{
    display: block;
    text-align: right;
    font-size: 11px;
    margin-top: 4px;
    opacity: 0.85;
}
.input_bar
{
    display: flex;
    padding: 12px;
    border-top: 1px solid #ddd;
    gap: 8px;
}
.input_bar input
{
    flex: 1;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 6px;
}
.input_bar button
{
    padding: 10px 18px;
    background: #3b82f6;
    color: #fff;
    border: none;
    border-radius: 6px;
    cursor: pointer;
}
</style>