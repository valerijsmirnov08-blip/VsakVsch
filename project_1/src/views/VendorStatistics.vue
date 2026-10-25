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
    PointElement} from 'chart.js';
    ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LineElement, LinearScale, PointElement)

    const chartData = ref
    ({
        labels:['Январь', 'Февраль', 'Март', 'Апрель'],
        datasets:
        [
            {
                type:'line',
                label: 'Отзывы',
                data:[10,25,15,35],
                borderColor: '#00000',
                borderWidth: 2,
                fill: false,
                tension: 0.4
            },
            {
                type:'bar',
                label: 'Продажи',
                data:[50,80,40,90],
                backgroundColor: '#e63946',
                borderRadius: 4
            }
        ]
    });
    const chartOptinos = ref({
        responsive:true,
        maintainAspectRatio: false
    })
    const selectProduct = ref('');
    const product = ref([]);
    const loading = ref(true);
    const analytics = ref([]);


    const loadProductsFromVendor = async() =>
{
    const session = localStorage.getItem('user_session');
    if(!session)
    {
        alert("Пожалуйста войдите в аккаунт");
        return;
        
    }
    const user = JSON.parse(session);
    
    const vendorId = user.id;
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
                console.error('Сервер ответил ошибкой HTTP:', response.status);
            }
    }
    catch (error) 
    {
        console.error('Ошибка связи с сервером Java:', error);
    }
    finally
    {
        loading.value = false;
    }

}
const SelectedProduct = async (item) =>
{
    selectProduct.value = item;
    try
    {
        const response = await fetch(`http://localhost:8080/api/analytics/product/${item.id}`);
        if(response.ok)
        {
            analytics.value = await response.json();
            console.log("Короче вот: ", analytics.value);

            const labels = analytics.value.map(stat => stat.monthLabel);
            const sales = analytics.value.map(stat => stat.salesCount);
            const reviews = analytics.value.map(stat => stat.reviewsCount);

            chartData.value = 
            ({
                labels:labels,
                datasets:
                [
                    {
                        type:'line',
                        label: 'Отзывы',
                        data:reviews,
                        borderColor: '#00000',
                        borderWidth: 2,
                        fill: false,
                        tension: 0.4
                    },
                    {
                        type:'bar',
                        label: `Продажи: ${item.brand}`,
                        data:sales,
                        backgroundColor: '#e63946',
                        borderRadius: 4
                    }
                ]
            });
        }
        else {
            console.error('Бэкенд не нашел эндпоинт, статус:', response.status);
        }
    } catch(error) {
        console.error('Сбой сети:', error);
    }
    // chartData.value.datasets[0].data = [5, 15, 10, 20];
    // chartData.value.datasets[1].data = [30, 50, 20, 70];
    // chartData.value.datasets[1].label = `Продажи: ${item.brand}`;

    // chartData.value = { ...chartData.value };
   
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
                <img :src="item.mainImage" alt="" >
                <p>{{ item.brand }}</p>
                <p>{{ item.rating }} ⭐</p>
            </div>
        </div>
        <div class="statistics_graph">
            <div class="chart_container">
                <Bar :data="chartData" :options="chartOptinos" />
            </div>
        </div>
    </div>
</template>

<style scoped>
.statistics_grid
{
    display: grid;
    grid-template-columns: 30% 70%;
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
    flex-direction: column;
    gap: 10px;
    padding: 20px;
    font-size: 20px;
    font-family: 'Courier New';
    border-radius: 20px;
    border: 2px solid #000;
    max-height: 250px;
    max-width: 250px;    
    flex-shrink: 0;
    align-items: center;
}
.statistics_card:hover
{
    cursor: pointer;
}
.statistics_card img
{
    width: 150px;
}
.statistics_graph
{
    height: 100%;
    padding: 20px;
    border-radius: 20px;
    border: 2px dashed #ccc;

}
.chart_container
{
    position: relative;
    height: 100%;
    width: 100%;
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
