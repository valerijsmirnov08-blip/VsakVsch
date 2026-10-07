<script setup>
    import{ref, onMounted} from 'vue'
    import { RouterLink } from 'vue-router'

    const products = ref([])
    const loading = ref(true)

    const loadProductsFromJava = async () =>
{
    try
    {
        const response = await fetch('http://localhost:8080/api/products')
        if(response.ok)
        {
            products.value = await response.json() 
        }    
    } catch (error) 
    {
        console.error('Ошибка связи с сервером Java:', error)
    }
    finally
    {
        loading.value = false
    }
}
const addToCartFromCatalog = async (productId) =>
{
    const session = localStorage.getItem('user_session')
    if(!session)
    {
        alert("Пожалуйста войдите в аккаунт");
        return;
    }
    try
    {
        const user = JSON.parse(session);
         const response = await fetch('http://localhost:8080/api/cart/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId: Number(user.id || user.userId), 
                productId: Number(productId),        
                quantity: 1
            })
        })
        if(response.ok)
        {
            alert('Товар добавлен в корзину');
        }
        else
        {
            console.error("Ошибка добавления товара в корзину попробуйте позже")
        }
    }
    catch(error)
    {
        console.error(error)
    }
    
}
    onMounted(() =>
    {
        loadProductsFromJava()
    })
</script>
<template>
     <div class="page-wrapper">
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
    <main class="catalog-container">
      <h2 class="catalog-title">Наши товары</h2>
      <div v-if="loading" class="loading">Загрузка товаров из базы данных...</div>
      <div v-else class="products-grid">
        <div v-for="product in products" :key="product.id" class="product-card">
            <RouterLink :to="`/product/${product.id}`" class="product-details-link">
                <div class="image-wrapper">
                    <img :src="product.mainImage" :alt="product.model" />
                </div>
                <div class="product-info">
                    <span class="brand">{{ product.brand }}</span>
                    <h3 class="model">{{ product.model }}</h3>
            
                    <div class="meta-row">
                        <span class="rating">⭐ {{ product.rating }}</span>
                        <span class="price">{{ product.price }} ₽</span>
                    </div>
                </div>
            </RouterLink>
          
          <div class="card-actions">
            <button @click="addToCartFromCatalog(product.id)" class="btn-cart">В корзину</button>
            <RouterLink :to="`/product/${product.id}`" class="btn-more">
              Подробнее
            </RouterLink>
          </div>

        </div>
      </div>
    </main>
  </div>

</template>