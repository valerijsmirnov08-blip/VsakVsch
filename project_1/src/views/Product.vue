
 <script setup>
    import { ref, onMounted, computed } from 'vue';
    import { useRoute, useRouter } from 'vue-router';

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
    <button @click="router.push('/')" class="back"><img src="/Назад.png" alt="Назад"></button>
    <div v-if="loading" class="status_center_msg">Загрузка товара...</div>
    <div v-else-if="errorMessege" class="status_center_msg error_msg">{{ errorMessege }}</div>
    <div v-else-if="product" class="product">
       
        <div class="mini_photo">
            <ul class="container_mini_img">
                <li v-for="(imgUrl, index) in parsedGallery" :key="index">
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
            <p class="prise">{{ product.price }} ₽</p>
            <button @click="addToCartFromCatalog(product.id)" class="in_basket">В корзину</button>
            <button class="buy_now">Купить сейчас</button>
            <p class="when">{{ product.estimatedDelivery || 'В течение недели'}}</p>
            <p class="rating">{{ product.rating || '0.0'}}</p>
        </div>
    </div>
    <div class="bottom">
        <div class="column_bottom">
            <p class="column_title">Для покупателей</p>
            <a href="">Частые вопросы</a>
            <a href="">Доставка</a>
            <a href="">Страховка товара</a>
            <a href=""><img src="/Вацап_ЧБ.png" alt=""></a>
        </div>
        <div class="column_bottom">
            <p class="column_title">Для продавцов</p>
            <a href="">Продажа товара</a>
            <a href="">Открытие своего пунтка выдачи</a>
            <a href="">Доставка заказов</a>
            <a href=""><img src="/ТГ_Чб.png" alt=""></a>
        </div>
        <div class="column_bottom">
            <p class="column_title">Наша компания</p>
            <a href="">О нас</a>
            <a href="">Контакты</a>
            <a href="">Поддержка</a>
            <a href=""><img src="/Макс_ЧБ.png" alt=""></a>
        </div>
    </div>
</template>
