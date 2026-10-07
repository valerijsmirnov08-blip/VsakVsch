<script setup>
import { computed, ref } from 'vue';
import { RouterLink } from 'vue-router';
import { useRouter } from 'vue-router';
import { onMounted } from 'vue';
import router from '@/router';

const product = ref([]);
const loading = ref(true);

const loadProductsFromVendor = async() =>
{
    const session = localStorage.getItem('user_session');
    if(!session)
    {
        alert("Пожалуйста войдите в аккаунт");
        return;
        
    }
    const user = JSON.parse(session);
    
    const vendorId = user.id;
    try
    {
        const response = await fetch(`http://localhost:8080/api/products/vendor/${vendorId}`);
        if(response.ok)
        {
            product.value = await response.json();
            console.log("Товары с сервера:", product.value);
        }
    }
    catch (error) 
    {
        console.error('Ошибка связи с сервером Java:', error);
    }
    finally
    {
        loading.value = false;
    }

}
const NotFormed = computed(() =>
{
    return product.value.filter(item => item.status === 'Не оформлен');
}) 
    onMounted(() =>
    {
        loadProductsFromVendor()
    })
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
    <div class="warehouse_main">
    <RouterLink to="/accaunt" class="link">
                <button class="back_btn"><img src="/Назад.png" alt="Назад"></button>
    </RouterLink>
    <div v-if="loading" class="loading">Загрузка товаров из базы данных...</div>
        <div v-else class="warehouse_grid">
            <div class="order_not_formalized">
                <p>Не оформленые</p>
                <div v-for="item in NotFormed" :key="item.id" class="warehouse_product_card">
                    <RouterLink :to="`/edit_product/${item.id}`" class="product_link">
                        <img class="warehouse_product_card_img" :src="item.mainImage" alt=""> 
                    </RouterLink>
                    <p>Модель: {{ item.model }}</p>
                    <p>Бренд: {{item.brand}}</p>
                    <p>Цена: {{ item.price }} ₽</p>
                    <p>Рейтинг: {{ item.rating }} ⭐</p>
                    <button class="go_edit_product" @click="router.push(`/edit_product/${item.id}`)">Изменить</button>
                    <!-- <RouterLink to="edit_product">Изменить</RouterLink> -->
                </div>
                 
            </div>
            <div class="order_formalized">
                <p>В процессе</p>
            </div>
            <div class="order_completed">
                <p>Выполнены</p>
            </div>
            <div class="new_product">
                <button @click="router.push('/create_product')" class="warehouse_create">Добавить новый товар +</button>
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
    </div>
   
</template>
