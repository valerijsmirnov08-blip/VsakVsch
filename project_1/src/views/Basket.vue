<script setup>
import { ref, onMounted, computed } from 'vue';
import { RouterLink } from 'vue-router';
import { notify } from '@/composables/useNotify.js';
import {getCart, updateCartQuantity, removeCartItem, checkoutCart} from '@/composables/cartApi.js';

const cartItems = ref([]);
const loading = ref(true);

const loadCart = async () => {
    const session = localStorage.getItem('user_session');
    if (!session) {
        loading.value = false;
        return;
    }
    try {
        const user = JSON.parse(session);
        const userId = user.id || user.userId;
        cartItems.value = await getCart(userId);  
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        notify.error('Не удалось загрузить корзину');
    } finally {
        loading.value = false;
    }
};

const totalItemsCount = computed(() => {
    if (!cartItems.value || cartItems.value.length === 0) return 0;
    return cartItems.value.reduce((sum, item) => sum + (Number(item.quantity) || 1), 0);
});

const totalCost = computed(() => {
    if (!cartItems.value || cartItems.value.length === 0) return 0;
    return cartItems.value.reduce((sum, item) => {
        const itemPrice = item.product && item.product.price ? Number(item.product.price) : 0;
        const itemQty = item.quantity ? Number(item.quantity) : 1;
        return sum + (itemPrice * itemQty);
    }, 0);
});

const deleteItem = async (cartItemId, silent = false) => {
    if (!silent) {
        const confirmed = await notify.confirm({
            title: 'Удаление товара',
            message: 'Убрать этот товар из корзины?',
            confirmText: 'Удалить',
            cancelText: 'Оставить',
            danger: true
        });
        if (!confirmed) return;
    }

    try {
        const isDeleted = await removeCartItem(cartItemId);
        if(isDeleted)
        {
            cartItems.value = cartItems.value.filter(item => item.id !== cartItemId);
        }
    } catch (error) {
        console.error('Ошибка при удалении', error);
        notify.error('Ошибка соединения с сервером');
    }
};

const Plus = async (productId) => {
    const session = localStorage.getItem('user_session');
    if (!session) {
        notify.error('Пожалуйста, войдите в аккаунт');
        return;
    }

    try
    {
        const user = JSON.parse(session);
        const userId = user.id || user.userId;
        const isUpdated = await updateCartQuantity(userId, productId, 1);
        const foundItem = cartItems.value.find(item => item.product.id === productId);
        if (isUpdated && foundItem) foundItem.quantity++;
    } catch (error) {
        console.error('Ошибка прибавления товара', error);
    }
};

const Minus = async (productId) => {
    const session = localStorage.getItem('user_session');
    if (!session) {
        notify.error('Пожалуйста, войдите в аккаунт');
        return;
    }

    const foundItem = cartItems.value.find(item => item.product.id === productId);
    if (!foundItem) return;

    if (foundItem.quantity <= 1) {
        await deleteItem(foundItem.id, false);
        return;
    }

    try
    {
        const user = JSON.parse(session);
        const userId = user.id || user.userId;
        const isUpdated = await updateCartQuantity(userId, productId, -1);
        if (isUpdated) foundItem.quantity--;
    } catch (error) {
        console.error('Ошибка уменьшения количества', error);
    }
};

const Pay = async () => {
    const session = localStorage.getItem('user_session');
    if (!session) {
        notify.error('Пожалуйста, войдите в аккаунт');
        return;
    }

    const confirmed = await notify.confirm({
        title: 'Оформление заказа',
        message: `Подтвердить покупку ${totalItemsCount.value} шт. на сумму ${totalCost.value.toLocaleString('ru-RU')} ₽?`,
        confirmText: 'Оплатить заказ',
        cancelText: 'Отмена'
    });
    if (!confirmed) return;

    try 
    {
        const user = JSON.parse(session);
        const userId = user.id || user.userId;
        const { ok, message } = await checkoutCart(userId);
        if (ok) { cartItems.value = []; notify.success('Заказ успешно оформлен!'); }
        else notify.error(message || 'Не удалось оформить заказ');
    } catch (error) {
        console.error('Ошибка при оформлении заказа:', error);
        notify.error('Не удалось связаться с сервером');
    }
};

onMounted(() => {
    loadCart();
});
</script>

