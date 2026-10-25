
 <script setup>
    import { ref, onMounted, computed } from 'vue';
    import { useRoute, useRouter } from 'vue-router';
    import { Heart } from 'lucide-vue-next';
    
    const isFavorite = ref(false);

    const route = useRoute();
    const router = useRouter();

    const product = ref(null);
    const loading = ref(true);
    const errorMessege = ref('');

    const activeImage = ref('');
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
            const session = localStorage.getItem('user_session');
            if(session)
            {
                const user = JSON.parse(session);
                const favResponse = await fetch(`http://localhost:8080/api/favorites/check/${user.id || user.userId}/${productId}`);
                if(favResponse.ok)
                {
                    isFavorite.value = (await favResponse.text() === 'true');
                }
                else
                {
                    console.error("Сервер вернул ошибку при обновлении избранного");
                }
            }
        } catch (error)
        {
            errorMessege.value = error.message;
        }finally
        {
            loading.value = false;
        }
    }) 
    const parsedGallery = computed(() =>
    {
        if(!product.value) return[];
        if(typeof product.value.gallery === 'string' && product.value.gallery.trim() !== '')
        {
            return product.value.gallery.split(',').map(url => url.trim());
        }
        return[product.value.mainImage];
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
    const toggleFavorite = async () =>
    {
        const session = localStorage.getItem('user_session');
        if(!session)
        {
            alert("Пожалуйста зарегистрируйтесь");
        }
        const user = JSON.parse(session);
        try
        {
            const response = await fetch('http://localhost:8080/api/favorites/toggle',{
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
                const favResponse = await fetch(`http://localhost:8080/api/favorites/check/${Number(user.id || user.userId)}/${productId}`);
                isFavorite.value = (await favResponse.text()) === 'true';
            } else
            {
                console.error("Сервер вернул ошибку при обновлении избранного");
            }
        }
        catch(error)
        {
            console.error("Ошибка связи с сервером:", error);
        }
        
    };
    </script>
<template>
    <button @click="router.push('/')" class="back"><img src="/Назад.png" alt="Назад"></button>
    <div v-if="loading" class="status_center_msg">Загрузка товара...</div>
    <div v-else-if="errorMessege" class="status_center_msg error_msg">{{ errorMessege }}</div>
    <div v-else-if="product" class="product">
       
       
        <div class="mini_photo">
            <ul class="container_mini_img">
                <li v-for="(imgUrl, index) in galleryImages" :key="index">
                    <img
                    class="mini_img"
                    :src="imgUrl"
                    :class="{active_preview: activeImage === imgUrl}"
                    alt="Превью"
                    @click="activeImage = imgUrl">
                </li>    
            </ul>
        </div>
        
        <div class="big_photo">
            <img id="main_photo" :src="activeImage" alt="Большое фото">
        </div>
        
        <div class="description">
            <div class="name_product">
                <h2>{{ product.brand }} {{ product.model }}</h2>
            </div>
           <div class="description_diller">
            <p>Артикул</p> <p class="answer">{{ product.vendorSku }}</p>
            <p>Модель</p> <p class="answer">{{ product.model }}</p>
            <p>Срок гарантии</p> <p class="answer">{{ product.warranty || "Не указано" }}</p>
            <p>Бренд</p> <p class="answer">{{ product.brand }}</p>
            <details class="product_description">
                <summary>Описание</summary>
                <div class="description_content">
                    <p>{{ product.description || "Описание отсутствует"}}</p>
                </div>
            </details>
        </div>
            
        </div>
        <div class="pay">
            <el-button class="favorite_btn" text circle @click="toggleFavorite">
                <el-icon size="30px">
                    <Heart :color="isFavorite ? '#ff7e5f' : '#3a3a3a'" :fill="isFavorite ? '#ff7e5f' : 'none'" />
                </el-icon>
            </el-button>
            <p class="prise">{{ product.price }} ₽</p>
            <button @click="addToCartFromCatalog(product.id)" class="in_basket">В корзину</button>
            <button class="buy_now">Купить сейчас</button>
            <p class="when">{{ product.estimatedDelivery || 'В течение недели'}}</p>
            <p class="rating">{{ product.rating || '0.0'}}</p>
        </div>
    </div>
</template>

<style scoped>
.product
{
    position: relative;
    margin-top: 150px;
    display: flex;
    gap: 100px;
}
.favorite_btn
{
    align-self: flex-end;
    margin-bottom: -15px;
   
}

.mini_photo ul
{
    
    display: flex;
    flex-direction: column;
    gap: 30px;
    list-style: none;
    padding: 0;
    margin: 0;
    margin-left: 30px;
}
.mini_photo ul li
{
    max-width: 100px;
    max-height: 100px;
    width: 100px;
    height: 100px;
    overflow: hidden;
}
.mini_photo ul li img
{
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
}
.mini_photo ul li button
{
    border: none;
    cursor: pointer;
}
.big_photo
{
    width: 200px;
    height: 400px;
    overflow: hidden;
    display: flex;
    justify-content: center;
    align-items: center;
    
}
.big_photo img
{
    width: 100%;
    height: 100%;
    
    object-fit: contain;
    display: block;
}
.description
{
    display: flex;
    flex-direction: column;
    gap: 20px;
}
.description_diller
{
    display: grid;
    grid-template-columns: max-content 1fr;
    gap: 15px 30px;
    align-items: center;
    max-width: 400px;
}
.answer
{
    color: #3a3a3a;
    font-weight: bold;
}
.product_description
{
    margin-top: 25px;
    max-width: 450px;
    font-family: 'Lucida Sans';
    border-bottom: 1px solid #eaeaea;
    padding-bottom: 10px;
}
.product_description summary
{
    font-size: 18px;
    font-weight: 700;
    color: #000000;
    cursor: pointer;
    list-style: none;
    display: flex;
    justify-content: space-between;
    align-items: center;
    user-select: none;
}
.description_content
{
    margin-top: 12px;
    font-size: 15px;
    line-height: 1.6;
    color: #3a3a3a;
    animation: In 0.3s ease-out;
}
@keyframes In
{
    from {opacity: 0; transform: translateY(-5px);}
    to {opacity: 1; transform: translateY(0);}
}

.pay
{
    display: flex;
    flex-direction: column;
    align-items: center;
}
.prise
{
    font-size: 40px;
    text-align: center;
}
.in_basket
{
    background-color: #ff7e5f;
    border-radius: 40px;
    border:none;
    width: 300px;
    height: 60px;
    font-size: 20px;
    font-weight: 700;
    color: #ffffff;
    margin-bottom: 20px;
    cursor: pointer;
}
.buy_now
{
    background-color: #ffff;
    border-radius: 40px;
    border:none;
    width: 300px;
    height: 60px;
    font-size: 20px;
    font-weight: 700;
    color: #ff7e5f;
    cursor: pointer;
}
.when
{
    align-self: flex-start;
    margin-left: 0;
    margin-top: 15px;
}
.rating
{
    align-self: flex-start;
    margin-left: 0;
    margin-top: 5px;
}
.rating::after
{
    content: "★";
    color: #f3ef03;
    margin-right: 4px;
    font-size: 18px;
}
</style>
