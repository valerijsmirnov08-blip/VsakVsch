<script setup>
    import {ref, onMounted } from 'vue';
    import { RouterLink } from 'vue-router';

    const searchtext = ref('');
    const categoryId = ref(null);
    const category = ref([]);
    const searchResult = ref([]);
    const isModalOpen = ref(false);

    const openModal = () =>
    {
        isModalOpen.value = true;
    };

    const closeModal = () =>
    {
        isModalOpen.value = false;
    };


    const loadCategories = async () =>
    {
        const response = await fetch(`http://localhost:8080/api/category/roots`)
        if(response.ok)
        {
           category.value = await response.json(); 
        }
    }
    const performSearch = async () =>
    {
        if(searchtext.value.trim().length >= 2)
        {
            let url = `http://localhost:8080/api/products/search?searchText=${searchtext.value}`;
            if(categoryId.value !== null)
            {
                url += `&categoryId=${categoryId.value}`;
            }
            const response = await fetch(url);
            searchResult.value = await response.json();
        }else
        {
            searchResult.value = [];
        }
    } 
    onMounted(() =>
    {
        loadCategories();
    })
</script>

<template>

    <div class="search-wrapper">
        <div v-if="isModalOpen" class="overlay" @click="closeModal"></div>

        <input @focus="openModal" v-model="searchtext" @input="performSearch" class="search-input" type="text" placeholder="Поиск">

        <div v-if="isModalOpen" class="dropdown-panel">
            
            <div class="dropdown-sidebar">
                <h3 class="dropdown-title">Категории</h3>
                <ul class="category-list">
                    <li @click="categoryId = null; performSearch()" :class="{ active: categoryId === null }">
                        Все категории
                    </li>
                    <li 
                        v-for="item in category" 
                        :key="item.id"
                        @click="categoryId = item.id; performSearch()"
                        :class="{ active: categoryId === item.id }"
                    >
                        {{ item.name }}
                    </li>
                </ul>
               
            </div>
             
            <div class="dropdown-content">
    <h3 class="dropdown-title">Результат поиска</h3>
    
            <ul v-if="searchResult.length > 0" class="result-list">
                <li v-for="product in searchResult" :key="product.id" class="result-item">
                    <RouterLink :to="`/product/${product.id}`" class="result-link" @click="closeModal">
                        <img :src="product.mainImage" :alt="product.model" class="result-image" />
                        <div class="result-info">
                            <span class="result-brand">{{ product.brand }}</span>
                            <span class="result-model">{{ product.model }}</span>
                        </div>
                        <div class="result-price">{{ product.price }} ₽</div>
                        
                    </RouterLink>
                </li>
            </ul>
            
            <div v-else class="empty-state">
                Начните вводить название товара...
            </div>
        </div>

        </div>
    </div>
</template>
<style scoped>
.search-wrapper {
    position: relative;
}

.overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.4);
    z-index: 1000;
}

.search-input {
    position: relative;
    z-index: 1001;
    width: 500px;
    height: 30px;
    padding: 14px 25px;
    border-radius: 30px;
    border: none;
    background-color: #f0f0f0;
    font-size: 18px;
    outline: none;
}

.dropdown-panel {
    position: absolute;
    top: 50px;
    left: 0;
    width: 700px;
    min-height: 350px;
    background-color: #ffffff;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.15);
    display: flex;
    z-index: 1001;
    overflow: hidden;
    color: #333;
    width: 850px;
    min-height: 400px;
    left: 50%;
    transform: translateX(-50%);
}

.dropdown-sidebar {
    width: 35%;
    background-color: #f9f9f9;
    padding: 20px;
    border-right: 1px solid #eaeaea;
}

.dropdown-content {
    width: 65%;
    padding: 20px;
}

.dropdown-title {
    margin-top: 0;
    margin-bottom: 15px;
    font-size: 18px;
    color: #555;
}

.category-list, .result-list {
    display: flex;
    flex-direction: column;
    gap: 5px;
    list-style: none;
    padding: 0;
    margin: 0;
    list-style: none;
    padding: 0;
    margin: 0;
    max-height: 300px;
    overflow-y: auto;
}

.category-list li {
    padding: 10px 20px;
    cursor: pointer;
    border-radius: 50px;
    transition: background-color 0.2s ease;
    font-family: sans-serif;
    font-size: 16px;
    text-align: left;
    transform: none;    
}

.category-list li:hover, .category-list li.active {
    background-color: #d1d1d1;
}
.result-list::-webkit-scrollbar {
    width: 6px;
}
.result-list::-webkit-scrollbar-thumb {
    background-color: #ccc;
    border-radius: 10px;
}
.result-item:hover {
    background-color: #f4f4f4;
    border: 1px solid #e0e0e0;
}
.result-link {
    display: flex;
    align-items: center;
    padding: 15px 20px;
    text-decoration: none;
    color: inherit;
    gap: 20px;
}
.result-info
{
    display: flex;
    gap: 10px;
}
.result-image {
    width: 70px;
    height: 70px; 
    object-fit: contain;
    border-radius: 8px;
    background-color: #fff;
    flex-shrink: 0;
}

.result-brand {
    font-size: 14px;
    color: #888;
    text-transform: uppercase;
    margin-bottom: 4px;
}

.result-model {
    font-size: 18px;
    font-weight: 600;
    color: #222;
    line-height: 1.2;
}

.result-price {
    font-weight: bold;
    color: #FF5B46; 
    font-size: 22px;
    white-space: nowrap;
}
.product-brand {
    font-weight: bold;
    color: #333;
    margin-right: 6px;
}

.product-model {
    color: #666;
}

.product-price {
    font-weight: bold;
    color: #e53935;
    white-space: nowrap;
}
.result-list li {
    padding: 10px 0;
    border-bottom: 1px solid #eaeaea;
}

.empty-state {
    color: #888;
    font-size: 14px;
}
</style>