<script setup>
import { ref,onMounted } from 'vue';
import { useProductMedia } from '@/composables/useProductMedia';
import { useVendorProducts } from '@/composables/useVendorProducts';

const emit = defineEmits(['close']);

const props = defineProps({
    productId: {
        type: Number,
        required: true
    }
});


const { product, loading, fetchProductById, updateProduct, deleteProduct } = useVendorProducts();
const { activeImage, isDragover, galleryImages, handleMainImageDrop } = useProductMedia(product);

onMounted(async () => {
    await fetchProductById(props.productId);
    if (product.value?.details?.mainImage) {
        activeImage.value = '/' + product.value.details.mainImage;
    }
});

const SaveProduct = async () => {
    if (!confirm("Сохранить внесённые изменения?")) return;
    if (await updateProduct(props.productId, product.value)) {
        alert('Товар успешно обновлен!');
        emit('close');
    }
};

const DeleteProduct = async () => {
    if (!confirm("ВНИМАНИЕ! Вы точно уверены, что хотите безвозвратно удалить этот товар?")) return;
    if (!response.ok)
    {
        const errText = await response.text();
        throw new Error(errText || 'Ошибка при удалении товара');
    }
    return true;
};
</script>

<template>
    <div v-if="loading" class="loading">Загрузка данных товара...</div>
    
    <div v-else-if="product" class="edit_container">
        <div class="editor_header">
            <button class="btn_back" @click="emit('close')">← Назад на склад</button>
            <button class="btn_save_top" @click="SaveProduct">Сохранить изменения</button>
        </div>

        <div class="edit_layout">
            <div class="gallery_section">
                <div class="main_photo_card">
                    <img :src="activeImage" alt="Основное фото" v-if="activeImage">
                    <div v-else class="no_photo">Нет фото</div>
                </div>
                
                <div class="thumbnails">
                    <img
                        v-for="(img, index) in galleryImages"
                        :key="index"
                        :src="img"
                        class="thumb_img"
                        :class="{ 'active_thumb': activeImage === img }"
                        @click="activeImage = img"
                        alt="Миниатюра">
                </div>
                <div class="upload_zone"
                :class="{ 'dragover': isDragover }"
                @dragover.prevent="isDragover = true"
                @dragleave.prevent="isDragover = false"
                @drop.prevent = "handleMainImageDrop">
                <div class="empty_state">
                    <p>+ Добавить фото</p>
                </div>    
                </div>
            </div>

            <div class="form_section">
                
                <div class="edit_card">
                    <h3>Основная информация</h3>
                    <div class="input_group">
                        <label>Бренд</label>
                        <input class="edit_input" v-model="product.brand">
                    </div>
                    <div class="input_group">
                        <label>Модель</label>
                        <input class="edit_input" v-model="product.model">
                    </div>
                    <div class="input_group">
                        <label>Описание</label>
                        <textarea class="edit_textarea" v-model="product.description" rows="4"></textarea>
                    </div>
                </div>

                <div class="edit_card">
                    <h3>Ценообразование и склад</h3>
                    <div class="grid_2col">
                        <div class="input_group">
                            <label>Цена (₽)</label>
                            <input type="number" class="edit_input" v-model="product.price">
                        </div>
                        <div class="input_group">
                            <label>Остаток (шт.)</label>
                            <input type="number" class="edit_input" v-model="product.quantity">
                        </div>
                    </div>
                </div>

                <div class="edit_card">
                    <h3>Условия и доставка</h3>
                    <div class="grid_2col">
                        <div class="input_group">
                            <label>Срок гарантии</label>
                            <select class="edit_input" v-model="product.warranty">
                                <option>Нет</option>
                                <option>1 месяц</option>
                                <option>6 месяцев</option>
                                <option>1 год</option>
                            </select>
                        </div>
                        <div class="input_group">
                            <label>Ориентировочная доставка</label>
                            <input class="edit_input" v-model="product.details.estimatedDelivery">
                        </div>
                    </div>
                </div>

                <div class="danger_zone">
                    <div class="danger_text">
                    </div>
                    <button class="btn_delete" @click="DeleteProduct">Удалить товар</button>
                </div>

            </div>
        </div>
    </div>
