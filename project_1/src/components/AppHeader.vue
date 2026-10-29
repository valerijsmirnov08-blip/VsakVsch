<script setup>
import { RouterLink } from 'vue-router';
import SearchModal from './SearchModal.vue';
</script>
<template>
    <header>
        <nav>
            <ul>
                <li class="name_market">Всякая всячина</li>
                 <RouterLink to="/" class="accaunt-link">
                    <li>Главная</li>
                 </RouterLink>
                <li><SearchModal /></li>
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