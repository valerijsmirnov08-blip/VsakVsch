<script setup>
    import {computed, ref} from 'vue';
    import VendorAllStatistics from '@/components/VendorAllStatistics.vue';
    import VendorStatistics from '@/components/VendorStatistics.vue';
    import VendorWarehouse from '@/components/VendorWarehouse.vue';
    import VendorReviews from '@/components/VendorReviews.vue';
    import VendorFinances from '@/components/VendorFinances.vue';
    import VendorRating from '@/components/VendorRating.vue';
    import VendorComplaints from '@/components/VendorComplaints.vue';

    const currentTab = ref('Главная');
    const currentPage = computed(() => componentMap[currentTab.value]);
    const componentMap =
    {
        'Главная': VendorAllStatistics,
        'Товары': VendorWarehouse,
        'Финансы': VendorFinances,
        'Статистика': VendorStatistics,
        'Рейтинги': VendorRating,
        'Жалобы': VendorComplaints,
        'Отзывы': VendorReviews
    };

</script>
<template>
<nav class="tabs_menu">
    <button @click="currentTab = 'Главная'" :class="{ active: currentTab === 'Главная' }">Главная</button>
    <button @click="currentTab = 'Товары'" :class="{ active: currentTab === 'Товары' }">Товары</button>
    <button @click="currentTab = 'Финансы'" :class="{ active: currentTab === 'Финансы' }">Финансы</button>
    <button @click="currentTab = 'Статистика'" :class="{ active: currentTab === 'Статистика' }">Статистика</button>
    <select @change="currentTab = $event.target.value" required  :class="{ active: currentTab === 'Рейтинги' }">
        <option value=""  disabled selected hidden>Рейтинги</option>
        <option value = 'Рейтинги'>Рейтинг продавца</option>
        <option value = 'Жалобы'>Жалобы</option>
    </select>

    <button @click="currentTab = 'Отзывы'" :class="{ active: currentTab === 'Отзывы' }">Отзывы</button>
</nav>
<component :is="currentPage"></component>
</template>
<style scoped>
.tabs_menu
{
    display: flex;
    justify-content: center;
    gap: 100px;
    font-size: 40px;
    margin-bottom: 30px;
    margin-top: 10px;
}
.tabs_menu button
{
    width: 120px;
    height: 30px;
    background: none;
    border: none;
    cursor: pointer;
    font-size: 16px;
}
.tabs_menu select
{
    width: 120px;
    height: 30px;
    background: none;
    border: none;
    cursor: pointer;
    font-size: 16px;
    border-color: none;
}
.tabs_menu button.active
{
    border-bottom: 3px solid #FF5B46;
    font-weight: bold;
}
</style>