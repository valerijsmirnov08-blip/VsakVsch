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
    </script>
<template>   
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
</template>

<style scoped>
.main_Accaunt
{
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 50px;
    padding: 20px;
    background-color: #fdfbf9;
    min-height: 70vh;
    font-family: system-ui, -apple-system, sans-serif;  
}
.profil_container
{
    display: flex;
    flex-direction: column;
    gap: 30px;
    width: 400px;
    flex-shrink: 0;
    margin: 0 auto;
}
.profil_container img
{
    width: 70px;
    margin-left: 10px;
}
.profil_links
{
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 0 20px;
    text-align: center;
    font-size: 18px;
    border-radius: 30px;
    background-color: rgba(254, 180, 123, 0.8);
}
.profil_links:hover
{
    background-color: #f0ebf4;
    cursor: pointer;
}
.profil_links a
{
    text-decoration: none;
    color: #000000;
    display: inline-block;
    max-width: 100%;
    word-break: break-word;
}
.accaunt_work
{
    text-align: center;
}
.profile_content
{
    flex: 1;
    background-color: #ffffff;
    border: 1px solid #f2edf0;
    border-radius: 20px;
    padding: 40px;
    min-height: 380px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.02);
    box-sizing: border-box;
}

.profile_content h2
{
    margin-top: 0;
    color: #222222;
    font-size: 24px;
}

.profile_content p
{
    color: #666666;
    line-height: 1.6;
}
.profil_container button
{
    width: 100%;
    height: 50px;
    border-radius: 40px;
    border: none;
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    color: #ffff;
    font-size: 18px;
    font-weight: 600;
    cursor: pointer;
    transition: opacity 0.2s;
    box-shadow: 0 4px 10px rgba(255, 126, 95, 0.2);
}

.profil_container button:hover
{
    background-color: #ffebeb;
    border-color: #ffcccc;
}
</style>