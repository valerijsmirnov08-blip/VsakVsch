<script setup>
import router from '@/router';
import {ref, onMounted, computed} from 'vue';
import { RouterLink } from 'vue-router';
import { errorMessages, parse } from 'vue/compiler-sfc';

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
const Pay = async() =>
{
    const session = localStorage.getItem('user_session');
    if(!session)
    {   
        alert("Пожалуйста войдите в аккаунт");
        return;
    }
    try
    {
        const user = JSON.parse(session);
        const response = await fetch(`http://localhost:8080/api/orders/checkout/${user.id}`,
        {
            method: 'POST'
        });
        if(response.ok)
        {
            cartItems.value = [];
            alert("Заказ успешно оформлен!");
        }
        else
        {
            const errorMessage = await response.text();
            alert("Ошибка: " + errorMessage);
        }
        
    }
    catch(error)
    {
        console.error("Ошибка при оформлении заказа:", error);
        alert("Не удалось связаться с сервером");
    }
}
onMounted(() =>
{
    loadCart();
});

</script>
<template>
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
            <div v-for="item in cartItems" :key="item.id" class="dynamic_cart_item">
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
            <button @click="Pay" class="buy_busket" :disabled= "cartItems.length === 0">Оплатить</button>
        </div>
    
   </div>
    </div>
    
</template>

<style scoped>
.main_basket {
    padding: 50px 30px;
    font-family: system-ui, -apple-system, sans-serif;
}
.main_basket h1 {
    font-size: 32px;
    margin-top: 20px;
    font-weight: 700;
}
.back_accaunt_in_basket img {
    width: 24px;
}
.back_accaunt_in_basket {
    background: none;
    border: none;
    cursor: pointer;
    opacity: 0.6;
    transition: opacity 0.2s;
}
.back_accaunt_in_basket:hover {
    opacity: 1;
}
.Basket {
    padding: 30px 0 80px 0;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 40px;
}
.elements_basket {
    display: flex;
    flex-direction: column;
    gap: 20px;
    flex-grow: 1;
}
.elements_basket h2 {
    font-size: 24px;
    color: #222;
    margin: 0;
}
.empty_basket_view {
    display: flex;
    flex-direction: column;
    gap: 20px;
}
.go_shop {
    width: 80px;
    height: 80px;
    opacity: 0.8;
}
.pay_basket {
    display: flex;
    flex-direction: column;
    width: 100%;
    max-width: 360px;
    align-items: center;
    gap: 24px;
    box-sizing: border-box;
    background-color: #ffff;
    border-radius: 20px;
    padding: 35px;
    box-shadow: 0 10px 25px rgba(0,0,0, 0.04);
    border: 1px solid #f2edf0;
    flex-shrink: 0;
}
.prise_basket {
    font-size: 26px;
    font-weight: 500;
    color: #333;
    margin: 0;
    font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
}
.prise_basket span {
    color: #b82e0c;
    font-weight: 700;
}
.buy_busket {
    width: 100%;
    height: 52px;
    border-radius: 40px;
    border: none;
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    color: #ffff;
    font-size: 18px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s ease;
    box-shadow: 0 4px 10px rgba(255, 126, 95, 0.2);
}
.buy_busket:disabled {
    background: #e0dce0;
    color: #666;
    box-shadow: none;
    cursor: not-allowed;
    opacity: 0.7;
}
.link_go_shop {
    color: #b82e0c;
    text-decoration: underline;
    font-size: 20px;
    font-weight: 600;
}

.cart_items_list {
    display: flex;
    flex-direction: column;
    gap: 16px;
    width: 100%;
}
.dynamic_cart_item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: #ffffff;
    border: 1px solid #f2edf0;
    padding: 20px;
    border-radius: 16px;
    box-shadow: 0 4px 12px rgba(0,0,0, 0.02);
}
.item_img {
    width: 80px;
    height: 80px;
    object-fit: contain;
}
.item_details {
    flex-grow: 1;
    padding-left: 20px;
}
.item_brand {
    font-size: 12px;
    color: #888;
    text-transform: uppercase;
    letter-spacing: 1px;
}
.item_model {
    margin: 4px 0;
    font-size: 18px;
    color: #222;
}
.item_sku {
    font-size: 12px;
    color: #666;
    margin: 0;
}
.item_price_zone {
    text-align: right;
    padding-right: 30px;
}
.item_price_text {
    font-size: 20px;
    font-weight: 700;
    color: #333;
    margin: 0;
}
.item_quantity_text {
    font-size: 14px;
    color: #888;
    margin: 4px 0 0 0;
}
.btn_remove_item {
    background: none;
    border: 1px solid #ff7e5f;
    color: #ff7e5f;
    padding: 8px 16px;
    border-radius: 20px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s;
}
.btn_remove_item:hover {
    background: #ff7e5f;
    color: #fff;
}
.loading_text {
    color: #666;
}
</style>