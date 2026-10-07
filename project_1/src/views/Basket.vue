<script setup>
import router from '@/router';
import {ref, onMounted, computed} from 'vue';
import { RouterLink } from 'vue-router';
import { parse } from 'vue/compiler-sfc';

const cartItems = ref([]);
const loading = ref(true);

const loadCart =  async () => 
{
    const session = localStorage.getItem('user_session');
    if(!session)
    {
        loading.value = false;
        return;
    }
    try
    {
        const user = JSON.parse(session);
        const response = await fetch(`http://localhost:8080/api/cart/${user.id}`);
        if(response.ok)
        {
            cartItems.value = await response.json();
        }
    }
    catch(error)
    {
        console.error('Ошибка загрузки:', error);
    }
    finally
    {
        loading.value = false;
    }
     
};
const totalCost = computed(() => {
    if (!cartItems.value || cartItems.value.length === 0) return 0;
    return cartItems.value.reduce((sum, item) => {
        const itemPrice = item.product && item.product.price ? Number(item.product.price) : 0;
        const itemQty = item.quantity ? Number(item.quantity) : 1;
        return sum + (itemPrice * itemQty);
    }, 0);
});
const deleteItem = async (cartItemId) =>
{
    try
    {
         const response = await fetch(`http://localhost:8080/api/cart/${cartItemId}`, {
            method: 'DELETE'
        });
        if (response.ok) {
            cartItems.value = cartItems.value.filter(item => item.id !== cartItemId);
        } else {
                console.error('Не удалось удалить товар')
            } 
    }
    catch (error)
        {
            console.error('Ошибка при удалении', error)
        }
};
const Plus = async (productId) =>
{
    const session = localStorage.getItem('user_session');
    if(!session)
    {
        alert("Войдите в аккаунт");
        return
    }

    try
    {
        const user = JSON.parse(session);
        const response = await fetch(`http://localhost:8080/api/cart/add`,
        {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify
            ({
                userId: Number(user.id),
                productId: Number(productId),
                quantity: 1
            })
            
        });
        if(response.ok)
        {
            const fountItem = cartItems.value.find(item => item.product.id === productId);
            if(fountItem)
            {
                fountItem.quantity ++;
            }
        }
        }
        catch(error)
        {
            console.error("Ошибка прибавления товара")
        }
}
const Minus =  async(productId) =>
{
    const session = localStorage.getItem('user_session')
    if(!session)
    {
        alert("Пожалуйста войдите в аккаунт")
        
    }
    
    try
    {
        const user = JSON.parse(session);
        const response = await fetch(`http://localhost:8080/api/cart/add`,
            {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify
                ({
                    userId: Number(user.id),
                    productId: Number(productId),
                    quantity: -1
                })
            });
            if(response.ok)
            {
                const fountItem = cartItems.value.find(item => item.product.id === productId);
                if(fountItem)
                {
                    if(fountItem.quantity === 1)
                    {
                        deleteItem(fountItem.id)
                        
                    }
                    fountItem.quantity --;
                }
            }
            
    }
    catch(error)
    {
        console.error("Ошибка удаления")
    }
    
}
const Pay = async(productId) =>
{
    const session = localStorage.getItem('user_session');
    if(!session)
    {   
        alert("Пожалуйста войдите в аккаунт");
    }
    try
    {
        const user = JSON.parse(session);
        const response = await fetch(`http://localhost:8080/api/cart/add`,
        {
            method: 'POST',
            header: {'Conten-Type': 'application/json'},
            body: JSON.stringify
            ({
                userId: Number(user.id),
                productId: Number(product.id),
                
            })
        });
    }
    catch(error)
    {

    }
}
onMounted(() =>
{
    loadCart();
});

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
    <div class="main_basket">

    <RouterLink to="/" class="link">
        <button class="back_accaunt_in_basket"><img src="/Назад.png" alt=""></button>
    </RouterLink>
   <h1>Корзина</h1>
    <div class="Basket">
    
        
        <div v-if="loading" class="elements_basket">
            <div  class="loading_text">
                <h2>Загрузка товаров</h2>
            </div>
            <img class="go_shop" src="/корзина2.png" alt="">
            <RouterLink to="/" class="link_go_shop">За покупками</RouterLink>
        </div>
        <div v-else-if="cartItems.length === 0" class="empty_basket_view">
            <h2>В корзине пусто</h2>
            <img class="go_shop" src="/корзина2.png" alt="">
            <br>
            <RouterLink to="/" class="link_go_shop">За покупками </RouterLink>
        </div>
        <div v-else class="cart_items_list">
            <div v-for="item in cartItems" :key="item.id" class="dinamic_cart_item">
                <img :src="item.product.mainImage" :alt="item.product.model" class="item_img" />
                <div class="item_details">
                    <span class="item_brand">{{ item.product.brand }}</span>
                    <h3 class="item_model">{{ item.product.model }}</h3>
                    <p class="item_sku">Артикул: {{ item.product.vendorSku }}</p>
                </div>
                <div class="item_price_zone">
                    <button @click="Plus(item.product.id)" class="item_dop_plus">+</button>
                    <button @click="Minus(item.product.id)" class="item_dop_plus">-</button>
                    <p class="item_price_text">{{ item.product.price }} ₽</p>
                    <p class="item_quantity_text">{{ item.quantity }} шт.</p>
                </div>
                <button @click="deleteItem(item.id)" class="btn_remove_item">Удалить</button>
            </div>
        </div>
        <div class="pay_basket">
            <p class="prise_basket">К оплате: <span>{{totalCost}} ₽</span></p>
            <button class="buy_busket" :disabled= "cartItems.length === 0">Оплатить</button>
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