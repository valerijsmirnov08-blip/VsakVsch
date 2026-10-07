<script setup>
    import { onMounted, ref } from 'vue'
    import {useRouter} from 'vue-router';

    const router = useRouter();

    const userDate = ref(null);

    onMounted(() => 
    {
        const session = localStorage.getItem('user_session');

        if(session)
        {
            userDate.value = JSON.parse(session);
        }else
        {
            router.push('/enter_accaunt')
        }
    })


    const handleLogout = () => 
    {
        localStorage.removeItem('user_session');
        userDate.value = null;
        router.push('/enter_accaunt')
    };
    // const isVendor = (() =>
    // {
    //     const session = localStorage.getItem('user_session')
    //     if(session)
    //     {
    //         userDate.value = JSON.parse(session);
    //     }
    //     else
    //     {
    //         router.push('/enter_accaunt');
    //     }
    //     if(userDate.is_vendor)
    //     {

    //     }
    // }) 
    </script>
<template>
        <header>
        <nav>
            <ul>
                <li class="name_market">Всякая всячина</li>
                 <RouterLink to="/" class="accaunt-link">
                    <li>Главная</li>
                 </RouterLink>
                <li><input class="search" type="text" placeholder="Поиск"></li>
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
    
    <div v-if="userDate" class="main_Accaunt">
        <h1>Здравствуйте, {{ userDate.userName }}!</h1>
        <div class="profil_container">
            
            <div class="profil_links">
                <img src="/Избранное.png" alt="">
                <a href="">Избранное</a>
            </div>
            <div class="profil_links">
                <img src="/Покупки.png" alt="">
                <a href="">Покупки</a>
            </div>
            <div class="profil_links">
                <img src="/Коробка_доставка.png" alt="">
                <a href="">Доставка</a>
            </div>
            <div v-if="!userDate.isVendor" class="profil_links">
                <img src="/Продавец_ЧБ.png" alt="">
                <RouterLink to="/vendor_b">Стать продавцом</RouterLink>
            </div>
            <div v-if="userDate.isVendor" class="profil_links">
                <img src="/ЗаказыПродавец_ЧБ.png" alt="">
                <RouterLink to="/vendor_o">Заказы</RouterLink>
            </div>
            <div v-if="userDate.isVendor" class="profil_links">
                <img src="/СтатистикаПродавец_ЧБ.png" alt="">
                <RouterLink to="/vendor_s">Статистика</RouterLink>
            </div>
            <div v-if="userDate.isVendor" class="profil_links">
                <img src="/Склад_Продавец_ЧБ.png" alt="">
                <RouterLink to="/vendor_w">Склад</RouterLink>
            </div>
            <div class="profil_links">
                <img src="/Работа.png" alt="">
                <a class="accaunt_work" href ="">Работа на складе/<wbr>Пункте выдачи</a>
            </div>
            <div class="profil_links">
                <img src="/Поддержка.png" alt="">
                <a href="">Поддержка</a>
            </div>
                <button @click="handleLogout">Выйти</button>
        </div>
       
    </div>
    <div class="bottom">
        <div class="column_bottom">
            <p class="column_title">Для покупателей</p>
            <a href="">Частые вопросы</a>
            <a href="">Доставка</a>
            <a href="">Страховка товара</a>
            <a href=""><img src="/Вацап_ЧБ.png" alt=""></a>
        </div>
        <div class="column_bottom">
            <p class="column_title">Для продавцов</p>
            <a href="">Продажа товара</a>
            <a href="">Открытие своего пунтка выдачи</a>
            <a href="">Доставка заказов</a>
            <a href=""><img src="/ТГ_Чб.png" alt=""></a>
        </div>
        <div class="column_bottom">
            <p class="column_title">Наша компания</p>
            <a href="">О нас</a>
            <a href="">Контакты</a>
            <a href="">Поддержка</a>
            <a href=""><img src="/Макс_ЧБ.png" alt=""></a>
        </div>
    </div>
</template>