<template>
    <div class="basket_page">
        <div class="basket_container">
            <header class="basket_header">
                <RouterLink to="/" class="back_btn">
                    <img src="/Назад.png" alt="" />
                    <span>На витрину</span>
                </RouterLink>
                <div class="title_row">
                    <h1>Корзина</h1>
                    <span v-if="totalItemsCount > 0" class="count_badge">{{ totalItemsCount }} шт.</span>
                </div>
            </header>

            <div class="basket_layout">
                <div class="basket_main">
                    <div v-if="loading" class="state_card">
                        <h2>Загрузка товаров...</h2>
                        <img class="state_img" src="/корзина2.png" alt="" />
                    </div>

                    <div v-else-if="cartItems.length === 0" class="state_card">
                        <img class="state_img" src="/корзина2.png" alt="" />
                        <h2>В корзине пока пусто</h2>
                        <p>Загляните в каталог — там наверняка найдётся что-то интересное</p>
                        <RouterLink to="/" class="go_shop_btn">Перейти за покупками</RouterLink>
                    </div>

                    <div v-else class="cart_items_list">
                        <article v-for="item in cartItems" :key="item.id" class="cart_item_card">
                            <div class="item_img_box">
                                <img
                                    :src="'/' + item.product.details?.mainImage"
                                    :alt="item.product.model"
                                    class="item_img"
                                />
                            </div>

                            <div class="item_info">
                                <span class="item_brand">{{ item.product.brand }}</span>
                                <h3 class="item_model">{{ item.product.model }}</h3>
                                <p class="item_sku">Артикул: {{ item.product.vendorSku }}</p>
                            </div>

                            <div class="item_controls">
                                <div class="qty_stepper">
                                    <button
                                        type="button"
                                        class="step_btn"
                                        @click="Minus(item.product.id)"
                                    >
                                        −
                                    </button>
                                    <span class="qty_val">{{ item.quantity }}</span>
                                    <button
                                        type="button"
                                        class="step_btn"
                                        @click="Plus(item.product.id)"
                                    >
                                        +
                                    </button>
                                </div>

                                <div class="price_col">
                                    <span class="total_item_price">
                                        {{ ((Number(item.product.price) || 0) * (Number(item.quantity) || 1)).toLocaleString('ru-RU') }} ₽
                                    </span>
                                    <span v-if="item.quantity > 1" class="unit_price">
                                        {{ Number(item.product.price).toLocaleString('ru-RU') }} ₽ / шт.
                                    </span>
                                </div>

                                <button
                                    type="button"
                                    class="btn_remove"
                                    @click="deleteItem(item.id)"
                                >
                                    Удалить
                                </button>
                            </div>
                        </article>
                    </div>
                </div>

                <aside class="summary_card">
                    <h2>Итого</h2>
                    <div class="summary_row">
                        <span>Товары ({{ totalItemsCount }} шт.)</span>
                        <span>{{ totalCost.toLocaleString('ru-RU') }} ₽</span>
                    </div>
                    <div class="summary_row">
                        <span>Доставка</span>
                        <span class="free_tag">Бесплатно</span>
                    </div>
                    <div class="summary_total">
                        <span>К оплате</span>
                        <span>{{ totalCost.toLocaleString('ru-RU') }} ₽</span>
                    </div>
                    <button
                        type="button"
                        class="checkout_btn"
                        :disabled="cartItems.length === 0"
                        @click="Pay"
                    >
                        Оплатить заказ
                    </button>
                </aside>
            </div>
        </div>
    </div>
</template>

<style scoped>
.basket_page {
    background: #fdfbf9;
    min-height: calc(100vh - 80px);
    padding: 32px 20px 80px;
    font-family: system-ui, -apple-system, sans-serif;
    box-sizing: border-box;
}

.basket_container {
    max-width: 1120px;
    margin: 0 auto;
}

.basket_header {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
    margin-bottom: 26px;
}

.back_btn {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    text-decoration: none;
    color: #5b5563;
    font-size: 14px;
    font-weight: 600;
    padding: 8px 14px;
    border-radius: 12px;
    background: #ffffff;
    border: 1px solid #eee8f2;
    transition: border-color 0.15s, color 0.15s;
}

.back_btn:hover {
    border-color: #feb47b;
    color: #ff7e5f;
}

.back_btn img {
    width: 18px;
    height: 18px;
    object-fit: contain;
}

.title_row {
    display: flex;
    align-items: center;
    gap: 12px;
}

.title_row h1 {
    margin: 0;
    font-size: 30px;
    font-weight: 800;
    color: #1f2937;
}

.count_badge {
    padding: 4px 12px;
    border-radius: 20px;
    background: #f0ebf4;
    color: #5b4b6a;
    font-size: 14px;
    font-weight: 700;
}

.basket_layout {
    display: grid;
    grid-template-columns: 1fr 350px;
    gap: 28px;
    align-items: start;
}

.basket_main {
    min-width: 0;
}

.state_card {
    background: #ffffff;
    border: 1px solid #eee8f2;
    border-radius: 24px;
    padding: 56px 24px;
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 14px;
}

.state_img {
    width: 84px;
    height: 84px;
    object-fit: contain;
    opacity: 0.85;
}

.state_card h2 {
    margin: 0;
    font-size: 22px;
    color: #1f2937;
}

.state_card p {
    margin: 0;
    font-size: 15px;
    color: #6b7280;
    max-width: 360px;
}

.go_shop_btn {
    margin-top: 8px;
    padding: 13px 26px;
    border-radius: 16px;
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    color: #ffffff;
    text-decoration: none;
    font-size: 15px;
    font-weight: 600;
    box-shadow: 0 6px 16px rgba(255, 126, 95, 0.25);
}

.cart_items_list {
    display: flex;
    flex-direction: column;
    gap: 16px;
}

