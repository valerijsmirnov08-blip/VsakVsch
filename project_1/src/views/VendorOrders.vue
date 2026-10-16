<script setup>
import { ref, onMounted, computed } from 'vue';
import router from '@/router';

const order = ref([]);
const currentTab = ref('В процессе');
const loading = ref(true);


const ErrorMessage = ref('');
const SussegeMessage = ref('');

const LoadProducts =  async() =>
{
    const session = localStorage.getItem('user_session');
    if(!session)
    {
        ErrorMessage.value = "Пожалуйста войдите в аккаунт";
        return
    }
    const user = JSON.parse(session);
    const vendorId = user.id;
    try
    {
        const response = await fetch(`http://localhost:8080/api/orders/vendor/${vendorId}`);
        if(response.ok)
        {
            order.value = await response.json();
            console.log(order.value)
        }
    }
    catch(error)
    {
        ErrorMessage.value = "Ошибка связи с сервером";
    }
    finally
    {
        loading.value = false;
    }
}

const filteredOrders = computed(() =>
{
    if (currentTab.value === 'Все')
    {
        return order.value;
    }
    return order.value.filter(item => item.status === currentTab.value);
});

const updateStatus = async(itemId, newStatus) =>
{
    try
    {
        const response = await fetch(`http://localhost:8080/api/orders/items/${itemId}/status?status=${encodeURIComponent(newStatus)}`,
        {
            method: 'PUT'
        })
        if(response.ok)
        {
            const found = order.value.find(o => o.id === itemId);
            if (found)
            {
                found.status = newStatus;
            }
        }
        else
        {
            const err = await response.text();
            alert("Ошибка смены статуса: " + err);
        }
        
    }
    catch(error)
    {
        console.error("Ошибка при обновлении статуса:", error);
    }
    
}
    const InProcessed = computed(() =>
    {
        return order.value.filter(item => item.status);
    }) 
    onMounted(() =>
    {
        LoadProducts()
    })
</script>

<template>
    <RouterLink to="/accaunt" class="back">
        <button class="back_btn"><img src="/Назад.png" alt="Назад"></button>
    </RouterLink>
    <h1>Заказы магазина</h1>
    <div class="buttons_current_tab">
        <button @click="currentTab = 'В процессе'">В процессе</button>
        <button @click="currentTab = 'Отправлен'">Отправленные</button>
        <button @click="currentTab = 'Все'">Все</button>
        <button @click="currentTab = 'Отменен'">Отмененые</button>
    </div>
    
    <div v-if="loading" class="loading">Загрузка товаров из базы данных...</div>
    <div v-else class="vendor_orders">
        <div v-for="item in filteredOrders" :key="item.id" class="vendor_order_card">
            <div class="img_vendor_card">
                 <img :src="item.product?.mainImage" alt="">
            </div>
            <div class="vendor_card_main_info">
                <p>{{ item.price }} ₽</p>
                <p>Кол-во: {{ item.quantity }} шт.</p>
                <p>{{ item.product?.rating }} ⭐</p>
            </div>
            <div class="vendor_card_post_info">
                <p>Заказчик: {{ item.order.user.userName }}</p>
                <p>Пункт выдачи:</p>
                <p>Срок доставки: {{ item.product?.estimatedDelivery }}</p>
                <p>Статус: "{{ item.status }}"</p>
                <!-- <p>{{ item.product?.brand }}</p>
                <p>{{ item.product?.estimated_delivery }}</p>
                <p>{{ item.product?.model }}</p> -->
                
                
            </div>
                <button class="button_send" @click="updateStatus(item.id, 'Отправлен')">Отправить</button>
                <button class="button_break" @click="updateStatus(item.id, 'Отменен')">Отменить</button>
                <button class="button_edit" v-if="currentTab = 'Отменен'" @click="router.push(`/edit_product/${item.product?.id}`)">Изменить</button>
            
        </div>
    </div>
</template>

<style scoped>
h1
{
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 70px;
}
.buttons_current_tab
{
    display: flex;
    justify-content: space-around;
    margin-bottom: 70px;
}
.buttons_current_tab button
{
    width: 200px;
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
.vendor_order_card
{
    display: grid;
    grid-template-columns: repeat(3,1fr);
    align-items: center;
    padding: 20px;
    margin: 20px;
    max-width: 400px;
    max-height: 400px;
    gap: 15px;
    border: 2px solid #000;
    border-radius: 20px;

}
.vendor_card_main_info
{
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 8px;
    font-size: 20px;
}
.vendor_card_post_info
{
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 8px;
}
.vendor_order_card img
{
    max-width: 200px;
    max-height: 200px;

}
.button_break
{
    width: 100%;
    height: 50px;
    border-radius: 40px;
    border: none;
    background: linear-gradient(to right, #fa542a, #f78b39);
    color: #ffff;
    font-size: 18px;
    font-weight: 600;
    cursor: pointer;
    transition: opacity 0.2s;
    box-shadow: 0 4px 10px rgba(255, 126, 95, 0.2);
}
.button_edit
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
.button_send
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
</style>
