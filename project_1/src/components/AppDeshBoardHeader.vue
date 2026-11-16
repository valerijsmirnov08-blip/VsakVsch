<script setup>
import { ref } from 'vue';
import { RouterLink } from 'vue-router';
import SearchModal from './SearchModal.vue';

const currentCity = ref('Великий Новгород');

const changeCity = () => {
    const newCity = prompt('Введите ваш город:', currentCity.value);
    if (newCity && newCity.trim() !== '') {
        currentCity.value = newCity.trim();
    }
};
</script>
<template>
    <header>
        <nav>
            <ul>
                <div class="left_column">
                    <button class="city_btn" @click="changeCity">
                        <img src="/Геолокация.png" alt=""> {{ currentCity }}
                    </button>
                    <li class="name_market">Всякая всячина</li>
                    <div class="seller_title">Страница продавца</div>
                </div>
                 <RouterLink to="/" class="accaunt-link">
                    <li>Главная</li>
                 </RouterLink>
                <li class="search_item"><SearchModal /></li>
                <div class="messedge">
                    <RouterLink to="/chats" class="accaunt-link">
                        <div class="icon_wrapper">
                            <img class="accaunt_img" src="/Уведомления.png" alt="">
                            <span v-if="unreadCount > 0" class="unread_badge">{{ unreadCount }}</span>
                        </div>
                        <li>Уведомления</li>
                    </RouterLink>
                </div>
                <div class="accaunt">
                <RouterLink to="/accaunt" class="accaunt-link">
                   <img class="accaunt_img" src="/Аккаунт.png" alt="">
                    <li>Аккаунт</li>
                </RouterLink>
                </div>
                <div class="basket">
                    <RouterLink to="/basket" class="accaunt-link">
                        <img class="basket_img" src="/Корзина.png" alt="">
                        <li>Корзина</li>
                    </RouterLink>
                </div>
                
            </ul>
        </nav>
    </header>
</template>
<style scoped>
.search_item {
    flex-grow: 1;
    margin: 0 40px;
}
.left_column {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 2px;
    white-space: nowrap; 
    margin-bottom: 0;
}

.city_btn {
    background: transparent;
    border: none;
    color: #ffffff;
    font-size: 18px !important;
    cursor: pointer;
    padding: 0;
    display: flex;
    align-items: center;
    gap: 5px;
    opacity: 0.9;
    transition: opacity 0.2s;
}

.city_btn img {
    width: 28px !important;
}

.name_market {
    font-size: 46px !important;
    font-weight: 800;
    margin: 0;
}

.seller_title {
    margin-top: 2px;
    font-size: 20px !important;
    color: #333333; 
    font-weight: 600;
}
.messedge {
    margin-right: 0px;
    text-align: center;
    
}
.messedge li
{
    font-size: 25px;
}

.accaunt-link {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
    font-size: 30px !important;
    font-weight: 500;
}
</style>