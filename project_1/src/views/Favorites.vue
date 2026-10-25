<script setup>
    import { ref } from 'vue';
    import router from '@/router';
    import { onMounted } from 'vue';
    import { Heart } from 'lucide-vue-next';
       
    const pendingRemovals = ref([]);
    const removalTimers = {};

    const product = ref([]);
    const loading = ref(true);

    const ErrorMessege = ref('');
    const SuccesMessege = ref('');

    const loadProductsFromFavorites = async() =>
    {
        const session = localStorage.getItem('user_session');
        if(!session)
        {
            alert("Пожалуйста зарегистрируйтесь");
            router.push('/enter_accaunt');
            return;
        }
        const user = JSON.parse(session);
        const currentUserId = user.id || user.userId;
        try
        {
            const response = await fetch(`http://localhost:8080/api/favorites/user/${currentUserId}`)
            if(response.ok)
            {
                product.value = await response.json();
                console.log(product);
            }
            else
            {
                ErrorMessege.value = "Не удалось загрузить избранное";
            }
        }
        catch(error)
        {
            ErrorMessege.value = "Ошибка сети";
            console.error(error);
    } finally {
        loading.value = false;
    }
}
const toggleFavorite = async (productId) =>
    {
       if(pendingRemovals.value.includes(productId))
       {
            pendingRemovals.value = pendingRemovals.value.filter(id => id !== productId);
            clearTimeout(removalTimers[productId]);
            delete removalTimers[productId];
            console.log(`Отменили удаление товара ${productId}`);
            return;
       }
       pendingRemovals.value.push(productId);
       console.log(`Товар ${productId} ждет удаления...`);

       removalTimers[productId] = setTimeout(async () =>
    {
        const session = localStorage.getItem('user_session');
        if(!session)
        {
            return;
        }
        const user = JSON.parse(session);
        try
        {
            const response = await fetch('http://localhost:8080/api/favorites/toggle',
            {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify
                ({
                    userId: Number(user.id || user.userId),
                    productId: Number(productId)
                })
            })
            if(response.ok)
            {
                product.value = product.value.filter(item => item.id !== productId);
            }
            else
            {
                console.error("Ошибка на сервере при удалении");
            }
        }
        catch(error)
        {
            console.error("Ошибка сети:", error);
        }
        finally
        {
            pendingRemovals.value = pendingRemovals.value.filter(id => id !== productId);
            delete removalTimers[productId];
        }
        
    }, 6000)
        
    };

    onMounted(() =>
    {
        loadProductsFromFavorites();
    }
    )
    
</script>

<template>
    <RouterLink to="/accaunt" class="link">
            <button class="back"><img src="/Назад.png" alt="Назад"></button>
    </RouterLink>
        <div v-if="loading">Загрузка...</div>
        <div v-else-if="ErrorMessege">{{ ErrorMessege }}</div>
        <div v-else class="favorites_grid">
            <div class="favorites_card" v-for="item in product" :key="item.id" :style="{ opacity: pendingRemovals.includes(item.id) ? 0.5 : 1, transition: 'opacity 0.3s' }">
               
                <div class="fav_btn">
                    <el-button class="favorite_btn" text circle @click="toggleFavorite(item.id)">
                        <el-icon size="30px">
                            <Heart 
                                :color="pendingRemovals.includes(item.id) ? '#3a3a3a' : '#ff7e5f'" 
                                :fill="pendingRemovals.includes(item.id) ? 'none' : '#ff7e5f'" 
                            />
                        </el-icon>
                    </el-button>
                </div>
                <div class="img_wrapper"><img :src="item.mainImage" alt=""></div>
                <div class="favorite_info">
                    <p class="brand">{{ item.brand }}</p>
                    <p class="model">{{ item.model }}</p>
                    <div class="favorite_post_info">
                        <p class="ratyng">⭐ {{ item.rating }}</p>
                        <p class="quantity">Кол-во: {{ item.quantity }}</p>
                    </div>
                    
                </div>
                <button class="more_detales" @click="router.push(`/product/${item.id}`)">Подробнее</button>
            </div>
        </div>
        
</template>

<style scoped>
.favorites_grid
{
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
    gap: 18px;
    padding: 40px;

}
.favorites_card
{
    display: flex;
    flex-direction: column;
    position: relative;
    min-height: 360px;
    box-sizing: border-box;
    border: 2px solid #000;
    border-radius: 20px;
    padding: 20px;
    padding-top: 40px;
    margin: 20px;
    gap:10px;
   
}
.img_wrapper
{
    height: 180px;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 10px;
}
.img_wrapper img
{
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
}
.favorite_info
{
    display: flex;
    flex-direction: column;
    gap: 6px;
    margin-bottom: 15px;
}
.favorite_info p
{
    margin: 0;
}
.brand
{
    font-size: 12px;
    color: #888;
    text-transform: uppercase;
}
.model
{
    font-size: 16px;
    font-weight: bold;
    color: #222;
    height: 40px;
    overflow: hidden;
}
.favorite_post_info
{
    display: flex;
    justify-content: space-between;
    margin-top: 8px;
    font-size: 14px;
    color: #555;
}
.more_detales
{
    background-color: #FF9466; 
    color: white;
    border: none;
    padding: 10px;
    border-radius: 20px;
    font-weight: bold;
    cursor: pointer;
    width: 100%;
    margin-top: auto;
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

</style>