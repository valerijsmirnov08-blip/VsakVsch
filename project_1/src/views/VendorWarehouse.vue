<script setup>
import { computed, ref } from 'vue';
import { RouterLink } from 'vue-router';
import { useRouter } from 'vue-router';
import { onMounted } from 'vue';
import router from '@/router';

const product = ref([]);
const loading = ref(true);
const currentTab = ref('В процессе');
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
            // console.log("Товары с сервера:", product.value);
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


const ProductsStatus = computed(() =>
{
    return product.value.filter(item => item.id);
}) 
    onMounted(() =>
    {
        loadProductsFromVendor()
    })
</script>

<template>
    <div class="warehouse_main">
    <RouterLink to="/accaunt" class="link">
            <button class="back"><img src="/Назад.png" alt="Назад"></button>
    </RouterLink>
    <div v-if="loading" class="loading">Загрузка товаров из базы данных...</div>
        <div v-else class="warehouse_grid">
            <p class="products_in_warehouse">Товары на складе</p>
            <div class="order_not_formalized">
                <div v-for="item in ProductsStatus" :key="item.id" class="warehouse_product_card">
                    <RouterLink :to="`/edit_product/${item.id}`" class="product_link">
                        <img class="warehouse_product_card_img" :src="item.mainImage" alt=""> 
                    </RouterLink>
                    <p>Модель: {{ item.model }}</p>
                    <p>Бренд: {{item.brand}}</p>
                    <p>Цена: {{ item.price }} ₽</p>
                    <p>Рейтинг: {{ item.rating }} ⭐</p>
                    <p>Количество на складе: {{ item.quantity }}</p>
                    <button class="go_edit_product" @click="router.push(`/edit_product/${item.id}`)">Изменить</button>
                </div>
                 
            </div>
            <div class="new_product">
                <button @click="router.push('/create_product')" class="warehouse_create">Добавить новый товар +</button>
            </div>
        </div>
    </div>
   
</template>

<style scoped>
.warehouse_main
{
    margin: 20px;
    padding: 20px;
}
.products_in_warehouse
{
    font-size: 24px;
    font-family: 'Lucida Sans';
}
.warehouse_grid
{
    margin: 20px;
    padding: 20px;
    align-items: center;  
    text-align: center;
    
}
.order_not_formalized
{
    margin-top: 70px;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 25px;
}
.order_formalized
{
    
    border-right: 3px solid #000000;
}
.warehouse_create
{
    width: 200px;
    height: 70px;
    background-color: #FF9466;
    border-radius: 40px;
    cursor: pointer;
}
.warehouse_create a
{
    text-decoration: none;
    color: #000000;

}
.warehouse_product_card
{
    max-width: 200px;
    max-height: 400px;
    display: flex;
    flex-direction: column;
    align-items: center;
    border: 2px solid #b8b8b8;
    border-radius: 40px;
    gap: 7px;
    margin: 10px 20px 10px 20px;
    padding: 20px;
}
.warehouse_product_card_img
{
    height: 180px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 12px;
}
.go_edit_product
{
    background-color: #ff7e5f;
    color: #ffffff;
    margin-top: 15px;
    width: 100px;
    height: 35px;
    border-radius: 30px;
    border-style: none;
}
.go_edit_product:hover
{
    cursor: pointer;
}
</style>
