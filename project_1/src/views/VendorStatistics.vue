<script setup>
import { ref, computed } from 'vue';
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
    Filler
} from 'chart.js';

ChartJS.register(
    Title,
    Tooltip,
    Legend,
    BarElement,
    CategoryScale,
    LinearScale,
    RadialLinearScale,
    PointElement,
    LineElement,
    Filler
);

const periods = ['Месяц', 'Полгода', 'Всё время'];
const activePeriod = ref('Месяц');

// Фейковый бэкенд для оценки интерфейса и интерактивности
const fakeBackend = {
    'Месяц': {
        overallRating: 4.8,
        statusLabel: 'Надёжный продавец',
        productRating: 4.85,
        onTimeDelivery: '96%',
        canceledPercent: '1.8%',
        loyaltyIndex: '94%',
        totalReviews: 48,
        starsDistribution: [38, 7, 2, 0, 1], // 5, 4, 3, 2, 1 звёзд
        radarMetrics: [97, 96, 98, 95, 90] // Товар, Доставка, Без отмен, Без жалоб, Ответы
    },
    'Полгода': {
        overallRating: 4.7,
        statusLabel: 'Надёжный продавец',
        productRating: 4.72,
        onTimeDelivery: '93%',
        canceledPercent: '2.5%',
        loyaltyIndex: '91%',
        totalReviews: 215,
        starsDistribution: [160, 35, 11, 5, 4],
        radarMetrics: [94, 93, 95, 92, 88]
    },
    'Всё время': {
        overallRating: 4.6,
        statusLabel: 'Хороший рейтинг',
        productRating: 4.65,
        onTimeDelivery: '91%',
        canceledPercent: '3.1%',
        loyaltyIndex: '89%',
        totalReviews: 540,
        starsDistribution: [390, 95, 30, 15, 10],
        radarMetrics: [93, 91, 92, 90, 85]
    }
};

const currentStats = computed(() => fakeBackend[activePeriod.value]);

// Данные для горизонтального графика распределения звёзд
const starsChartData = computed(() => ({
    labels: ['5 ⭐', '4 ⭐', '3 ⭐', '2 ⭐', '1 ⭐'],
    datasets: [
        {
            label: 'Количество отзывов',
            data: currentStats.value.starsDistribution,
            backgroundColor: [
                '#ff7e5f',
                '#ff967d',
                '#feb47b',
                '#ffd1a9',
                '#e0e0e0'
            ],
            borderRadius: 8,
            barThickness: 22
        }
    ]
}));

const starsChartOptions = {
    indexAxis: 'y',
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: { display: false }
    },
    scales: {
        x: {
            beginAtZero: true,
            grid: { color: '#f3f3f3' },
            ticks: { precision: 0 }
        },
        y: {
            grid: { display: false },
            ticks: { font: { size: 14, weight: '600' } }
        }
    }
};

// Данные для радарной диаграммы критериев качества
const radarChartData = computed(() => ({
    labels: [
        'Оценка товаров',
        'Доставка вовремя',
        'Минимум отмен',
        'Отсутствие жалоб',
        'Скорость ответов'
    ],
    datasets: [
        {
            label: 'Показатели магазина (%)',
            data: currentStats.value.radarMetrics,
            backgroundColor: 'rgba(255, 126, 95, 0.25)',
            borderColor: '#ff7e5f',
            borderWidth: 2.5,
            pointBackgroundColor: '#ffffff',
            pointBorderColor: '#ff7e5f',
            pointBorderWidth: 2,
            pointRadius: 4
        }
    ]
}));

const radarChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: { display: false }
    },
    scales: {
        r: {
            min: 0,
            max: 100,
            ticks: {
                stepSize: 25,
                backdropColor: 'transparent',
                color: '#999'
            },
            grid: { color: '#ececec' },
            angleLines: { color: '#ececec' },
            pointLabels: {
                font: { size: 13, weight: '600' },
                color: '#333'
            }
        }
    }
};
</script>

