<script setup>
    import { ref } from 'vue'

    const table = ref([{status: 'Ожидает оплаты', planeDate: '23.12.2026', invoiceId: '1212', amount: '1234124', paymentDate: '23.1.2027'}]);
    const  status = ref('');
    const  planeDate = ref('');
    const  invoiceId = ref('');
    const  amount = ref('');
    const  paymentDate = ref('');

</script>
<template>
    <div class="finances_grid">
        
        <div class="date_finances">
            <div class="finances">
                <h2>Выплаты</h2>
                <select>
                    <option>Все счета</option>
                    <option>Не выплаченные</option>
                    <option>Выплаченные</option>
                </select>
            </div>
            <div class="finance_accaunt">
                
                <img src="/Кошелёк.png" alt="Кошелёк">
                <p>Ваш баланс:   ₽</p>
            </div>
        </div>
        <table class="payouts_table">
        <thead>
            <tr>
                <th>Статус</th>
                <th>Планируемая дата</th>
                <th>Номер счета</th>
                <th>Сумма</th>
                <th>Дата выплаты</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="item in table">
                <td>{{ item.status }}</td>
                <td>{{ item.planeDate }}</td>
                <td>{{ item.invoiceId }}</td>
                <td>{{ item.amount }}  ₽</td>
                <td>{{ item.paymentDate }}</td>
            </tr>
        </tbody>
    </table>
    </div>
    
    
</template>
<style scoped>
    .finances_grid
    {
        margin: 30px;
    }
    .date_finances
    {
        display: flex;
        justify-content: space-between;
        margin: 30px;
    }
    .date_finances select
    {
        margin: 0;
    }
    .finances
    {
        display: flex;
        flex-direction: column;
        gap: 10px;
        font-size: 20px;
    }

    .finance_accaunt
    {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 20px;
        font-size: 20px;
    }
    .finance_accaunt img
    {
        width: 100px;
        display: flex;
        flex-direction: column;
        align-self: center;
    }

    .payouts_table
    {
        width: 100%;
        border-collapse: collapse;
    }
    .payouts_table th
    {
        text-align: left;
        color: #363636;
        padding: 15px 10px;
        border-bottom: 1px solid #E0E0E0;
    }
    .payouts_table td
    {
        padding: 10px 10px;
        border-bottom: 1px solid #E0E0E0;
    }
    .status-gray
    {
        color: #363636;
    }
</style>