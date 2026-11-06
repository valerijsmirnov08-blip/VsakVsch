<script setup>
    import { ref } from 'vue'

    const table = ref([{status: 'Ожидает оплаты', planeDate: '23.12.2026', invoiceId: '1212', amount: '1234124', paymentDate: '23.1.2027'}]);
    const  status = ref('');
    const  planeDate = ref('');
    const  invoiceId = ref('');
    const  amount = ref('');
    const  paymentDate = ref('');
    const currentTab = ref('Все счета')
</script>
<template>
    <div class="finances_grid">
        
        <div class="date_finances">
            <div class="finances">
                <h2>Выплаты</h2>
                <div class="finance_accaunt">
                
                <img src="/Кошелёк.png" alt="Кошелёк">
                <p>Ваш баланс:   ₽</p>
            </div>
                <div class="change_finances">
                    <nav>
                        <button @click="currentTab = 'Все счета'" :class="{ active: currentTab === 'Все счета' }">Все счета</button>
                        <button @click="currentTab = 'Не выплаченные'" :class="{ active: currentTab === 'Не выплаченные' }">Не выплаченные</button>
                        <button @click="currentTab = 'Выплаченные'" :class="{ active: currentTab === 'Выплаченные' }">Выплаченные</button>
                    </nav>
                </div>
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
        flex-direction: column;
        margin: 30px;
    }
    .change_finances
    {
        display: flex;
        justify-content: left;
        margin: 20px;
        align-items: center;
        gap: 40px;
        margin-left: 0;
        margin-bottom: 25px;
        background: none;
    }
    .change_finances nav .active
    {
        border-bottom: 3px solid #FF5B46;
        font-weight: bold;
    }
    .change_finances button
    {
        width: 135px;
        height: 20px;
        border: none;
        background: none;
        cursor: pointer;
        font-size: 14px;
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