<template>
    <div class="rating_dashboard">
        <!-- Верхняя панель с заголовком и фильтром периода -->
        <div class="rating_header">
            <div>
                <h2>Рейтинг и качество работы магазина</h2>
                <p class="subtitle">Сводная оценка по отзывам, доставке и отменам заказов</p>
            </div>
            <div class="period_filters">
                <button
                    v-for="period in periods"
                    :key="period"
                    :class="{ active: activePeriod === period }"
                    @click="activePeriod = period"
                >
                    {{ period }}
                </button>
            </div>
        </div>

        <!-- Верхний ряд: Главный рейтинг + 4 карточки критериев -->
        <div class="kpi_grid">
            <div class="kpi_card main_score_card">
                <span class="kpi_label">Общий рейтинг</span>
                <div class="main_score_value">
                    {{ currentStats.overallRating }} <span>/ 5.0 ⭐</span>
                </div>
                <span class="status_badge">{{ currentStats.statusLabel }}</span>
            </div>

            <div class="kpi_card">
                <span class="kpi_label">Оценка товаров</span>
                <span class="kpi_value good">{{ currentStats.productRating }} ⭐</span>
                <span class="kpi_hint">На основе {{ currentStats.totalReviews }} отзывов</span>
            </div>

            <div class="kpi_card">
                <span class="kpi_label">Доставка без опозданий</span>
                <span class="kpi_value good">{{ currentStats.onTimeDelivery }}</span>
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

        <!-- Нижний ряд: Два графика -->
        <div class="charts_grid">
            <div class="chart_box">
                <h3>Распределение оценок покупателей</h3>
                <div class="canvas_wrap">
                    <Bar :data="starsChartData" :options="starsChartOptions" />
                </div>
            </div>

            <div class="chart_box">
                <h3>Профиль качества продавца</h3>
                <div class="canvas_wrap">
                    <Radar :data="radarChartData" :options="radarChartOptions" />
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.rating_dashboard {
    display: flex;
    flex-direction: column;
    gap: 25px;
    padding: 10px 40px 40px 40px;
}

.rating_header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 15px;
}

.rating_header h2 {
    margin: 0;
    font-size: 24px;
    color: #222;
}

.subtitle {
    margin: 5px 0 0 0;
    font-size: 14px;
    color: #777;
}

.period_filters {
    display: flex;
    gap: 10px;
}

.period_filters button {
    padding: 8px 18px;
    font-size: 14px;
    border: 1px solid #e0e0e0;
    background-color: #ffffff;
    color: #555;
    border-radius: 20px;
    cursor: pointer;
    transition: all 0.25s ease;
}

.period_filters button.active {
    background-color: #ff7e5f;
    color: #ffffff;
    border-color: #ff7e5f;
    box-shadow: 0 4px 10px rgba(255, 126, 95, 0.25);
}

.kpi_grid {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 18px;
}

.kpi_card {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 8px;
    padding: 20px;
    background-color: #ffffff;
    border-radius: 16px;
    border: 1px solid #f0f0f0;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
}

.main_score_card {
    background: linear-gradient(135deg, #ff7e5f, #feb47b);
    color: #ffffff;
    border: none;
}

.main_score_card .kpi_label {
    color: rgba(255, 255, 255, 0.9);
}

.main_score_value {
    font-size: 30px;
    font-weight: 800;
}

.main_score_value span {
    font-size: 16px;
    font-weight: 500;
}

.status_badge {
    align-self: flex-start;
    padding: 4px 10px;
    background-color: rgba(255, 255, 255, 0.25);
    border-radius: 12px;
    font-size: 12px;
    font-weight: 600;
}

.kpi_label {
    font-size: 14px;
    color: #666;
    font-weight: 500;
}

.kpi_value {
    font-size: 26px;
    font-weight: 700;
}

.kpi_value.good {
    color: #2e7d32;
}

.kpi_value.warning {
    color: #d32f2f;
}

.kpi_hint {
    font-size: 12px;
    color: #999;
}

.charts_grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 20px;
}

.chart_box {
    background-color: #ffffff;
    padding: 24px;
    border-radius: 20px;
    border: 1px solid #eaeaea;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
}

.chart_box h3 {
    margin: 0 0 20px 0;
    font-size: 18px;
    color: #333;
}

.canvas_wrap {
    position: relative;
    height: 290px;
    width: 100%;
}
</style>