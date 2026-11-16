<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { messengerApi } from '@/composables/messengerApi.js';

const router = useRouter();

const session = localStorage.getItem('user_session');
const parsedUser = session ? JSON.parse(session) : null;
const currentUser = ref(
    parsedUser ? { ...parsedUser, id: Number(parsedUser.id || parsedUser.userId) } : null
);

const conversations = ref([]);
const activeTab = ref('ALL');
const searchQuery = ref('');
const loading = ref(true);
let pollTimer = null;

const loadConversations = async () => {
    if (!currentUser.value?.id) {
        router.push('/enter_accaunt');
        return;
    }
    try {
        conversations.value = await messengerApi.getConversations(currentUser.value.id);
    } finally {
        loading.value = false;
    }
};

const filteredConversations = computed(() => {
    const query = searchQuery.value.trim().toLowerCase();

    return conversations.value.filter(chat => {
        if (activeTab.value === 'SUPPORT' && chat.type !== 'SUPPORT') return false;
        if (activeTab.value === 'BUYING' && (chat.type === 'SUPPORT' || chat.buyer?.id !== currentUser.value?.id)) return false;
        if (activeTab.value === 'SELLING' && (chat.type === 'SUPPORT' || chat.vendor?.id !== currentUser.value?.id)) return false;

        if (!query) return true;

        const productName = (chat.product?.model || chat.product?.name || '').toLowerCase();
        const buyerName = (chat.buyer?.userName || chat.guestEmail || '').toLowerCase();
        const vendorName = (chat.vendor?.userName || '').toLowerCase();

        return productName.includes(query) || buyerName.includes(query) || vendorName.includes(query);
    });
});

const openChat = (chatId) => {
    router.push({ path: '/messenger', query: { chatId } });
};

onMounted(async () => {
    await loadConversations();
    pollTimer = setInterval(loadConversations, 5000);
});

onUnmounted(() => {
    if (pollTimer) clearInterval(pollTimer);
});
</script>
<template>
    <div class="hub_container">
        <div class="hub_header">
            <h2>Мои диалоги</h2>
            <input
                v-model="searchQuery"
                type="text"
                class="search_input"
                placeholder="Поиск по товару или собеседнику..."
            />
        </div>

        <div class="tabs_bar">
            <button
                :class="['tab_btn', { active: activeTab === 'ALL' }]"
                @click="activeTab = 'ALL'"
            >
                Все ({{ conversations.length }})
            </button>
            <button
                :class="['tab_btn', { active: activeTab === 'BUYING' }]"
                @click="activeTab = 'BUYING'"
            >
                Покупки
            </button>
            <button
                :class="['tab_btn', { active: activeTab === 'SELLING' }]"
                @click="activeTab = 'SELLING'"
            >
                Продажи
            </button>
            <button
                :class="['tab_btn', { active: activeTab === 'SUPPORT' }]"
                @click="activeTab = 'SUPPORT'"
            >
                Поддержка
            </button>
        </div>

        <div v-if="loading" class="status_box">Загрузка диалогов...</div>

        <div v-else-if="filteredConversations.length === 0" class="status_box">
            <p>В этой категории диалогов пока нет</p>
        </div>

        <div v-else class="chats_grid">
            <div
                v-for="chat in filteredConversations"
                :key="chat.id"
                class="chat_card"
                @click="openChat(chat.id)"
            >
                <div class="card_top">
                    <span v-if="chat.type === 'SUPPORT'" class="badge support">Поддержка</span>
                    <span v-else-if="chat.buyer?.id === currentUser?.id" class="badge buying">Покупка</span>
                    <span v-else class="badge selling">Продажа</span>

                    <span class="chat_id_label">Чат #{{ chat.id }}</span>
                </div>

                <h3 class="card_title">
                    {{
                        chat.type === 'SUPPORT'
                            ? 'Обращение в службу поддержки'
                            : (chat.product?.brand
                                ? `${chat.product.brand} ${chat.product.model}`
                                : (chat.product?.name || 'Чат по товару'))
                    }}
                </h3>

                <p class="card_partner">
                    <template v-if="chat.type === 'SUPPORT'">
                        Собеседник: <strong>{{ chat.vendor?.userName || 'Администратор' }}</strong>
                    </template>
                    <template v-else-if="chat.buyer?.id === currentUser?.id">
                        Продавец: <strong>{{ chat.vendor?.userName || 'Продавец' }}</strong>
                    </template>
                    <template v-else>
                        Покупатель: <strong>{{ chat.buyer?.userName || chat.guestEmail || 'Покупатель' }}</strong>
                    </template>
                </p>

                <div class="card_footer">
                    <span class="open_link">Открыть переписку →</span>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.hub_container {
    max-width: 1000px;
    margin: 40px auto;
    padding: 0 20px;
}

.hub_header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 20px;
    margin-bottom: 24px;
    flex-wrap: wrap;
}

.hub_header h2 {
    margin: 0;
    font-size: 26px;
    color: #1f2937;
}

.search_input {
    width: 320px;
    max-width: 100%;
    padding: 10px 14px;
    border: 1px solid #d1d5db;
    border-radius: 8px;
    font-size: 14px;
    outline: none;
    transition: border-color 0.2s;
}

.search_input:focus {
    border-color: #ff7e5f;
}

.tabs_bar {
    display: flex;
    gap: 10px;
    margin-bottom: 24px;
    flex-wrap: wrap;
}

.tab_btn {
    padding: 8px 18px;
    border-radius: 20px;
    border: 1px solid #e5e7eb;
    background: #f9fafb;
    color: #374151;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.15s;
}

.tab_btn:hover {
    background: #f3f4f6;
}

.tab_btn.active {
    background: #ff7e5f;
    color: #ffffff;
    border-color: #ff7e5f;
}

.status_box {
    text-align: center;
    padding: 60px 20px;
    color: #9ca3af;
    background: #f9fafb;
    border-radius: 12px;
    border: 1px dashed #d1d5db;
}

.chats_grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(290px, 1fr));
    gap: 16px;
}

.chat_card {
    background: #ffffff;
    border: 1px solid #e5e7eb;
    border-radius: 12px;
    padding: 16px;
    cursor: pointer;
    display: flex;
    flex-direction: column;
    gap: 10px;
    transition: transform 0.15s, box-shadow 0.15s, border-color 0.15s;
}

.chat_card:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.06);
    border-color: #ff7e5f;
}

.card_top {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.badge {
    font-size: 11px;
    font-weight: 700;
    padding: 3px 8px;
    border-radius: 6px;
    color: #ffffff;
}

.badge.buying {
    background: #10b981;
}

.badge.selling {
    background: #3b82f6;
}

.badge.support {
    background: #ef4444;
}

.chat_id_label {
    font-size: 12px;
    color: #9ca3af;
}

.card_title {
    margin: 0;
    font-size: 17px;
    color: #111827;
}

.card_partner {
    margin: 0;
    font-size: 14px;
    color: #4b5563;
}

.card_footer {
    margin-top: auto;
    padding-top: 8px;
    border-top: 1px solid #f3f4f6;
    text-align: right;
}

.open_link {
    font-size: 13px;
    font-weight: 600;
    color: #ff7e5f;
}
</style>