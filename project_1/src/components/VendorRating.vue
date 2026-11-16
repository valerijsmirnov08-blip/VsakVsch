<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { Bar, Radar } from 'vue-chartjs';
import {
    Chart as ChartJS,
    Title,
    Tooltip,
    Legend,
    BarElement, 
    CategoryScale,
    LinearScale, 
    RadialLinearScale, 
    PointElement, 
    LineElement, 
    Filler,
    plugins} from 'chart.js';
    ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, RadialLinearScale, PointElement, LineElement, Filler)

const periods = ref(['Месяц', 'Полгода', 'Всё время']);
const activePeriod = ref('Месяц');

const currentStats = ref({
    overallRating: 0,
    statusLabel: 'Загрузка',
    productRating: 0,
    onTimeDelivery: '0',
    canceledPercent: '0%',
    loyaltyIndex: '0%',
    totalReviews: 0,
    starsDistribution: [0, 0, 0, 0, 0],
    radarMetrics: [0, 0, 0, 0, 0]
})
const props = defineProps(['vendorId']);
const fetchRating = async() =>
{
    const id = props.vendorId || localStorage.getItem('vendorId') || 1;
    const response = await fetch(`http://localhost:8080/api/vendor-rating/${id}?period=${encodeURIComponent(activePeriod.value)}`);
    if(response.ok)
    {
        currentStats.value = await response.json();
    }
}
const starsChartData = computed(() =>
({
    labels:['5 ⭐', '4 ⭐', '3 ⭐', '2 ⭐', '1 ⭐'],
    datasets:
    [{ 
        data: currentStats.value.starsDistribution,
        backgroundColor: ['#ff7e5f', '#ff967d', '#feb47b', '#ffd1a9', '#e0e0e0'],
        borderRadius: 8, barThickness: 22
    }]
}))
const starsChartOptions = 
{
    indexAxis: 'y', responsive: true, maintainAspectRatio: false,
    plugins: {legend: {display: false}},
    scales:
    {
        x:
        {
            beginAtZero: true,
            grid:{color:'#f3f3f3'}, 
            ticks: {precision: 0}
        },
        y:
        {
            grid:{display: false},
            ticks: {font: {size: 14, weight:'600'}}
        }
    }
}
const radarCharData = computed(() =>
({
    labels:['Оценка товаров', 'Доставка вовремя', 'Минимум отмен', 'Отсутствие жалоб', 'Скорость ответов'],
    datasets:
    [{
        data: currentStats.value.radarMetrics,
        backgroundColor: 'rgba(255, 126, 95, 0.25)',
        borderColor: '#ff7e5f',
        borderWidth: 2.5,
        pointBackgroundColor: '#ffffff',
        pointBorderColor: '#ff7e5f'
    }]
}))
const radarChartOptions = 
{
    responsive: true,
    maintainAspectRatio: false, 
    plugins: { legend: { display: false } },
    scales:
    {
        r:
        {
            min: 0, max: 100,
            ticks: { stepSize: 25, 
            backdropColor: 'transparent' },
            pointLabels: { font: { size: 13, weight: '600' }, color: '#333' }
        }
    }
}
onMounted(fetchRating);
watch(activePeriod, fetchRating);
</script>

