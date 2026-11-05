<script setup>
    import {computed, ref} from 'vue';

    const filter = ref('Все');
    const currentFilter = computed(() => filterMap[filter.value])
   
    const tableReviews = ref([{numberComplaints: '7-1316', status: 'Закрыто', dateComplaints: "26-10-2026", productId: '14', groupComplaints: 'Проблема с товаром', complaints: 'Брак' }])
    const current =  computed(() =>
    {
        if(filter.value = 'Все')
        {
            tableReviews.value = tableReviews.value;
        }
        else
        {
            tableReviews.value.filter();
        }
    })    
    
</script>
<template>
    <div class="complaints_grid">
        
        <div class="date_complaints_for_vendor">
            <h2>Отзывы покупателей</h2>
            <div class="for_vendor">
                
                <nav>
                    <button @click="filter = 'Все'" :class="{ active: filter === 'Все' }">Все</button>
                    <button @click="filter = 'Новые'" :class="{ active: filter === 'Новые' }">Новые</button>
                    <button @click="filter = 'Закрытые'" :class="{ active: filter === 'Закрытые' }">Закрытые</button>
                    <button @click="filter = 'Не закрытые'" :class="{ active: filter === 'Не закрытые' }">Не закрытые</button>
                </nav>
            </div>
        </div>
        <div class="search_product">
            <input type="text" placeholder="Название, артикул">
        </div>
        <table class="complaints_table">
        <thead>
            <tr>
                <th>№ Жалобы</th>
                <th>Статус жалобы</th>
                <th>Дата создания жалобы</th>
                <th>ID товара</th>
                <th>Группа жалоб</th>
                <th>Жалоба</th>
            </tr>   
        </thead>
        <tbody>
            <tr v-for="item in tableReviews">
                <td>{{ item.numberComplaints }}</td>
                <td >{{ item.status }}</td>
                <td>{{ item.dateComplaints }}</td>
                <td>{{ item.productId }}</td>
                <td>{{ item.groupComplaints }}</td>
                <td>{{ item.complaints }}</td>
            </tr>
        </tbody>
    </table>
    </div>
    
</template>

<style scoped>
.complaints_grid
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
.complaints_table
{
    width: 100%;
    border-collapse: collapse;
}
.complaints_table th
{
    text-align: left;
    color: #363636;
    padding: 15px 10px;
    border-bottom: 1px solid #E0E0E0;
}
.complaints_table td
{
    padding: 10px 10px;
    border-bottom: 1px solid #E0E0E0;
}
</style>