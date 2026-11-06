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
        <div class="statistics_graph">
            <div class="chart_container">
                <Bar :data="chartData" :options="chartOptinos" />
            </div>
        </div>
        <div class="vendor_main">
                <div class="vendor_main_info">
                    <details open class="vendor_table_info">
                        <summary>Рейтинг продавца</summary>
                        <ul>
                            <li value="">Доставка без опозданий 84% </li>
                            <li value="">Оценка доставки 94%</li>
                            <li value="">Оценка товара 4.5</li>
                            <li value="">Отменённые товары 10%</li>
                        </ul>
                       
                    </details>
                </div>
                <div class="vendor_main_info">
                    <details open class="vendor_table_info">
                        <summary>Комуникация</summary>
                        <ul>
                            <li value="">Отзывы <p> 243</p></li>
                            <li value="">Сообщения <p>10</p></li>
                            <li value="">Вопросы <p>3</p></li>
                        </ul>
                        
                        
                    </details>
                </div>
            </div>
    </div>
</template>

<style scoped>
.statistics_grid
{
    display: grid;
    grid-template-columns: 60% 40%;
    height: 80vh;
    gap: 20px;
    overflow: hidden;
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
.vendor_main
{
    display: flex;
    flex-direction: column;
    padding: 20px;
    margin: 20px;
    width: 100%;
    gap: 30px;
}
.vendor_main_info
{
    display: flex;
    width: 400px;
    font-weight: 400;
    font-size: 20px;
    gap: 20px;

}
.vendor_main_info li
{
    text-decoration: none;
    list-style-type: none;
    gap: 20px;
    font-size: 25px;
    display: inline-block;
}
.vendor_table_info
{

}
.vendor_table_info ul
{
    display: flex;
    flex-direction: column;
    justify-content: left;
    gap: 20px;
}
.vendor_table_info ul p
{
    text-align: center;
    width: 70px;
    display: inline-block;
    background-color: #ff7e5f;
    border-radius: 40px;
}
.vendor_table_info summary
{
    margin-bottom: 20px;
    font-size: 30px;
    font-weight: bold;
}
</style>
