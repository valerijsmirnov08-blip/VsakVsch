<script setup>
import { computed, ref, onMounted } from 'vue';
import VendorEditProduct from '@/components/VendorEditProduct.vue';

const product = ref([]);
const loading = ref(true);
const editingProductId = ref(null);

const loadProductsFromVendor = async() => {
    loading.value = true;
    const session = localStorage.getItem('user_session');
    if(!session) {
        alert("Пожалуйста войдите в аккаунт");
        return;
    }
    const user = JSON.parse(session);
    const vendorId = user.id;
    
    try {
        const response = await fetch(`http://localhost:8080/api/products/vendor/${vendorId}`);
        if(response.ok) {
            product.value = await response.json();
        }
    } catch (error) {
        console.error('Ошибка связи с сервером Java:', error);
    } finally {
        loading.value = false;
    }
}
const closeEditor = () => {
    editingProductId.value = null;
    loadProductsFromVendor();
}

onMounted(() => {
    loadProductsFromVendor();
});
</script>

<template>
    <div class="warehouse_container">
        <VendorEditProduct 
            v-if="editingProductId" 
            :productId="editingProductId" 
            @close="closeEditor" 
        />

        <div v-else>
            <div class="warehouse_header">
                 <h1>Товары на складе</h1>
                 <button class="btn_add">+ Добавить товар</button>
            </div>
            
            <div v-if="loading" class="loading_state">
                Загрузка товаров...
            </div>
            
            <div v-else class="product_grid">
                <div v-for="item in product" :key="item.id" class="product_card">
                    <div class="card_img_wrap">
                        <img :src="'/' + (item.details?.mainImage || '')" :alt="item.model">
                    </div>
                    <div class="card_content">
                        <p class="card_brand">{{ item.brand }}</p>
                        <p class="card_model">{{ item.model }}</p>
                        <div class="card_stats">
                            <p class="card_price">{{ item.price }} ₽</p>
                            <p class="card_stock">Остаток: {{ item.quantity }} шт.</p>
                        </div>
                        <button @click="editingProductId = item.id" class="btn_edit">Изменить</button>
                    </div>
                </div>
            </div>
        </div>
    </div>   
</template>

<style scoped>
.warehouse_container {
    padding: 40px;
}
.warehouse_header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 40px;
}
.warehouse_header h1 {
    margin: 0;
    font-size: 28px;
    font-weight: 700;
    color: #222;
}
.btn_add {
    background-color: #ff7e5f;
    color: #ffffff;
    border: none;
    padding: 12px 24px;
    font-size: 16px;
    font-weight: 600;
    border-radius: 30px;
    box-shadow: 0 4px 12px rgba(255, 126, 95, 0.3);
    cursor: pointer;
    transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.btn_add:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(255, 126, 95, 0.4);
}
.loading_state {
    text-align: center;
    font-size: 18px;
    padding: 50px;
    color: #888;
}
.product_grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 40px;
}
.product_card {
    display: flex;
    flex-direction: column;
    overflow: hidden;
    background-color: #ffffff;
    border-radius: 16px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.06);
    transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.product_card:hover {
    transform: translateY(-5px);
    box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);
}
.card_content {
    padding: 20px;
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    gap: 8px;
}
.card_content p {
    margin: 0;
}
.card_brand {
    font-size: 13px;
    color: #888;
    text-transform: uppercase;
}
.card_model {
    font-size: 16px;
    font-weight: 600;
    color: #222;
}
.card_stats {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    margin-top: auto;
    margin-bottom: 15px;
}
.card_price {
    font-size: 16px;
    font-weight: 700;
    color: #ff7e5f;
}
.card_stock {
    font-size: 13px;
    color: #555;
}
.btn_edit {
    width: 100%;
    padding: 10px 0;
    background-color: #f9f9f9;
    color: #333;
    border: 1px solid #eaeaea;
    border-radius: 8px;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s ease;
}
.btn_edit:hover {
    background-color: #ff7e5f;
    color: #ffffff;
    border-color: #ff7e5f;
}
.card_img_wrap {
    height: 180px;
    background-color: #f5f5f5;
}
.card_img_wrap img {
    width: 100%;
    height: 100%;
    object-fit: contain;
    padding: 10px;
    box-sizing: border-box;
}
</style>