<script setup>
    import { ref, onMounted } from 'vue';
    import router from '@/router';
    import { computed } from 'vue';

    const order = ref([]);
    const product = ref([]);
    const currentTab = ref('Выполнен');
    const loading = ref(true);

    const ErrorMessage = ref('');
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

const Estimate = async() =>
{
    try
    {
        const response  = await fetch(`http://localhost:8080/api/orders/vendor/${vendorId}`)
    }
    catch(error)
    {

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
    onMounted(() =>
    {
        LoadProducts()
    })

</script>

<template>
    <RouterLink to="/accaunt" class="back">
        <button class="back_btn"><img src="/Назад.png" alt="Назад"></button>
    </RouterLink>
    <div class="reviews_main">
        <div v-if="loading" class="loading">Загрузка товаров из базы данных...</div>
        <div v-else class="orders">
            <div v-for="item in filteredOrders" :key="item.id" class="orders_card">
            <div class="">
                 <img class="main_img" :src="item.product?.mainImage" alt="">
            </div>
            <div class="orders_card_post_info">
                <p>Срок доставки: {{ item.product?.estimatedDelivery }}</p>
                <p>Статус: "{{ item.status }}"</p>
                <p>Бренд: {{ item.product?.brand }}</p>
                <p>Модель: {{ item.product?.model }}</p>
                <p>{{ item.price }} ₽</p>
                <p>Кол-во: {{ item.quantity }} шт.</p>
                <!-- <p>{{ item.product?.rating }} ⭐</p> -->
            </div>
            <!-- <div class="orders_card_main_info">
                
            </div> -->
            
            <button class="estimate">Оценить</button>
        </div>
        </div>
    </div>
</template>

<style scoped>
.reviews_main
{

}
.estimate
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
.orders
{
    display: grid;
}
.orders_card
{
    max-width: 400px;
    max-height: 400px;
    display: grid;
    align-items: center;
    grid-template-columns: repeat(2,1fr);
    gap:20px;
    border: 2px solid #000;
    border-radius: 20px;
    padding: 20px;
    margin: 20px;
}
.main_img
{
    max-width: 200px;
    /* border: 2px solid #000; */
    border-radius: 20px;
}
.orders_card_main_info
{

}
.orders_card_post_info
{
    gap: 15px;
    font-size: 20px;
    align-items: center;
}
</style>
