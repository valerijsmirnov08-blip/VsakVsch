<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const userName = ref('');
const email = ref('');
const password = ref('');
const passwordConfirm = ref('');
const errorMessage = ref('');
const successMessage = ref('');

const handleRegister = async () => {
    errorMessage.value = '';
    successMessage.value = '';

    if (password.value != passwordConfirm.value) {
        errorMessage.value = "Пароли не совпадают";
        return;
    }

    try {
        const response = await fetch('http://localhost:8080/api/user/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                user_name: userName.value,
                email: email.value,
                password: password.value
            })
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || errorData.messege || "Ошибка регистрации");
        }

        successMessage.value = "Аккаунт успешно создан";
        
        userName.value = '';
        email.value = '';
        password.value = '';
        passwordConfirm.value = '';

        setTimeout(() => {
            router.push('/enter_accaunt');
        }, 1500);

    } catch (error) {
        errorMessage.value = error.message;
    }
}
</script>

<template>
   <div class="main_accaunt">
    <h1>Регистрация</h1>
    <form @submit.prevent="handleRegister" class="form">
        <input v-model="userName" class="input_name" type="text" placeholder="Введите своё имя">
        <input v-model="email" class="input_email" type="email" placeholder="Введите email">
        <input v-model="password" class="input_password" type="password" placeholder="Введите пароль">
        <input v-model="passwordConfirm" class="input_password" type="password" placeholder="Введите пароль повторно">
        <p v-if="errorMessage" style="color: red; font-size: 14px; text-align: center; margin: 5px 0;">{{ errorMessage }}</p>
        <p v-if="successMessage" style="color: green; font-size: 14px; text-align: center; margin: 5px 0;">{{ successMessage }}</p>
        
        <button class="enter_in_accaunt">Создать аккаунт</button>
        <button class="help_accaunt">Помощь</button>
        <a href="">Политика коонфиденциальности</a>
    </form>
    
      <RouterLink to="/enter_accaunt" class="link">
        <button type="button" class="back_accaunt"><img src="/Назад.png" alt=""></button>
      </RouterLink>
   </div>
</template>

<style scoped>
.input_name
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
.input_name:focus
{
   border-color: #ff7e5f;
   background-color: #ffff;
   box-shadow: 0 0 0 4px rgba(255, 126, 95, 0.1);
}
.input_name::placeholder
{
    text-align: center;
    color: #9c9a9c;
    font-size: 16px;
}
</style>