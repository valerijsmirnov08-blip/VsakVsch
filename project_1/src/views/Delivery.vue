<script setup>
    import { onMounted, ref } from 'vue'
    import {useRouter} from 'vue-router';

    const product = ref([]);
    const loading = ref(true);

    const ErrorMessage = ref('')

   
    const loadProductsFromDelivery = async() =>
    {
         const session = localStorage.getItem('user_session');
        if(!session)
        {
            ErrorMessage.value = "Пожалуйста зарегистрируйтесь";
        }
        const user = JSON.parse(session);
        try
        {
            const response = await fetch(`http://localhost:8080/api/orders/user/${user.id}/items`)
            if(response.ok)
            {
                product.value = await response.json();
                console.log(product);
            }
            else
            {
                ErrorMessage.value = "Ошибка загузки";
            }
        }
        catch(error) {
            ErrorMessage.value = "Ошибка сети";
            console.error(error);
        } finally {
            loading.value = false;
        }
    }
    const refuse = async(item) =>
    {
        const confirmed = confirm("Вы уверены что хотите отказатсья?");
        if(!confirmed)
        {
            return;
        }
        try
        {
            const response = await fetch(`http://localhost:8080/api/orders/items/${item.id}/status`,
            {
                method: 'PUT',
                headers: { 'Content-Type': 'text/plain; charset=UTF-8' },
                body: 'Отказано'
            });
            if(response.ok)
            {
                item.status = 'Отказано';
            }else
            {
                alert("Не удалось изменить статус заказа");
            }
        }
        catch(error)
        {
            console.error("Ошибка сети:", error);
        }
        
    }
    onMounted (() =>
    {
        loadProductsFromDelivery();
    })
</script>

<template>
    <RouterLink to="/accaunt" class="link">
            <button class="back"><img src="/Назад.png" alt="Назад"></button>
    </RouterLink>
    <div   class="delivery_grid">
        <div v-if="loading" class="loading">Загрузка товаров</div>
        <div  v-for="item in product" :key="item.id"  class="delivery_card">
            <div class="head_delivery">
                <h3>Заказ №{{ item.order.id }}</h3>
                <p>Дата создания заказа </p>
            
            </div>
            <div class="body_delivery">
                <img :src="item.product.mainImage" alt="">
                <p>Модель: {{ item.product.model }}</p>
                <p>Кол-во: {{ item.quantity }}</p>
                <p>Цена: {{ item.price }}</p>
            </div>
            <div class="bottom_delivery">
                <p>Статус: {{ item.status }}</p>
                <button v-if="item.status !== 'Отказано' && item.status !== 'Получен'" @click="refuse(item)" class="refuse">Отказаться</button>
            </div>

        </div>
    </div>
</template>

<style scoped>
.delivery_grid
{
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 18px;
    padding: 40px;
}
.delivery_card
{
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 250px;
    min-height: 400px;
    border: 2px solid #000;
    border-radius: 20px;
    padding: 20px;
    margin: 20px;
    gap:10px;
}
.delivery_card:hover
{
    border: solid #FF9466;
    box-shadow: 4px #FF9466;
}
.delivery_card img
{
    width: 100px;
    height: 100px;
    object-fit: cover;
}
.head_delivery
{
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    font-size: 20px;
    font-family: Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;
}
.body_delivery
{
    flex-grow: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 10px;
    font-size: 18px;
}
.bottom_delivery
{
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
}
.refuse
{
    width: 150px;
    height: 40px;
    font-size: 18px;
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    border: none;
    border-radius: 40px;
    color: rgb(235, 235, 235);
    cursor: pointer;
}
</style>