.cart_item_card {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 18px;
    background: #ffffff;
    border: 1px solid #eee8f2;
    padding: 18px 22px;
    border-radius: 22px;
    box-shadow: 0 4px 14px rgba(0, 0, 0, 0.02);
    transition: border-color 0.18s, box-shadow 0.18s;
}

.cart_item_card:hover {
    border-color: #ffd8bd;
    box-shadow: 0 8px 22px rgba(0, 0, 0, 0.04);
}

.item_img_box {
    width: 84px;
    height: 84px;
    border-radius: 16px;
    background: #fdfbf9;
    border: 1px solid #f4eff7;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    padding: 6px;
    box-sizing: border-box;
}

.item_img {
    width: 100%;
    height: 100%;
    object-fit: contain;
}

.item_info {
    flex: 1;
    min-width: 0;
}

.item_brand {
    font-size: 11px;
    font-weight: 700;
    color: #8b8294;
    text-transform: uppercase;
    letter-spacing: 0.8px;
}

.item_model {
    margin: 4px 0 6px;
    font-size: 17px;
    color: #111827;
    line-height: 1.3;
    word-break: break-word;
}

.item_sku {
    font-size: 12px;
    color: #6b7280;
    margin: 0;
}

.item_controls {
    display: flex;
    align-items: center;
    gap: 20px;
    flex-shrink: 0;
}

.qty_stepper {
    display: inline-flex;
    align-items: center;
    background: #f5f1f8;
    border-radius: 14px;
    padding: 4px;
    gap: 6px;
}

.step_btn {
    width: 30px;
    height: 30px;
    border-radius: 10px;
    border: none;
    background: #ffffff;
    color: #1f2937;
    font-size: 17px;
    font-weight: 700;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
    transition: background 0.15s, color 0.15s;
}

.step_btn:hover {
    background: #ff7e5f;
    color: #ffffff;
}

.qty_val {
    min-width: 24px;
    text-align: center;
    font-size: 15px;
    font-weight: 700;
    color: #1f2937;
}

.price_col {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    min-width: 96px;
}

.total_item_price {
    font-size: 18px;
    font-weight: 800;
    color: #111827;
    white-space: nowrap;
}

.unit_price {
    font-size: 12px;
    color: #9ca3af;
    margin-top: 2px;
    white-space: nowrap;
}

.btn_remove {
    background: #fff5f5;
    border: 1px solid #fecaca;
    color: #dc2626;
    padding: 8px 14px;
    border-radius: 12px;
    font-size: 13px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.15s;
}

.btn_remove:hover {
    background: #ef4444;
    border-color: #ef4444;
    color: #ffffff;
}

.summary_card {
    position: sticky;
    top: 24px;
    background: #ffffff;
    border: 1px solid #eee8f2;
    border-radius: 24px;
    padding: 26px;
    box-shadow: 0 10px 26px rgba(0, 0, 0, 0.03);
    display: flex;
    flex-direction: column;
    gap: 14px;
}

.summary_card h2 {
    margin: 0 0 4px;
    font-size: 20px;
    color: #111827;
}

.summary_row {
    display: flex;
    justify-content: space-between;
    font-size: 14px;
    color: #4b5563;
}

.free_tag {
    color: #15803d;
    font-weight: 600;
}

.summary_total {
    display: flex;
    justify-content: space-between;
    align-items: baseline;
    padding-top: 14px;
    border-top: 1px solid #f0ebf4;
    font-size: 17px;
    font-weight: 700;
    color: #111827;
}

.summary_total span:last-child {
    font-size: 24px;
    font-weight: 800;
    color: #e04f2b;
}

.checkout_btn {
    margin-top: 6px;
    width: 100%;
    height: 52px;
    border-radius: 16px;
    border: none;
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    color: #ffffff;
    font-size: 16px;
    font-weight: 700;
    cursor: pointer;
    box-shadow: 0 6px 18px rgba(255, 126, 95, 0.28);
    transition: transform 0.15s, box-shadow 0.15s;
}

.checkout_btn:hover:not(:disabled) {
    transform: translateY(-1px);
    box-shadow: 0 8px 22px rgba(255, 126, 95, 0.36);
}

.checkout_btn:disabled {
    background: #e5e7eb;
    color: #9ca3af;
    box-shadow: none;
    cursor: not-allowed;
}

@media (max-width: 920px) {
    .basket_layout {
        grid-template-columns: 1fr;
    }

    .summary_card {
        position: static;
    }
}

@media (max-width: 640px) {
    .basket_page {
        padding: 18px 12px 84px;
    }

    .title_row h1 {
        font-size: 24px;
    }

    .cart_item_card {
        flex-wrap: wrap;
        padding: 16px;
        gap: 14px;
    }

    .item_img_box {
        width: 72px;
        height: 72px;
    }

    .item_info {
        flex: 1 1 calc(100% - 92px);
    }

    .item_controls {
        width: 100%;
        justify-content: space-between;
        padding-top: 12px;
        border-top: 1px solid #f4f0f7;
        gap: 10px;
    }

    .price_col {
        align-items: center;
        min-width: auto;
    }
}
</style>