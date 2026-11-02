<script setup>
    import { ref } from 'vue';
    import { Bar } from 'vue-chartjs';
    import
    {
        Chart as ChartJS,
        Title,
        Tooltip,
        Legend,
        BarElement,
        CategoryScale,
        LinearScale,
        LineElement,
        PointElement
    } from 'chart.js';
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
</script>

<template>
    <p>Успех общей статистики</p>
</template>

<style scoped>

</style>