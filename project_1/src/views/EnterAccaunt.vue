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
<template class="main_accaunt">
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