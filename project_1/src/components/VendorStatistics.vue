<script setup>
import { ref } from 'vue';
import { Bar } from 'vue-chartjs';
import { useRoute } from 'vue-router';
import { onMounted } from 'vue';
import {
    Chart as ChartJS,
    Title,
    Tooltip,
    Legend,
    BarElement,
    CategoryScale,
    LinearScale,
    LineElement,
    PointElement,
    LineController,
    BarController,
    scales} from 'chart.js';
    ChartJS.register
    (
        Title, Tooltip, Legend, BarElement, CategoryScale, LineElement, LinearScale,LineController, PointElement, BarController  
    )

    const chartData = ref({
    labels: ['Январь', 'Февраль', 'Март', 'Апрель'],
    datasets: [
        {
            type: 'line',
            label: 'Отзывы',
            data: [10, 25, 15, 35],
            borderColor: '#333333',
            borderWidth: 3,
            fill: false,
            tension: 0.4,
            pointRadius: 5,
            pointBackgroundColor: '#ffffff',
            pointBorderColor: '#333333',
            pointBorderWidth: 2
        },
        {
            type: 'bar',
            label: 'Продажи',
            data: [12, 45, 23, 60],
            backgroundColor: '#ff7e5f',
            borderRadius: 4,
            barPercentage: 0.6
        }
    ]
});
const chartOptinos = ref
({
    scales:
    {
        x:
        {
            grid:
            {
                display:false
            }
        }
    }
})
const fakeBakend =
    {
        'Неделя': {labels: ['Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб', 'Вс'], data: [12, 19, 3, 45, 12, 78, 1]},
        '2 недели': {labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14'], data: [12, 18, 15, 25, 22, 30, 28, 14, 20, 19, 35, 32, 40, 38]},
        'Месяц':{labels: ['Неделя 1','Неделя 2','Неделя 3','Неделя 4'], data: [12, 19, 3, 45]},
        'Год': {labels: ['Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл', 'Авг', 'Сен', 'Окт', 'Ноя', 'Дек'], data: [113, 142, 1222, 418, 1, 1132, 122, 13, 122, 1333, 128, 1]}
    }

const updateChartData = (labels, reviews, sales, brandName) => {
    chartData.value = {
        labels: labels,
        datasets: [
            {
                type: 'line',
                label: 'Отзывы',
                data: reviews,
                borderColor: '#333333', 
                borderWidth: 3,
                fill: false,
                tension: 0.4,
                pointRadius: 5,
                pointBackgroundColor: '#ffffff',
                pointBorderColor: '#333333',
                pointBorderWidth: 2
            },
            {
                type: 'bar',
                label: `Продажи: ${brandName}`,
                data: sales,
                backgroundColor: '#ff7e5f', 
                borderRadius: 4,
                barPercentage: 0.6
            }
        ]
    };
};
    const product = ref([]);
    const loadProductsFromVendor = async() =>
{
    const session = localStorage.getItem('user_session');
    if(!session)
    {
        alert("Пожалуйста войдите в аккаунт");
        return;
        
    }
    const vendorId = JSON.parse(session);
    try
    {
        const response = await fetch(`http://localhost:8080/api/products/vendor/${vendorId}`);
        if(response.ok)
        {
            product.value = await response.json();
            console.log("Товары с сервера:", product.value);
        }
        else 
            {
                throw new Error(`Ошибка HTTP: ${response.status}`);
            }
        
    }
    catch (error) 
    {
        console.error('Ошибка связи с сервером Java:', error);
        product.value =
        [
            { id: 101, brand: 'Игровое кресло', rating: 4.8, weeklySales: 124, mainImage: '...' },
            { id: 102, brand: 'Механическая клавиатура', rating: 4.9, weeklySales: 89, mainImage: '...' },
        ];
    }
    
    finally
    {
        loading.value = false;
    }

}
const selectProduct = ref([]);
const SelectedProduct = async (item) => {
    selectProduct.value = item;
    try {
        const response = await fetch(`http://localhost:8080/api/analytics/product/${item.id}`);
        if(response.ok) {
            const data = await response.json();
            console.log("Данные для графика: ", data);

            const newLabels = data.map(stat => String(stat.statDate).replace(/-/g, '.'));
            const newSales = data.map(stat => Number(stat.salesCount));
            const newReviews = data.map(stat => Number(stat.reviewsCount));

            chartData.value = {
                labels: newLabels,
                datasets: [
                    {
                        type: 'line',
                        label: 'Отзывы',
                        data: newReviews,
                        borderColor: '#333333',
                        borderWidth: 2,
                        fill: false,
                        tension: 0.4,
                        pointRadius: 5,
                        pointBackgroundColor: '#000000'
                    },
                    {
                        type: 'bar',
                        label: `Продажи: ${item.brand}`,
                        data: newSales,
                        backgroundColor: '#ff7e5f',
                        borderRadius: 4
                    }
                ]
            };
        }
        else {
            console.error('Бэкенд не нашел эндпоинт, статус:', response.status);
        }
    } catch(error) {
        console.error('Сбой сети:', error);
    }
}
onMounted (() =>
{
    loadProductsFromVendor();
})
</script>

<template>
<div class="statistics_grid">
        <div  class="products_statistics">
            <div v-for="item in product " :key="item.id" class="statistics_card" @click="SelectedProduct(item)">
                <div class="card_img_wrap">
                    <img :src="item.mainImage" alt="" >
                </div>
                <div class="card_info">
                    <span class="card_id">Арт. {{ item.id }}</span>
                    <p class="card_brand">{{ item.brand }}</p>
                    <div class="card_stats">
                        <span class="card_rating">⭐ {{ item.rating }}</span>
                        <span class="card_sales">📈 {{ item.weeklySales }} шт/нед</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="statistics_graph">
            <div class="chart_container">
                <Bar :data="chartData" :options="chartOptinos"/>
            </div>
        </div>
    </div>
</template>

<style scoped>
.statistics_grid
{
    display: grid;
    grid-template-columns: 40% 60%;
    height: 80vh;
    gap: 20px;
    overflow: hidden;
}
.products_statistics
{
    display: flex;
    flex-direction: column;
    gap: 15px;
    height: 50%;
    overflow-y: auto;
    padding-right: 10px;
}
.products_statistics::-webkit-scrollbar
{
    width: 6px;
}
.products_statistics::-webkit-scrollbar-thumb
{
    background-color: #ff7e5f;
    border-radius: 10px;
}
.statistics_card
{
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 20px;
    padding: 20px;
    font-size: 20px;
    font-family: 'Courier New';
    border-radius: 20px;
    border: 1px solid #eaeaea;
    max-height: 250px;
    max-width: 250px;    
    flex-shrink: 0;
    cursor: pointer;
}
.statistics_card:hover
{
    border-color: #ff7e5f;
    box-shadow: 0 4px 12px rgba(255, 126, 95, 0.1);
}
.card_img_wrap
{
    width: 85px;
    height: 85px;
    border-radius: 8px;
    overflow: hidden;
    flex-shrink: 0;
    background-color: #f9f9f9;
}
.card_img_wrap img
{
    width: 100%;
    height: 100%;
    object-fit: cover;
}
.card_info
{
    display: flex;
    flex-direction: column;
    gap: 5px;
}
.card_info p
{
    margin: 0;
}
.card_brand
{
    font-size: 18px;
    font-weight: 600;
    color: #333;
}
.card_id
{
    font-size: 12px;
    color: #999;
    text-transform: uppercase;
}

.card_brand
{
    font-size: 16px;
    font-weight: 700;
    color: #222;
    margin-top: 2px;
    margin-bottom: 8px;
}

.card_stats
{
    display: flex;
    gap: 15px;
    align-items: center;
}

.card_rating
{
    font-size: 14px;
    font-weight: 600;
    color: #ffb400;
}

.card_sales
{
    font-size: 13px;
    font-weight: 500;
    color: #2e7d32;
    background-color: #e8f5e9;
    padding: 2px 6px;
    border-radius: 6px;
}
.statistics_graph
{
    height: 100%;
    padding: 20px;
    border-radius: 20px;
    border: 2px, dashed #ccc;
    box-sizing: border-box;
    min-height: 0;
    min-width: 0;
}
</style>
