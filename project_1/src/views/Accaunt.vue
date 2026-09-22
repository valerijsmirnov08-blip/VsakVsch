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
        router.push('/')
    };

    const mainPhoto = ref('/Шампунь.jpg')
    function changePhoto(newSrc)
    {
        mainPhoto.value = newSrc
    }
    </script>
<template>
  <header>
        <nav>
            <ul>
                <li class="name_market">Всякая всячина</li>
                <li><a href="">Главная</a></li>
                <li><input class="search" type="text" placeholder="Поиск"></li>
                <div class="accaunt">
                <RouterLink to="/enter_accaunt" class="accaunt-link">
                    <a href=""><img class="accaunt_img" src="/Аккаунт.png" alt=""></a>
                    <li><a href="">Аккаунт</a></li>
                </RouterLink>
                </div>
                <div class="basket">
                    <RouterLink to="/basket" class="accaunt-link">
                        <a href=""><img class="basket_img" src="/Корзина.png" alt=""></a>
                        <li><a href="">Корзина</a></li>
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
            <div class="profil_links">
                <img src="/Продавец_ЧБ.png" alt="">
                <a href="http://localhost:5173/vendor_b">Стать продавцом</a>
            </div>
            <div class="profil_links">
                <img src="/Работа.png" alt="">
                <a class="accaunt_work" href ="">Работа на складе/<wbr>Пункте выдачи</a>
            </div>
            <div class="profil_links">
                <img src="/Поддержка.png" alt="">
                <a href="">Поддержка</a>
            </div>
            <RouterLink to="/enter_accaunt">
                <button @click="handleLogout">Выйти</button>
            </RouterLink>
            
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