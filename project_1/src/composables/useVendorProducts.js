import { ref } from 'vue';
import { API_URL } from './apiConfig.js';

const API_BASE = `${API_URL}/products`;

export function useVendorProducts()
{
    const products = ref([]);
    const product = ref(null);
    const loading = ref(false);
    const errorMessage = ref('');

    const getVendorId = () =>
    {
        const session = localStorage.getItem('user_session');
        if(!session)
        {
            return null;
        }
        return JSON.parse(session).id;
    }
    const fetchVendorProducts = async () => {
        const vendorId = getVendorId();
        if (!vendorId) {
            errorMessage.value = 'Необходимо авторизоваться';
            return;
        }

        loading.value = true;
        errorMessage.value = '';
        try {
            const response = await fetch(`${API_BASE}/vendor/${vendorId}`);
            if (!response.ok) throw new Error('Ошибка загрузки товаров склада');
            products.value = await response.json();
        } catch (error) {
            errorMessage.value = error.message;
        } finally {
            loading.value = false;
        }
    };

    const fetchProductById = async (id) => {
        loading.value = true;
        errorMessage.value = '';
        try {
            const response = await fetch(`${API_BASE}/${id}`);
            if (!response.ok) throw new Error('Товар не найден');
            const data = await response.json();
            if (!data.details) data.details = {};
            product.value = data;
        } catch (error) {
            errorMessage.value = error.message;
        } finally {
            loading.value = false;
        }
    };
    const createProduct = async (payload) => {
        loading.value = true;
        errorMessage.value = '';
        try {
            const response = await fetch(`${API_BASE}/create`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            if (!response.ok) {
                const errText = await response.text();
                throw new Error(errText || 'Ошибка при создании товара');
            }
            return true;
        } catch (error) {
            errorMessage.value = error.message;
            alert('Ошибка: ' + error.message);
            return false;
        } finally {
            loading.value = false;
        }
    };
    const updateProduct = async (id, updatedProduct) => {
        loading.value = true;
        errorMessage.value = '';
        try {
            const response = await fetch(`${API_BASE}/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updatedProduct)
            });
            if (!response.ok) throw new Error('Ошибка при сохранении изменений');
            return true;
        } catch (error) {
            errorMessage.value = error.message;
            alert('Ошибка: ' + error.message);
            return false;
        } finally {
            loading.value = false;
        }
    };
    const deleteProduct = async (id) => {
        loading.value = true;
        errorMessage.value = '';
        try {
            const response = await fetch(`${API_BASE}/${id}`, {
                method: 'DELETE'
            });
            if (!response.ok)
            {
                const errText = await response.text();
                throw new Error(errText || 'Ошибка при удалении товара');
            }
        } catch (error) {
            errorMessage.value = error.message;
            alert('Ошибка: ' + error.message);
            return false;
        } finally {
            loading.value = false;
        }
    };

    return {
        products,
        product,
        loading,
        errorMessage,
        getVendorId,
        fetchVendorProducts,
        fetchProductById,
        createProduct,
        updateProduct,
        deleteProduct
    };
}





