<script setup>
import { ref, computed } from 'vue';
import router from '@/router';
import { RouterLink } from 'vue-router';
import { useRouter, useRoute } from 'vue-router';
import { onMounted } from 'vue';

    const route = useRoute()
     
    const product  =  ref(null);
    const activeImage = ref('');
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
            errorMessage.value = error.message;
        }finally
        {
            loading.value = false;
        }
    });




    const galleryImages = computed(() =>
    {
        if(!product.value)
        {
            return [];
        }
         const images = [];
        if(product.value.mainImage)
        {
            images.push(product.value.mainImage);
        }
        if(product.value.gallery)
        {
            const extra = product.value.gallery.split(',');
            images.push(...extra);
        }
        return images;
    });




    const SaveProduct = async() =>
    {
        const confirmed = confirm("Сохранить внесённые изменения?");
        if(!confirmed)
        {
            return;
        }
        try
        {
            const response = await fetch(`http://localhost:8080/api/products/${productId}`,
        {
            method: 'PUT',
            headers:{'Content-Type': 'application/json'},
            body: JSON.stringify(product.value)
        });
        if(!response.ok)
        {
            throw new Error('Ошибка при сохранении');
        }
        alert('Товар успешно обновлен!');
        router.push('/accaunt');
        }
        catch(error)
        {
            errorMessage.value = error.message;
        }
    }
    const DeleteProduct = async() =>
    {
        const confirmed = confirm("Вы точно уверены, что хотите удалить этот товар?");
        if(!confirmed)
        {
            return;
        }
        try
        {
            const response = await fetch(`http://localhost:8080/api/products/${productId}`,
            {
                method: 'DELETE'
            })
            if(!response.ok)
            {
                throw new Error('Ошибка при удалении');
            }
            router.push("/vendor_w")
        
        }
        catch(error)
        {
            errorMessage.value = error.message
        }
    }
</script>

<template>
    <div v-if="product" class="edit_product_main">
        <div class="gallery">
                
                <div class="other_photos">
                    <img
                        v-for="(img, index) in galleryImages"
                        :key = "index"
                        :src = "img"
                        class="other_photo"
                        @click="activeImage = img"
                        alt="Остальные фото">
                </div>
                <div class="main_photo">
                    <img  :src="activeImage" alt="Основоное фото">
                </div>

        </div>
        <div class="product_data">
            <div class="edit_product">
                <label>Модель</label>
                <input class="edit_input" v-model="product.model" :placeholder="product.model">
            </div>
            <div class="edit_product">
                <label>Бренд</label>
                <input class="edit_input" v-model="product.brand" :placeholder="product.brand">
            </div>
            <div class="edit_product">
                <label>Количество на складе</label>
                <input class="edit_input" v-model="product.quantity" :placeholder="product.quantity">
            </div>
            <div class="edit_product">
                <label>Описание</label>
                <input class="edit_input" v-model="product.description" :placeholder="product.description">
            </div>
            <div class="edit_product">   
                <label>Срок гарантии</label>
                <input class="edit_input" v-model="product.warranty" :placeholder="product.warranty">
            </div>
            <div class="edit_product">
                <label>Срок доставки</label>
                <input class="edit_input" v-model="product.estimatedDelivery" :placeholder="product.estimatedDelivery">
            </div>
            <div class="edit_product">
                <label>Цена</label>
                <input class="edit_input" v-model="product.price" :placeholder="product.price">
            </div>
        </div>
        
    </div>
    <div class="buttons">
        <div>
            <button class="button_delete" @click="DeleteProduct">Удалить</button>
        </div>
        <div>
            <button class="button_save" @click="SaveProduct">Сохранить изменения</button>
        </div>
    </div>
</template>

<style scoped>
.edit_product_main
{
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 100px;
    padding: 10px;
}
.edit_input
{
    width: 100%;
    height: 46px;
    border-radius: 40px;
    border: 1px solid #000000;
    background: #fcfbfc;
    padding: 0 20px;
    box-sizing: border-box;
    font-size: 16px;
    outline: none;
    transition: all 0.2s ease;
}
.edit_input::placeholder
{
    text-align: center;
    color: #9c9a9c;
    font-size: 16px;
}
.edit_input:focus
{
    border-color: #ff7e5f;
    background-color: #ffff;
    box-shadow: 0 0 0 4px rgba(255, 126, 95, 0.1);
}
.product_data
{
    display: flex;
    flex-direction: column;
    gap: 20px;
}
.gallery
{
    display: grid;
    grid-template-columns: 80px, 1fr;
}
.main_photo img
{
    width: 100%;
    height: 400px;
    object-fit: contain;
    border-radius: 12px;
}
.other_photos
{
    display: flex;
    flex-direction: column;
    gap: 10px;
    
}
.other_photo
{   
    width: 60px;
    height: 60px;
    border-radius: 8px;
    object-fit: cover;
}
.other_photo:hover
{
    cursor: pointer;
    
}
.edit_product
{
    align-items: center;
    font-size: 20px;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
}
.button_delete
{
    width: 300px;
    height: 50px;
    border-radius: 40px;
    border: none;
    background: linear-gradient(to right, #ff3908, #fca15b);
    color: #ffff;
    font-size: 18px;
    font-weight: 600;
    cursor: pointer;
    transition: opacity 0.2s;
    box-shadow: 0 4px 10px rgba(255, 126, 95, 0.2);
}
.button_save
{
    width: 300px;
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
.buttons
{
    margin-top: 80px;
    display: flex;
    justify-content: space-around;
}
</style>
