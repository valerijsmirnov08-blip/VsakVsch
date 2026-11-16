<script setup>
import { ref, onMounted } from 'vue';
import { useVendorProducts } from '@/composables/useVendorProducts';
import VendorEditProduct from '@/components/VendorEditProduct.vue';
import VendorCreateProduct from '@/components/VendorCreateProduct.vue';

const { products, loading, fetchVendorProducts } = useVendorProducts();
const editingProductId = ref(null);
const isCreating = ref(false);

const closeSubView = async () => {
    editingProductId.value = null;
    isCreating.value = false;
    await fetchVendorProducts();
};

onMounted(fetchVendorProducts);
</script>

<template>
    <div class="warehouse_container">
        <VendorEditProduct 
            v-if="editingProductId" 
            :productId="editingProductId" 
            @close="closeSubView" 
        />

        <VendorCreateProduct 
            v-else-if="isCreating" 
            @close="closeSubView" 
        />

        <div v-else>
            <div class="warehouse_header">
                 <h1>Товары на складе</h1>
                 <button @click="isCreating = true" class="btn_add">+ Добавить товар</button>
            </div>
            
            <div v-if="loading" class="loading_state">
                Загрузка товаров...
            </div>
            
            <div v-else class="product_grid">
                <div v-for="item in products" :key="item.id" class="product_card">
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