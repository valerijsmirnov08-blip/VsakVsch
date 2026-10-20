<script setup>
import { ref, onMounted } from 'vue';
import router from '@/router';
import { computed } from 'vue';
import { ElRate } from 'element-plus';

const order = ref([]);
const product = ref([]);
const currentTab = ref('Выполнен');
const loading = ref(true);
const ratingValue = ref(0);
const showModel = ref(false);
const selectedProduct = ref(null);
const commentText = ref('');
const is_reviewed = ref(false);
const selectedOrderItem = ref(null);

const ErrorMessage = ref('');

const LoadProducts =  async() =>
{
    const session = localStorage.getItem('user_session')
    const user = JSON.parse(session);
    const userId = user.id; // объявляем именно userId

try {
    const response = await fetch(`http://localhost:8080/api/orders/user/${userId}/items`);
    if (response.ok) {
        order.value = await response.json();
        console.log("Заказы пользователя с бэкенда:", order.value);
    }
} catch (error) {
    ErrorMessage.value = "Ошибка связи с сервером";
    console.error(error);
}
    finally
    {
        loading.value = false;
    }
}
const OpenReview = async(item) =>
{
    selectedOrderItem.value = item;
    selectedProduct.value = item.product;
    showModel.value = true;
}
const CloseReview = async() =>
{
    selectedProduct.value = null;
    showModel.value = false;
    selectedProduct.value = null;
    commentText.value = '';
    ratingValue.value = 0;
    showModel.value = false;
}

const SaveReview = async() =>
{
    const session = localStorage.getItem('user_session');
    if(!session)
    {
        ErrorMessage.value = "Пожалуйста, войдите в аккаунт";
        return;
    }
    const user = JSON.parse(session);

    const payload = 
    {
        productId: selectedProduct.value.id,
        userId: user.id,
        rating: ratingValue.value,
        comment: commentText.value
    }

    try
    {
        const response = await fetch('http://localhost:8080/api/reviews',
            {
                method: 'POST',
                headers: {'Content-Type' : 'application/json'},
                body: JSON.stringify(payload)
            })
        if (response.ok)
        {
            const itemId = selectedOrderItem.value.id;
            await fetch(`http://localhost:8080/api/orders/items/${itemId}/reviewed`,
            {
                method: 'PUT'
            });
            selectedOrderItem.value.isReviewed = true;
            CloseReview();
        } else
        {
            console.error('Ошибка сохранения отзыва');
        }
    }
    catch(error)
    {
        ErrorMessage.value = "Ошибка связи с сервером";
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
    return order.value.filter(item => item.status === 'Выполнен');
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
            
            <button v-if="!item.isReviewed" @click="OpenReview(item)" class="estimate">Оценить</button>
            <span v-else style="color: #ff7e5f; font-weight: 600; font-size: 18px;">Отзыв оставлен</span>
        </div>
        </div>
        <div v-if="showModel" class="modal_review" @click.self="CloseReview">
            <div class="modal_review_window">
                <h3 style="font-size: 40px;">Оценить товар</h3>
                <img class="modal_review_window_img" :src="selectedProduct.mainImage" alt="">
                <div class="modal_review_window_important_info">
                    <p>{{ selectedProduct?.brand }} </p>
                    <p>{{ selectedProduct?.model }}</p>
                </div>
                
                <p>Срок доставки {{ selectedProduct?.estimatedDelivery }}</p>
                <p>Общая оценка товара товара <el-rate v-model="ratingValue" clearable /></p>
                <textarea v-model="commentText" placeholder="Раскажите про товар"></textarea>
                <div class="modal_review_window_buttons">
                    <button @click="CloseReview">Закрыть</button>
                    <button @click="SaveReview">Сохранить</button>
                </div>
                
            </div>
        </div>
    </div>
</template>

<style scoped>
/* .reviews_main
{

} */
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
/* .orders_card_main_info
{

} */
.orders_card_post_info
{
    gap: 15px;
    font-size: 20px;
    align-items: center;
}
.modal_review
{
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}
.modal_review_window
{
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    background: #ffffff;
    padding: 24px;
    border-radius: 16px;
    min-width: 320px;
}
.modal_review_window textarea
{
    border-radius: 20px;
    width: 400px;
    height: 100px;
}
.modal_review_window_img
{
    width: 400px;
    height: 500px;
}
.modal_review_window_buttons
{
    display: flex;
    gap: 40px;
    justify-content: space-between;
}
.modal_review_window_buttons button
{
    width: 200px;
    height: 50px;
    border-radius: 40px;
    border: none;
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    color: #ffffff;
    font-size: 18px;
    font-weight: 600;
    cursor: pointer;
    transition: opacity 0.2s;
    box-shadow: 0 4px 10px rgba(255, 126, 95, 0.2);
}
.modal_review_window_important_info
{
    gap: 40px;
    display: flex;
    justify-content: space-between;
    font-size: 30px;
}
</style>
