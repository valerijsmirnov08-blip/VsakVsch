<script setup>
    import{ref, onMounted} from 'vue'
    import { RouterLink } from 'vue-router'
    import { Heart } from 'lucide-vue-next';
       
    const favoriteIds = ref([]);

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
const toggleFavorite = async (productId) =>
    {
      const session = localStorage.getItem('user_session');
    if (!session) {
        alert("Пожалуйста, войдите в аккаунт");
        return;
    }
    const user = JSON.parse(session);

    if (favoriteIds.value.includes(productId)) {
        favoriteIds.value = favoriteIds.value.filter(id => id !== productId);
    } else {
        favoriteIds.value.push(productId);
    }
    try {
        await fetch('http://localhost:8080/api/favorites/toggle', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                userId: Number(user.id || user.userId),
                productId: Number(productId)
            })
        });
    } catch (error) {
        console.error("Ошибка сети при обновлении избранного:", error);
    }
        
    };
      const loadFavorites = async () => {
      const session = localStorage.getItem('user_session');
      if (!session) return;

      const user = JSON.parse(session);
      const currentUserId = user.id || user.userId;

      try {
          const response = await fetch(`http://localhost:8080/api/favorites/user/${currentUserId}`);
          if (response.ok) {
              const favoriteItems = await response.json();
              favoriteIds.value = favoriteItems.map(item => item.product ? item.product.id : item.id);
          }
      } catch (error) {
          console.error('Ошибка загрузки избранного:', error);
      }
  };
    onMounted(() =>
    {
        loadProductsFromJava();
        loadFavorites();
    })
</script>
<template>
     <div class="page-wrapper">
    <main class="catalog-container">
      <h2 class="catalog-title">Наши товары</h2>
      <div v-if="loading" class="loading">Загрузка товаров из базы данных...</div>
      <div v-else class="products-grid">
        <div v-for="product in products" :key="product.id" class="product-card">
            <RouterLink :to="`/product/${product.id}`" class="product-details-link">
               <div class="fav_btn">
                    <el-button class="favorite_btn" text circle @click.prevent="toggleFavorite(product.id)">
                        <el-icon size="30px">
                            <Heart 
                                :color="favoriteIds.includes(product.id) ? '#ff7e5f' : '#3a3a3a'" 
                                :fill="favoriteIds.includes(product.id) ? '#ff7e5f' : 'none'" 
                            />
                        </el-icon>
                    </el-button>
                </div> 
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

<style scoped>
.catalog-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: sans-serif;
}
.product-details-link
{
    text-decoration: none;
    color: inherit;
    display: block;
}
.catalog-title {
  margin-bottom: 20px;
  color: #333;
}

.loading {
  text-align: center;
  font-size: 1.2rem;
  color: #666;
  margin-top: 50px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 25px;
}

.product-card {
  background: #ffffff;
  border: 1px solid #E0E0E0;
  border-radius: 12px;
  position: relative;
  padding: 16px;
  display: flex;
  flex-direction: column;
  transition: transform 0.2s, box-shadow 0.2s;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.08);
}
.favorite_btn
{
    width: 100%;
    display: flex;
    justify-content: flex-end;
    position: absolute;
    top: 15px;
    right: 15px;
   
}
.image-wrapper {
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12px;
}

.image-wrapper img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.brand {
  font-size: 0.8rem;
  text-transform: uppercase;
  color: #888;
}

.model {
  font-size: 1rem;
  margin: 4px 0 12px 0;
  color: #222;
  height: 40px;
  overflow: hidden;
}

.meta-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.price {
  font-weight: bold;
  font-size: 1.1rem;
  color: #333;
}

.rating {
  font-size: 0.9rem;
  color: #555;
}

.card-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: auto;
}

.btn-cart {
  background-color: #FF9466; 
  color: white;
  border: none;
  padding: 10px;
  border-radius: 20px;
  font-weight: bold;
  cursor: pointer;
  width: 100%;
}

.btn-cart:hover {
  background-color: #ff814d;
}

.btn-more {
  text-align: center;
  text-decoration: none;
  color: #FF5B46; 
  font-size: 0.9rem;
  font-weight: bold;
  padding: 4px;
}

</style>