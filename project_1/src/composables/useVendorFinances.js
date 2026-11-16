import {ref} from 'vue';

export function useVendorFinances()
{
    const summary = ref({
    availableBalance: 0,
    frozenBalance: 0,
    totalPaid: 0
    });
    const payouts = ref([]);
    const loading = ref(false);
    const error = ref(null);
    const vendorId = ref(null);

    const getVendorId = () =>
    {
        const session = localStorage.getItem('user_session');
        if(!session)
        {
            return;
        }
        const parsed = JSON.parse(session);
        return parsed.vendorId || parsed.id;
    }
    const fetchFinanceData = async() =>
    {
        const vendorId = getVendorId();
        if (!vendorId) return;

        loading.value = true;
        error.value = null;
        try
        {
           const [summaryRes, payoutsRes] = await Promise.all([
                fetch(`http://localhost:8080/api/finances/summary/${vendorId}`),
                fetch(`http://localhost:8080/api/finances/payouts/${vendorId}`)
            ]);
            if (!summaryRes.ok) throw new Error('Ошибка загрузки баланса');
            if (!payoutsRes.ok) throw new Error('Ошибка загрузки истории выплат');

            summary.value = await summaryRes.json();
            payouts.value = await payoutsRes.json();
        }
        catch(err)
        {
            console.error(err);
            error.value = err.message;
        }
        finally
        {
            loading.value = false;
        }
    }
    const createPayoutRequest = async() =>
    {
        const vendorId = getVendorId();
        if (!vendorId) return;

        loading.value = true;
        error.value = null;
        try
        {
            
            const response = await fetch(`http://localhost:8080/api/finances/payout/${vendorId}`,
                {
                    method: 'POST'
                })
            if(response.ok)
            {
                await fetchFinanceData();
            }
            else
            {
                const message = await response.text();
                error.value = message || 'Не удалось создать заявку на вывод';
            }
        }
        catch(err)
        {
            error.value = err.message
        }
        finally
        {
            loading.value = false;
        }
    }
    return {
        summary,
        payouts,
        loading,
        error,
        fetchFinanceData,
        createPayoutRequest
    };
}