<script setup>
import { isVNode, onMounted, ref } from 'vue'
import {useRouter} from 'vue-router';

const router = useRouter();

const shopName = ref('');
const brandName = ref('');

const userDate = ref(null);
const errorMessege = ref('');

onMounted(() =>
{
    const session = localStorage.getItem('user_session');
    if(session)
    {
        userDate.value = JSON.parse(session);
        if(userDate.value.isVendor)
        {
            router.push('/enter_accaunt');
        }else
        {
            router.push('/vendor_b');
            
        }
    }
});
const handleCreateShop = async () =>
{
    errorMessege.value = '';
    if(!shopName.value.trim())
    {
        errorMessege.value = "Пожалуйста укажите название вашего магазина";
        return;
    };
    try
    {
        const response = await fetch(`http://localhost:8080/api/users/${userDate.value.id}/become_vendor`,
        {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify
            ({
                shopName: shopName.value.trim()
            })
        })
        if(!response.ok)
        {
            const errorData = await response.json().catch(() => ({}));
            throw new Error (errorData.messege  || "Ошибука ввода данных, повтарите попытку позже");
        }
        const updateUser = await response.json();
        userDate.value = updateUser;
        localStorage.setItem('user_session', JSON.stringify(updateUser));
        // console.log(shopName.value);
        alert(`Поздравляем ваш магазин '${shopName.value}' успешно создан`);
        router.push('/accaunt');
    }
    catch (error)
    {
        errorMessege.value = error.message || error.messege || "Произошла ошибка";
    }

}

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
    <button @click="router.push('/accaunt')" class="back"><img src="/Назад.png" alt=""></button>
    <div class="become_vendor_main">
            <h1>Создание своего собственного магазина</h1>
            <p class="preface_become_vendor">Шаг до открытия своего бизнеса на Всякая всячина! Придумайте название вашего магазина, под которым ваши товары увидят миллионы покупателей</p>
            <form @submit.prevent="handleCreateShop" class="form_vendor">
                <input v-model="shopName" class="input_vendor_info" type="text" placeholder="Введите название своего магазина">
                <input v-model="brandName" class="input_vendor_info" type="text" placeholder="Введите название своего бренда(не обязательно)">
                <p v-if="errorMessege" style="color: red; font-size: 14px; margin: 0;">{{ errorMessege }}</p>
                <details>
                    <summary class="agreement_vendor">Пользовательское соглашение</summary>
                    <div class="vendor_agreement">
                        <p>Вы соглашаетесь на обработку персональных данных</p>
                    </div>
                </details>
                <button type="submit" class="create_vendor">Создать</button>
            </form>
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