<template>
    <div class="rating_dashboard">
        <div class="rating_header">
            <div>
                <h2>Рейтинг магазина</h2>
                <p class="subtitle">Сводная оценка по отзывам и доставке</p>
            </div>
            <div class="period_filters">
                <button v-for="i in periods" :key="i" :class="{active: activePeriod === i}" @click="activePeriod = i">{{ i }}</button>
            </div>
        </div>
        <div class="kpi_grid">
            <div class="kpi_card main_score_card">
                <span class="kpi_label">Общий рейтинг</span>
                <div class="main_score_value">{{ currentStats.overallRating }} <span>/ 5.0 ⭐</span></div>
                <span class="status_badge">{{ currentStats.statusLabel }}</span>
            </div>
            <div class="kpi_card">
                <span class="kpi_label">Оценка товаров</span>
                <span class="kpi_value good">{{ currentStats.productRating }} ⭐</span>
                <span class="kpi_hint">Отзывов: {{ currentStats.totalReviews }}</span>
            </div>
            <div class="kpi_card">
                <span class="kpi_label">Доставка без опозданий</span>
                <span class="kpi_value good">{{ currentStats.onTimeDelivery }}  </span>
                <span class="kpi_hint">Норма маркетплейса: от 90%</span>
            </div>  
            <div class="kpi_card">
                <span class="kpi_label">Отменённые заказы</span>
                <span class="kpi_value warning">{{ currentStats.canceledPercent }}</span>
                <span class="kpi_hint">Отмены по вине продавца</span>
            </div>
            <div class="kpi_card">
                <span class="kpi_label">Индекс лояльности</span>
                <span class="kpi_value good">{{ currentStats.loyaltyIndex }}</span>
                <span class="kpi_hint">Доля оценок 4 и 5 звёзд</span>
            </div>
        </div>
        <div class="charts_grid">
            <div class="chart_box">
                <h3>Распределение оценок покупателей</h3>
                <div class="canvas_wrap"><Bar :data="starsChartData" :options="starsChartOptions"/></div>
            </div>
             <div class="chart_box">
                <h3>Профиль качества продавца</h3>
                <div class="canvas_wrap"><Radar :data="radarCharData" :options="radarChartOptions"/></div>
            </div>
        </div>
    </div>
</template>
<style scoped>
.rating_dashboard
{
    display: flex;
    flex-direction: column;
    gap: 25px;
    padding: 10px 40px 40px 40px;
}
.rating_header
{
    display: flex;
    justify-content: space-between;
    align-items: center;

}
.rating_header h2
{
    margin: 0;
    font-size: 24px;
    color: #222;
}
.subtitle
{
    margin: 5px 0 0 0;
    font-size: 14px;
    color: #777;
}
.period_filters
{
    display: flex;
    gap: 10px;
}
.period_filters button
{
    padding: 8px 18px;
    border: 1px solid #e0e0e0;
    background: #fff;
    color: #555;
    border-radius: 20px;
    cursor: pointer;
    transition: 0.25s;
}
.period_filters button.active
{
    background: #ff7e5f;
    color: #fff;
    border-color: #ff7e5f;
    box-shadow: 0 4px 10px rgba(255, 126, 95, 0.25);
}
.kpi_grid
{
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 18px
}
.kpi_card
{
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 8px;
    padding: 20px;
    background: #fff;
    border-radius: 16px;
    border: 1px solid #f0f0f0;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04)  ;
}
.main_score_card
{
    background: linear-gradient(135deg, #ff7e5f, #feb47b);
    color: #fff;
    border: none;
}
.main_score_card .kpi_label
{
    color: rgba(255, 255, 255, 0.9);
}
.main_score_value
{
    font-size: 30px;
    font-weight: 800;
}
.main_score_value span
{
    font-size: 16px;
    font-weight: 500;
}
.status_badge
{
    align-self: flex-start;
    padding: 4px 10px;
    background: rgba(255, 255, 255, 0.25);
    border-radius: 12px;
    font-size: 12px;
}
.kpi_label
{
    font-size: 14px;
    color: #666;
    font-weight: 500;
}
.kpi_value
{
    font-size: 26px;
    font-weight: 700;
}
.kpi_value.good
{
    color: #2e7d32;
}
.kpi_value.warning
{
    color: #d32f2f;
}
.kpi_hint
{
    font-size: 12px;
    color: #999;
}
.charts_grid
{
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 20px;
}
.chart_box
{
    background: #fff;
    padding: 24px;
    border-radius: 20px;
    border: 1px solid #eaeaea;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
}
.chart_box h3
{
    margin: 0 0 20px 0;
    font-size: 18px;
    color: #333;
}
.canvas_wrap
{
    position: relative;
    height: 290px;
    width: 100%;
}
</style>