</template>

<style scoped>
.edit_container {
    display: flex;
    flex-direction: column;
    gap: 30px;
    padding-bottom: 50px;
}
.editor_header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #eaeaea;
    padding-bottom: 20px;
}
.btn_back {
    background: none;
    border: none;
    font-size: 16px;
    color: #666;
    cursor: pointer;
    font-weight: 500;
}
.btn_back:hover {
    color: #ff7e5f;
}
.btn_save_top {
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    color: white;
    border: none;
    padding: 10px 24px;
    border-radius: 30px;
    font-size: 16px;
    font-weight: 600;
    cursor: pointer;
    box-shadow: 0 4px 10px rgba(255, 126, 95, 0.2);
}
.edit_layout {
    display: grid;
    grid-template-columns: 400px 1fr;
    gap: 40px;
}

.gallery_section {
    display: flex;
    flex-direction: column;
    gap: 15px;
}
.main_photo_card {
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 4px 15px rgba(0,0,0,0.05);
    padding: 20px;
    height: 400px;
    display: flex;
    align-items: center;
    justify-content: center;
}
.main_photo_card img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
}
.thumbnails {
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
}
.thumb_img {
    width: 70px;
    height: 70px;
    border-radius: 10px;
    object-fit: cover;
    cursor: pointer;
    border: 2px solid transparent;
}
.active_thumb {
    border-color: #ff7e5f;
}
.upload_zone {
    height: 70px;
    border: 2px dashed #ccc;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #888;
    cursor: pointer;
    background: #f9f9f9;
}
.upload_zone:hover {
    border-color: #ff7e5f;
    color: #ff7e5f;
}

.form_section {
    display: flex;
    flex-direction: column;
    gap: 25px;
}
.edit_card {
    background: #fff;
    border-radius: 16px;
    padding: 25px;
    box-shadow: 0 4px 15px rgba(0,0,0,0.03);
    display: flex;
    flex-direction: column;
    gap: 20px;
}
.edit_card h3 {
    margin: 0;
    font-size: 20px;
    color: #222;
    border-bottom: 1px solid #f0f0f0;
    padding-bottom: 10px;
}
.input_group {
    display: flex;
    flex-direction: column;
    gap: 8px;
}
.input_group label {
    font-size: 14px;
    color: #666;
    font-weight: 500;
}
.grid_2col {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 20px;
}
.edit_input, .edit_textarea {
    width: 100%;
    background: #fcfbfc;
    border: 1px solid #e0e0e0;
    border-radius: 12px;
    padding: 12px 16px;
    font-size: 16px;
    box-sizing: border-box;
    outline: none;
    transition: all 0.2s;
    font-family: inherit;
}
.edit_textarea {
    resize: vertical;
}
.edit_input:focus, .edit_textarea:focus {
    border-color: #ff7e5f;
    background: #fff;
    box-shadow: 0 0 0 3px rgba(255, 126, 95, 0.1);
}

.danger_zone {
    border: 1px solid #ffcccc;
    background: #fffafa;
    border-radius: 16px;
    padding: 20px 25px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;
}
.danger_text h4 {
    margin: 0 0 5px 0;
    color: #d32f2f;
    font-size: 18px;
}
.danger_text p {
    margin: 0;
    color: #666;
    font-size: 14px;
}
.btn_delete {
    background: #fff;
    color: #d32f2f;
    border: 1px solid #d32f2f;
    padding: 10px 20px;
    border-radius: 30px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s;
}
.btn_delete:hover {
    background: #d32f2f;
    color: #fff;
}
.upload_zone.dragover {
    border-color: #ff7e5f;
    background-color: #fff3f0;
    color: #ff7e5f;
}
.empty_state {
    pointer-events: none;
}
</style>