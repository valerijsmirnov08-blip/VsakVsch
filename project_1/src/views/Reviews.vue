<script setup>
import { ref, onMounted } from 'vue';
import router from '@/router';
import { computed } from 'vue';
import { ElRate } from 'element-plus';
import { tr } from 'element-plus/es/locales.mjs';

const order = ref([]);
const product = ref([]);
const currentTab = ref('Доставлен');
const loading = ref(true);
const ratingValue = ref(0);
const showModel = ref(false);
const selectedProduct = ref(null);
const commentText = ref('');
const complaintTextContent = ref('');
const is_reviewed = ref(false);
const selectedOrderItem = ref(null);
const gallery = ref([]);
const galleryPreview = ref([]);
const isComplaint = ref(false);

const selectedParentIds = ref([])
const selectedSubIds = ref([])
const allCategories = ref([]);
const GalleryDragover = ref(false);

const ErrorMessage = ref('');



    const mainCategories = computed(() => {
        return allCategories.value.filter(item => item.id === item.parentId);
    });

    const subCategories = computed(() => {
        if (selectedParentIds.value.length === 0) return [];
        return allCategories.value.filter(item => 
            item.id !== item.parentId && 
            selectedParentIds.value.includes(item.parentId)
        );
    });
    const LoadCategories = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/categories/complaints');
            if (response.ok) {
                allCategories.value = await response.json();
            }
        } catch (error) {
            console.error("Ошибка загрузки категорий:", error);
        }
    }
    const LoadProducts =  async() =>
    {
        const session = localStorage.getItem('user_session')
        const user = JSON.parse(session);
        const userId = user.id;

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
        gallery.value = [];
        galleryPreview.value = [];
        GalleryDragover.value = false;
        selectedParentIds.value = [];
        selectedSubIds.value = [];
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
        comment: commentText.value,
        photoUrl: gallery.value,
        orderItemId: selectedOrderItem.value.id,
        isComplaint: isComplaint.value,
        complaintCategoryIds: isComplaint.value ? selectedSubIds.value : [],
        complaintText: isComplaint.value ? complaintTextContent.value : null

        
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
            console.log("Статус жалобы перед отправкой:", isComplaint.value);
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


const galleryDrop = (a) =>
{
    GalleryDragover.value = false;
    const filesList = a.dataTransfer.files;
    if(filesList && filesList.length > 0)
    {
        const files = Array.from(filesList);
        const namesArray = files.map(file => `${file.name}`);
        gallery.value = namesArray;
        galleryPreview.value = files.map(file => URL.createObjectURL(file));
    }
}


const filteredOrders = computed(() =>
{
    return order.value.filter(item => item.status === 'Доставлен');
});
    onMounted(() =>
    {
        LoadProducts()
        LoadCategories()
    })

</script>

<template>
    <RouterLink to="/accaunt" class="link">
            <button class="back"><img src="/Назад.png" alt="Назад"></button>
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
               
            </div>
            
            <button v-if="!item.isReviewed" @click="OpenReview(item)" class="estimate">Оценить</button>
            <span v-else style="color: #ff7e5f; font-weight: 600; font-size: 18px;">Отзыв оставлен</span>
        </div>
        </div>
        <div v-if="showModel" class="modal_review" @click.self="CloseReview">
            <div class="modal_review_wrapper" @click.stop>
            <div class="modal_review_window">
                <h3 style="font-size: 40px;">Оценить товар</h3>
                <img class="modal_review_window_img" :src="selectedProduct.mainImage" alt="">
                <div class="modal_review_window_important_info">
                    <p>{{ selectedProduct?.brand }} </p>
                    <p>{{ selectedProduct?.model }}</p>
                </div>
                <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 10px;">
                    <input type="checkbox" id="complaint" v-model="isComplaint">
                    <label for="complaint" style="color: #ff5b32; font-weight: bold;">Это жалоба на товар</label>
                </div>
                <p>Срок доставки {{ selectedProduct?.estimatedDelivery }}</p>
                <p>Общая оценка товара товара <el-rate v-model="ratingValue" clearable /></p>
                <div class="put_photo"
                    :class="{'dragover_active': GalleryDragover}"
                    @dragover.prevent = "GalleryDragover = true"
                    @dragenter.prevent = "GalleryDragover = true"
                    @dragleave.prevent = "GalleryDragover = false"
                    @drop.prevent = "galleryDrop">
                    <div v-if="galleryPreview.length === 0">
                        <p>{{ galleryPreview.length }} шт.</p>
                        
                    </div>
                    <div v-else>
                        <div v-for="(src, idx) in galleryPreview" :key="idx">
                            <img :src="src" alt="">
                        </div>
                    </div>
                    
                </div>
                <textarea v-model="commentText" placeholder="Раскажите про товар"></textarea>
                <div class="modal_review_window_buttons">
                    <button @click="CloseReview">Закрыть</button>
                    <button @click="SaveReview">Сохранить</button>
                </div>
                
            </div>
            <transition name="slide-right">
            <div v-if="isComplaint" class="modal_review_complaint_panel">
                <h3 style="font-size: 30px; text-align: center; margin-bottom: 15px;">Детали жалобы</h3>
                
                <div style="display: flex; flex-direction: column; gap: 10px; flex-grow: 1;">
                    <div v-for="cat in mainCategories" :key="cat.id" style="font-size: 16px;">
                        <input type="checkbox" :id="'cat-'+cat.id" :value="cat.id" v-model="selectedParentIds">
                        <label :for="'cat-'+cat.id" style="margin-left: 8px;">{{ cat.name }}</label>
                    </div>

                    <div v-if="subCategories.length > 0" style="margin-top: 15px; display: flex; flex-direction: column; height: 100%;">
                        <p style="font-weight: bold; margin-bottom: 5px;">Уточните причину:</p>
                        <div style="flex-grow: 1; border-radius: 10px; padding: 10px; border: 1px solid #ccc; max-height: 150px; overflow-y: auto; display: flex; flex-direction: column; gap: 8px;">
                            <div v-for="sub in subCategories" :key="sub.id" style="display: flex; align-items: center;">
                                <input type="checkbox" :id="'sub-'+sub.id" :value="sub.id" v-model="selectedSubIds" style="cursor: pointer;">
                                <label :for="'sub-'+sub.id" style="margin-left: 8px; cursor: pointer;">{{ sub.name }}</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </transition>
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
.put_photo {
    width: 400px;
    min-height: 100px;
    border: 2px dashed #ccc;
    border-radius: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f9f9f9;
    transition: all 0.3s ease;
    cursor: pointer;
    overflow: hidden;
}

.dragover_active {
    border-color: #ff7e5f;
    background-color: rgba(255, 126, 95, 0.1);
    transform: scale(1.02);
}
.put_photo img {
    height: 80px;
    width: 80px;
    object-fit: cover;
    border-radius: 10px;
    margin: 5px;
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
    gap: 8px;
    background: #ffffff;
    padding: 20px;
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
    width: 200px;
    height: 250px;
    object-fit: contain;
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
.modal_review_wrapper {
    display: flex;
    gap: 20px;
    align-items: center;
    height: 100%;
}

.modal_review_complaint_panel {
    display: flex;
    flex-direction: column;
    background: #ffffff;
    padding: 18px;
    border-radius: 16px;
    width: 336px;
    height: 75%;
    box-sizing: border-box;
    overflow-y: auto;
}

.slide-right-enter-active,
.slide-right-leave-active {
    transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}
.slide-right-enter-from,
.slide-right-leave-to {
    opacity: 0;
    transform: translateX(-50px) scale(0.95);
}
</style>
