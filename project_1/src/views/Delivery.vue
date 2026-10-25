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
            <h3>Заказ №{{ item.order.id }}</h3>
            <img :src="item.product.mainImage" alt="">
            <p>Статус: {{ item.status }}</p>
            <p>Модель: {{ item.product.model }}</p>
            <p>Кол-во: {{ item.quantity }}</p>
            <p>Цена: {{ item.price }}</p>
        </div>
    </div>
</template>

<style scoped>
.delivery_grid
{
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
    gap: 18px;
    padding: 40px;
}
.delivery_card
{
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 150px;
    height: 300px;
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
    width: 150px;
}
</style>