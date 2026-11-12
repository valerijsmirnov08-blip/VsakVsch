<script setup>
import { ref } from 'vue';
import { Line } from 'vue-chartjs';
import { useRoute } from 'vue-router';
import { onMounted } from 'vue';
import 
{
   Chart as ChartJS,
   Title,
   Tooltip,
   Legend,
   LineElement,
   PointElement,
   CategoryScale,
   LinearScale,
   Filler,
} from 'chart.js';
ChartJS.register
(
    Title,
    Tooltip,
    Legend,
    LineElement,
    PointElement,
    CategoryScale,
    LinearScale,
    Filler
)
const chartData = ref
({
    labels: ['Январь', 'Февраль', 'Март', 'Апрель'],
    datasets: 
    [{
        type: 'line',
        data: [10,25,15,35],
        borderColor: '#ff7e5f',
        fill: true,
        tension: 0.4,
        label: 'Заказы',
        pointBackgroundColor: '#ffffff',
        pointBorderColor: '#ff7e5f',
        pointBorderWidth: 3,
        pointRadius: 4,
        pointHoverRadius: 6,
        backgroundColor: (context) =>
        {
            const chart = context.chart;
            const ctx = chart.ctx;
            const chartArea = chart.chartArea;
            if(!chartArea)
            {
                return null;
            }
            const gradient = ctx.createLinearGradient(0, chartArea.top, 0, chartArea.bottom);
            gradient.addColorStop(0, 'rgba(255, 126, 95, 0.5)');
            gradient.addColorStop(1, 'rgba(255, 126, 95, 0.0)')
            return gradient;
        }
    }]
})
const chartOptions = ref
({
    scales:
    {
        x:
        {
            grid:
            {
                display: false
            }
        },
        y:
        {
            beginAtZero: true,
            ticks:
            {
                stepSize: 1,
                precision: 0
            }
        }
        
    }
});
const periods = ['Неделя', '2 недели', 'Месяц', 'Год'];
const activePeriod = ref('Месяц');   


const updateChart = async(period) =>
{
    activePeriod.value = period;
    const session = localStorage.getItem('user_session');
    if(!session)
    {
        return;
    }
    const vendorData = JSON.parse(session);
    const vendorId = vendorData.id;
    try
    {
    const response = await fetch(`http://localhost:8080/api/orders/vendor/${vendorId}/chart?period=${encodeURIComponent(period)}`);
    if(response.ok)
    {
        const data = await response.json();

        if(data.length === 0)
        {
            chartData.value = 
            {
                labels: ['Нет данных'],
                datasets: [{ ...chartData.value.datasets[0], data: [0] }]
            }
            return;
        }

        const newLabels = data.map(item => item.label); 
        const newData = data.map(item => item.value);
        chartData.value = 
        {
            ...chartData.value,
            labels: newLabels,
            datasets:
            [{
                ...chartData.value.datasets[0],
                data: newData   
            }]
        }
    }
    else
    {
        console.error("Ошибка загрузки графика");
    }
}
catch(error)
{
    console.error("Ошибка сети:", error);
}
    
    // chartData.value.labels = newData.labels;
    // chartData.value.datasets[0].data = newData.data;
    // chartData.value =   {... chartData.value}
}
const summaryStats = ref
({
    onTimeDelivery: 0,
    deliveryRating: 0,
    productRating: 0.0,
    canceledPercent: 0,
    totalReviews: 0
})
const loadSummaryStats = async() =>
{
    const session = localStorage.getItem('user_session');
    if(!session)
    {
        return;
    }
    const vendorId = JSON.parse(session).id;
    try
    {
        const response = await fetch(`http://localhost:8080/api/orders/vendor/${vendorId}/summary-stats`);
        if(response.ok)
        {
            summaryStats.value = await response.json();
        }
    }
    catch(error)
    {
        console.error("Ошибка загрузки статистики правой панели:", error);
    }
}
onMounted (() =>
{
    updateChart('Месяц');
    loadSummaryStats();
})
</script>

