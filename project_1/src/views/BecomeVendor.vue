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
</template>

<style scoped>
.become_vendor_main
{
    display: flex;
    flex-direction: column;
    justify-content: center;
    text-align: center;
    align-items: center;
    gap: 30px;
}
.input_vendor_info
{
    width: 100%;
    height: 56px;
    border-radius: 40px;
    border: 1px solid #f0edf0;
    background: #fcfbfc;
    padding: 0 20px;
    box-sizing: border-box;
    font-size: 16px;
    outline: none;
    transition: all 0.2s ease;
}
.input_vendor_info:focus
{
   border-color: #ff7e5f;
   background-color: #ffff;
   box-shadow: 0 0 0 4px rgba(255, 126, 95, 0.1);
}
.input_vendor_info::placeholder
{
    text-align: center;
    color: #9c9a9c;
    font-size: 16px;
}
.preface_become_vendor
{
    hyphens: auto;
}
.agreement_vendor
{
    margin-top: 25px;
    max-width: 450px;
    font-family: 'Lucida Sans';
    border-bottom: 1px solid #eaeaea;
    padding-bottom: 10px;
}
.agreement_vendor summary
{
    font-size: 18px;
    font-weight: 700;
    color: #000000;
    cursor: pointer;
    list-style: none;
    display: flex;
    justify-content: space-between;
    align-items: center;
    user-select: none;
}
.vendor_agreement
{
    margin-top: 12px;
    font-size: 15px;
    line-height: 1.6;
    color: #3a3a3a;
    animation: In 0.3s ease-out;
}
.create_vendor
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
.create_vendor:hover
{
    opacity: 0.9;
}

</style>
