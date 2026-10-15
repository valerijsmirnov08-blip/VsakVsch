<script setup>
import { ref } from 'vue';
import { stringifyQuery, useRouter } from 'vue-router';
import { errorMessages } from 'vue/compiler-sfc';

const router = useRouter();

const email = ref('');
const password = ref('');
const errorMessage = ref('');
const successMessage = ref('');

const handleLogin = async () =>
{

    errorMessage.value = '';
    successMessage.value = '';


    if(!email.value.trim() || !password.value.trim())
    {
        errorMessage.value = "Заполните все поля";
        return;
    }
  try
  {
    const response = await fetch('http://localhost:8080/api/users/login',{
      method: "POST",
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({
        email: email.value.trim(),
        password: password.value
      })
    });
    if(!response.ok)
    {
      const errorData = await response.json().catch(() => ({}));
      throw new Error (errorData.message || errorData.messege || "Ошибка входа");
    }

    const userSession = await response.json();

    localStorage.setItem('user_session', JSON.stringify(userSession));

    successMessage.value = "Вход успешно выполнен"

    setTimeout(() =>
  {
    router.push('/accaunt');
  }, 1000);
  } catch (error)
  {
      errorMessage.value = error.message;
  }

}
</script>
<template>
   <div class="main_accaunt">
    <h1>Вход в Аккаунт</h1>
    <form @submit.prevent = "handleLogin" class="form">
      <input v-model="email" class="input_email" type="email" placeholder="Введите email">
      <input v-model="password" class="input_password" type="password" placeholder="Введите пароль">
      <p v-if="errorMessage" style="color: red; font-size: 14px; text-align: center; margin: 5px 0;">{{ errorMessage }}</p>
      <p v-if="successMessage" style="color: green; font-size: 14px; text-align: center; margin: 5px 0;">{{ successMessage }}</p>
      <button type="submit" class="enter_in_accaunt">Войти</button>
      <button type="button" class="help_accaunt">Помощь</button>
      <a href="">Забыли пароль?</a>
      
    </form>
      <RouterLink to="/accauntR" class="link">
        <a class="registration" href="">Нет аккаунта? Зарегестрируйтесь</a>
    </RouterLink>
      <RouterLink to="/" class="link">
        <button class="back_accaunt"><img src="/Назад.png" alt=""></button>
      </RouterLink>
   </div>
</template>

<style scoped>


.registration
{
    margin-top: 25px;
    font-size: 15px;
    color: #b82e0c;
    text-decoration: none;
    font-weight: 500;
}
.registration:hover
{
    text-decoration: underline;
}
.back_accaunt
{
    background: none;
    border: none;
    cursor: pointer;
    margin-top: 30px;
    opacity: 0.5;
    transition: opacity 0.2s;
}

</style>