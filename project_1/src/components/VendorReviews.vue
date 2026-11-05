<script setup>
    import {computed, onMounted, ref} from 'vue';

    const filter = ref('Все');
    const product = ref([]);
    const review = ref([]);
    const activeMenu = ref(null);

    const toggleMenu = (id) =>
    {
        activeMenu.value = activeMenu.value === id ? null : id; 
    }

    const changeStatus = async(item) => 
    {
        const response = await fetch(`http://localhost:8080/api/reviews/`)
        console.log("Меняем статус для отзыва с ID:", item.id);
    
        activeMenu.value = null;
    }


    const loadReviews = async () => {
    try {
        const session = localStorage.getItem('user_session');
        const user = JSON.parse(session);
        const vendorId = user.id;
        
        const response = await fetch(`http://localhost:8080/api/reviews/vendor/${vendorId}`);
        if (response.ok) {
            review.value = await response.json();
            console.log("Загруженные отзывы:", review.value);
        } else {
            const errorText = await response.text();
            console.error("Текст ошибки 500 от Java:", errorText);
        }
    } catch (error) {
        console.error("Ошибка загрузки отзывов:", error);
    }
}
    

    const formattedReviews = computed(() =>
    {
            let filtered = review.value;
        
        if (filter.value === 'Просмотренные') {
            filtered = filtered.filter(r => r.isRead === true);
        } else if (filter.value === 'Не просмотренные' || filter.value === 'Новые') {
            filtered = filtered.filter(r => r.isRead === false);
        }

        return filtered.map(r => ({
            id: r.id,
            nameProduct: 'Товар ID: ' + r.productId,
            status: "Получен", 
            review: r.comment,
            statusReview: r.isRead ? 'Просмотрено' : 'Не просмотрено',
            rating: r.rating,
            foto: r.photoUrls && r.photoUrls.length > 0 ? 'Есть фото' : 'Нет фото'
        }));
    });
    onMounted(() =>
    {
        loadReviews();
    })
</script>
<template>
    <div class="reviews_grid">
        
        <div class="date_reviews_for_vendor">
            <h2>Отзывы покупателей</h2>
            <div class="for_vendor">
                
                <nav>
                    <button @click="filter = 'Все'" :class="{ active: filter === 'Все' }">Все</button>
                    <button @click="filter = 'Новые'" :class="{ active: filter === 'Новые' }">Новые</button>
                    <button @click="filter = 'Просмотренные'" :class="{ active: filter === 'Просмотренные' }">Просмотренные</button>
                    <button @click="filter = 'Не просмотренные'" :class="{ active: filter === 'Не просмотренные' }">Не просмотренные</button>
                </nav>
            </div>
        </div>
        <div class="search_product">
            <input type="text" placeholder="Название, артикул">
        </div>
        <table class="reviews_table">
        <thead>
            <tr>
                <th>Название товара</th>
                <th>Статус товара</th>
                <th>Отзыв</th>
                <th>Статус отзыва</th>
                <th>Оценка</th>
                <th>Фото</th>
                <th>Действия</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="item in formattedReviews" :key="item.id">
                <td>{{ item.nameProduct }}</td>
                <td >{{ item.status }} <img style="display: none;" :class="{active: filter === ''}" src="/Галочка_получен.png" alt="Получен"><img style="display: none;" :class="{active: filter === ''}" src="/Крестик_не_получен.png" alt="Не получен"></td>
                <td>{{ item.review }}</td>
                <td>{{ item.statusReview }}</td>
                <td>{{ item.rating }}</td>
                <td>{{ item.foto }}</td>
                <td class="action_cell">
                    <button class="dots_btn" @click="toggleMenu(item.id)">⋮</button>
                    
                    <div v-if="activeMenu === item.id" class="dropdown_menu">
                        <button>Сменить статус</button>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
    </div>
    
</template>

<style scoped>
.reviews_grid
{
    margin: 30px;
}
.for_vendor nav
{
    display: flex;
    justify-content: left;
    margin: 20px;
    align-items: center;
    gap: 30px;
    margin-left: 0;
    margin-bottom: 25px;
}
.for_vendor nav .active
{
    border-bottom: 3px solid #FF5B46;
    font-weight: bold;
}
.for_vendor nav button
{
    width: 135px;
    height: 20px;
    border: none;
    cursor: pointer;
    font-size: 14px;
}
.search_product
{
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
.reviews_table
{
    width: 100%;
    border-collapse: collapse;
}
.reviews_table th
{
    text-align: left;
    color: #363636;
    padding: 15px 10px;
    border-bottom: 1px solid #E0E0E0;
}
.reviews_table td
{
    padding: 10px 10px;
    border-bottom: 1px solid #E0E0E0;
}
.reviews_table td img .active
{
    display: contents;
    width: 20px;
    /* display: flex;
    flex-direction: column; */
    align-self: center;
}
.action_cell {
    position: relative; 
    text-align: center;
}

.dots_btn {
    background: none;
    border: none;
    font-size: 20px;
    font-weight: bold;
    cursor: pointer;
    color: #363636;
    padding: 0 10px;
}

/* 2. Отрываем меню от таблицы и поднимаем на верхний слой */
.dropdown_menu {
    position: absolute;
    top: 100%; /* Появляется ровно под кнопкой */
    right: 0; /* Выравнивается по правому краю ячейки */
    background: white;
    border: 1px solid #E0E0E0;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15); /* Тень для эффекта "над таблицей" */
    z-index: 9999; /* Гарантированно перекрывает все остальные строки */
    min-width: 160px;
    padding: 5px 0;
    display: flex;
    flex-direction: column;
}

.dropdown_menu button {
    background: none;
    border: none;
    padding: 10px 15px;
    text-align: left;
    cursor: pointer;
    font-size: 14px;
    color: #333;
    width: 100%;
}

.dropdown_menu button:hover {
    background-color: #f5f5f5;
}
</style>