<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter, RouterLink } from 'vue-router';

const router = useRouter();

const brand = ref('');
const model = ref('');
const price = ref(0);
const vendorSku = ref('');
const mainImage = ref('');
const gallery = ref('');
const description = ref('');
const warranty = ref('1 год');
const estimatedDelivery = ref('9 августа');
const quantity = ref(0);

const errorMessage = ref('');
const successMessage = ref('');
const currentVendorId = ref(null);

const isMainDragover = ref(false);
const isGalleryDragover = ref(false);
const mainImagePreview = ref(null);
const galleryPreviews = ref([]);

onMounted(() =>
{
    const session = localStorage.getItem('user_session');
    if(!session)
    {
        alert("Пожалуйста войдите в аккаунт");
        router.push("/accaunt");
        return;
    }
    const user = JSON.parse(session);
    currentVendorId.value = user.id;
});
const handleMainImageDrop = (e) => {
    isMainDragover.value = false;
    const files = e.dataTransfer.files;
    if (files && files.length > 0) {
        const file = files[0];
        mainImage.value = `/${file.name}`;
        mainImagePreview.value = URL.createObjectURL(file);
    }
};

const handleGalleryDrop = (e) => {
    isGalleryDragover.value = false;
    const filesList = e.dataTransfer.files;
    if (filesList && filesList.length > 0) {
        const files = Array.from(filesList);
        const namesArray = files.map(file => `/${file.name}`);
        gallery.value = namesArray.join(', ');
        galleryPreviews.value = files.map(file => URL.createObjectURL(file));
    }
};
const areBothImagesLoaded = computed(() => {
    return mainImagePreview.value !== null;
});
const handleCreateProduct = async() =>
{
    errorMessage.value= '';
    successMessage.value = '';

    if(!brand.value.trim() || !model.value.trim() || !vendorSku.value.trim())
    {
        errorMessage.value = "Заполните обязательно поля (Бренд, Модель, Артикул)";
        return;
    }
    try
    {
        const response = await fetch('http://localhost:8080/api/products/create',
        {
                method: 'POST',
                headers:{'Content-Type': 'application/json'},
                body:
                JSON.stringify
                ({
                    vendorId: currentVendorId.value,
                    vendorSku: vendorSku.value,
                    brand: brand.value.trim(),
                    model: model.value.trim(),
                    price: Number(price.value),
                    rating: 0.0,
                    estimatedDelivery: estimatedDelivery.value,
                    warranty: warranty.value,
                    description: description.value.trim(),
                    mainImage: mainImage.value.trim() || "/Шампунь.jpg",
                    gallery: gallery.value.trim(),
                    quantity: Number(quantity.value)
                })
        });
        if(response.ok)
        {
            successMessage.value = "Товар успешно добавлен";
            setTimeout(() =>
            {
                router.push('/accaunt');
            }, 1500);
        } else
        {
            const errText = await response.text();
            throw new Error(errText || "Ошибка при создании товара");
        }
    } catch(error)
    {
        errorMessage.value = error.message;
    }
}
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
     <div class="form-page-container">
        <div class="main_create_product">
        <RouterLink to="/accaunt" class="link">
            <button class="back_btn"><img src="/Назад.png" alt="Назад"></button>
        </RouterLink>
    </div>
    <h1>Создание карточки товара</h1>
    <form @submit.prevent="handleCreateProduct" class="product_form">
        <div class="form_group">
            <label>Бренд</label>
            <input v-model="brand" type="text" placeholder="На пример Puma">
        </div>
        <div class="form_group">
            <label>Модель / Название</label>
            <input v-model="model" type="text" placeholder="На пример Худи чёрная">
        </div>
        <div class="form_group">
            <label>Артикул</label>
            <input v-model="vendorSku" type="text" placeholder="На пример 452134">
        </div>
        <div class="form_group">
            <label>Цена товара</label>
            <input v-model="price" type="number" placeholder="Цена">
        </div>
        <div class="form_group">
            <label>Количество на складе</label>
            <input v-model.number="quantity" type="number" placeholder="Цена">
        </div>
         <div class="form_group">
            <label>Главное изображение товара *</label>
            <div 
                class="drop_zone"
                :class="{ dragover: isMainDragover }"
                @dragover.prevent="isMainDragover = true"
                @dragenter.prevent="isMainDragover = true"
                @dragleave.prevent="isMainDragover = false"
                @drop.prevent="handleMainImageDrop">
                <div v-if="!mainImagePreview" class="drop_zone_content">
                    <img src="/Корзина.png" class="upload_icon" alt="">
                    <p v-if="!mainImagePreview">Перетащите сюда главное фото</p>
                </div>
                <div v-else class="preview_container">
                    <img :src="mainImagePreview" class="main_image_preview" alt="">
                    <p class="file_path_text">Путь в БД: {{ mainImage }}</p>
                </div>
            </div>
            <input v-model="mainImage" type="text" placeholder="Или укажите путь текстом">
        </div>

        <div class="form_group">
            <label>Дополнительные фото для вертикальной галереи (несколько штук) *</label>
            <div 
                class="drop_zone" 
                :class="{ dragover: isGalleryDragover }"
                @dragover.prevent="isGalleryDragover = true"
                @dragenter.prevent="isGalleryDragover = true"
                @dragleave.prevent="isGalleryDragover = false"
                @drop.prevent="handleGalleryDrop">
                <div v-if="galleryPreviews.length === 0" class="drop_zone_content">
                    <img src="/Корзина.png" class="upload_icon" alt="">
                    <p v-if="galleryPreviews.length === 0">Перетащите сюда файлы галереи (выделите мышкой несколько)</p>
                    <p v-else style="color: #4caf50; font-weight: 600;">✓ Загружено фото для галереи: {{ galleryPreviews.length }} шт. (ожидаем главное фото)</p>
                </div>
                <div v-else class="gallery_previews_grid">
                    <div v-for="(src, idx) in galleryPreviews" :key="idx" class="gallery_preview_wrapper">
                        <img :src="src" class="gallery_img_preview" alt="Превью галереи">
                    </div>
                </div>
            </div>
            <p v-if="gallery && areBothImagesLoaded" class="file_path_text">Пути в БД: {{ gallery }}</p>
        </div>
        <div class="form_group">
            <label>Описание</label>
            <input v-model="description" type="text" placeholder="Ваше детально описание">
        </div>
        <p v-if="errorMessage" class="msg error">{{ errorMessage }}</p>
        <p v-if="successMessage" class="msg success">{{ successMessage }}</p>
            
         <button type="submit" :disabled="!areBothImagesLoaded" class="btn_submit_product">Выставить на витрину</button>
    </form>
     </div>
    

</template>