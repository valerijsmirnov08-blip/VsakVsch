<script setup>
import { ref } from 'vue';
import router from '@/router';
import { RouterLink } from 'vue-router';
import { useRouter, useRoute } from 'vue-router';
import { onMounted } from 'vue';

    const route = useRoute()

    const product  =  ref(null);
    const loading = ref(true);

    const errorMessage = ref('');
    const productId = route.params.id;

    onMounted(async () => 
    {
        try
        {
            const response = await fetch(`http://localhost:8080/api/products/${productId}`);
            if(!response.ok)
            {
                throw new Error('Ошибка товар не найден');
            }
            product.value = await response.json();
            if(product.value)
            {
                activeImage.value = product.value.mainImage;
            }
        } catch (error)
        {
            errorMessege.value = error.message;
        }finally
        {
            loading.value = false;
        }
    });
// const SaveProduct = async(productId) =>
// {
//     const response = await fetch(`http://localhost:8080/api/products/`)
// }
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

    <div v-if="product" class="edit_product_main">
        <label>Модель</label>
        <input class="edit_input" :placeholder="product.model">
        <button>Изменить</button>
        <label>Бренд</label>
        <input class="edit_input" :placeholder="product.brand">
        <button>Изменить</button>
        <label>Количество на складе</label>
        <input class="edit_input" :placeholder="product.quantity">
        <button>Изменить</button>
        <label>Описание</label>
        <input class="edit_input" :placeholder="product.description">
        <button>Изменить</button>
        <label>Оценка</label>
        <input class="edit_input" :placeholder="product.warranty">
        <button>Изменить</button>
        <label>Срок доставки</label>
        <input class="edit_input" :placeholder="product.estimated_delivery">
        <button>Изменить</button>
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