<template>
<div class="statistics_grid">
        <div class="statistics_graph">
            <div class="chart_filters">
                <button @click="updateChart(period)" v-for="period in periods" :key="period" :class="{ active: activePeriod === period }">
                    {{ period }}
                </button>
            </div>
            <div class="chart_container">
                <Line :key="activePeriod" :data="chartData" :options="chartOptions" />
            </div>
        </div>
        <div class="vendor_main">
                <div class="vendor_main_info">
                    <details open class="vendor_table_info">
                        <summary>Рейтинг продавца</summary>
                        <ul>
                            <li><span>Доставка без опозданий</span> <span class="good">{{ summaryStats.onTimeDelivery }}</span> </li>
                            <li><span>Оценка доставки</span> <span class="good">{{ summaryStats.deliveryRating }}</span></li>
                            <li><span>Оценка товара</span> <span class="good">{{ summaryStats.productRating }}</span></li>
                            <li><span>Отменённые товары</span> <span class="warning">{{ summaryStats.canceledPercent }}</span></li>
                        </ul>
                       
                    </details>
                </div>
                <div class="vendor_main_info">
                    <details open class="vendor_table_info comm_stats">
                        <summary>Комуникация</summary>
                        <ul>
                            <li><span>Отзывы</span> <span>{{ summaryStats.totalReviews }}</span></li>
                            <li><span>Сообщения</span> <span>10</span></li>
                            <li><span>Вопросы</span> <span>3</span></li>
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
    padding: 50px;
    padding-top: 0;
    margin-top: 0;
}
.chart_filters {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
}

.chart_filters button {
    padding: 6px 16px;
    font-size: 14px;
    border: 1px solid #e0e0e0;
    background-color: transparent;
    color: #555;
    border-radius: 20px;
    cursor: pointer;
    transition: all 0.3s ease;
}
.chart_filters button.active {
    background-color: #ff7e5f;
    color: #ffffff;
    border-color: #ff7e5f;
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
    padding-top: 0;
    margin-top: 0;

}
.vendor_main_info
{
    display: flex;
    box-sizing: border-box;
    width: 100%;
    font-weight: 400;
    font-size: 20px;
    gap: 20px;
    padding: 25px;
    background-color: #ffffff;
    border-radius: 15px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        padding-top: 0;

}
.vendor_main_info li
{
    display: flex;
    justify-content: space-between;
    text-decoration: none;
    list-style-type: none;
    gap: 20px;
    font-size: 16px;
    border-bottom: 1px solid #f0f0f0;
    padding-bottom: 10px;

}
.comm_stats li span:last-child
{
    display: inline-block;
    min-width: 40px;
    padding: 2px 10px;
    background-color: #ff7e5f;
    border-radius: 40px;
    color: #ffffff;
    text-align: center;
    box-shadow: 0 2px 8px rgba(255, 126, 95, 0.3);

}
.comm_stats li:last-child
{
    border-bottom: 0;
}
.vendor_table_info
{
   
    width: 100%;
}
.vendor_table_info ul
{
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: left;
    gap: 20px;
    margin: 0;
    padding: 0;
}
.vendor_table_info summary
{
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
    font-size: 20px;
    font-weight: bold;
    list-style: none;
    cursor: pointer;
}
.vendor_table_info summary::after
{
    content: '▼';
    transition: transform 0.3s;
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
}
details[open].vendor_table_info summary::after
{
    transform: rotate(90deg);
}
.vendor_main_info li span.good
{
    color: #2e7d32;
    font-weight: 700;
}

.vendor_main_info li span.warning
{
    color: #d32f2f;
    font-weight: 700;
}
.chart_container {
    position: relative;
    width: 100%;
    height: 350px;
    margin-top: 20px;
}
</style>
