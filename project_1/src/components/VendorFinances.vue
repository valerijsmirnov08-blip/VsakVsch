<script setup>
    import { ref, onMounted,computed } from 'vue'
    import { useVendorFinances } from '@/composables/useVendorFinances';


    const currentTab = ref('Все счета')
    const { summary, payouts, loading, error, fetchFinanceData, createPayoutRequest } = useVendorFinances();
    onMounted(() =>
    {
        fetchFinanceData();
    });
    const filteredPayouts = computed(() =>
    {
        if(currentTab.value === 'Не выплаченные')
        {
            return payouts.value.filter(item => item.status === 'PENDING')
        }
        else if(currentTab.value === 'Выплаченные')
        {
            return payouts.value.filter(item => item.status === 'PAID')
        }
        else
        {
            return payouts.value;
        }
    })
    const formatStatus = (status) =>
    {
        if(status === 'PENDING')
        {
            return 'Ожидает оплаты';
        }
        if(status === 'PAID')
        {
            return 'Выплачено';
        }
        else
        {
            return status;
        }
    }
    const formatDate = (dateStr) =>
    {
        if(!dateStr)
        {
            return '-';
        }
        return new Date(dateStr).toLocaleDateString('ru-RU');
    }
    const handlePayout = async () =>
    {
        if (!confirm(`Создать заявку на вывод ${summary.value.availableBalance} ₽?`)) return;
        await createPayoutRequest();
    };
</script>
<template>
    <div class="finances_grid">
        
        <div class="date_finances">
            <div class="finances">
                <h2>Выплаты</h2>
                <div class="finance_accaunt">
                
                <img src="/Кошелёк.png" alt="Кошелёк">
                <div class="balance_info">
                    <p class="main_balance">Доступно к выводу:  <strong>{{ summary.availableBalance }} ₽</strong></p>
                    <div class="sub_balance">
                        <span>В обработке (заморожено): <strong>{{ summary.frozenBalance }} ₽</strong></span>
                        <span>Всего выплачено: <strong>{{ summary.totalPaid }} ₽</strong></span>
                    </div>
                </div>
                <button class="btn_payout" :disabled="loading || summary.availableBalance <= 0" @click="handlePayout">
                    {{ loading ? 'Обработка...' : 'Вывести средства' }}
                </button>
            </div>
            <p v-if="error" class="error_text">{{ error }}</p>
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
            <tr v-if="filteredPayouts.length === 0"><td colspan="5">Счетов пока нет</td></tr>
            <tr v-for="item in filteredPayouts" :key="item.id">
                <td>{{ formatStatus(item.status) }}</td>
                <td>{{ formatDate(item.plannedDate) }}</td>
                <td>{{ item.invoiceNumber }}</td>
                <td>{{ item.netAmount }}  ₽</td>
                <td>{{ formatDate(item.paymentDate) }}</td>
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
        margin: 0px;
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
    .balance_info
    {
    display: flex;
    flex-direction: column;
    gap: 6px;
    }
    .main_balance
    {
        margin: 0;
        font-size: 22px;
    }
    .sub_balance
    {
        display: flex;
        gap: 25px;
        font-size: 15px;
        color: #666;
    }
    .btn_payout 
    {
        margin-left: auto;
        background: linear-gradient(to right, #ff7e5f, #feb47b);
        color: white;
        border: none;
        padding: 12px 24px;
        border-radius: 30px;
        font-size: 15px;
        font-weight: 600;
        cursor: pointer;
        box-shadow: 0 4px 10px rgba(255, 126, 95, 0.25);
        transition: opacity 0.2s;
    }
    .btn_payout:disabled 
    {
        opacity: 0.5;
        cursor: not-allowed;
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
    .error_text 
    {
        color: #d32f2f;
        font-size: 14px;
        margin: 5px 0;
    }
    .empty_row {
    text-align: center;
    color: #888;
    padding: 25px 0;
}
</style>