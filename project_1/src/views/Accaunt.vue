<script setup>
import { onMounted, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import SupportChat from '@/components/SupportChat.vue';
import { notify } from '@/composables/useNotify.js';
import { API_URL } from '@/composables/apiConfig.js'

const router = useRouter();

const isEditing = ref(false);
const editName = ref('');
const editShopName = ref('');
const editShopDesc = ref('');
const editBrandName = ref('');
const editInn = ref('');
const editLegalAddress = ref('');
const editLogoUrl = ref('');
const vendorRating = ref(0);

const userDate = ref(null);
const userId = ref('');

const isVendor = computed(() => userDate.value?.role === 'VENDOR');

const loadVendorProfile = async (id) => {
    try {
        const response = await fetch(`${API_URL}/users/${id}/vendor-profile`);
        if (response.ok) {
            const vp = await response.json();
            editShopName.value = vp.shopName || '';
            editShopDesc.value = vp.shopDescription || '';
            editBrandName.value = vp.brandName || '';
            editInn.value = vp.inn || '';
            editLegalAddress.value = vp.legalAddress || '';
            editLogoUrl.value = vp.logoUrl || '';
            vendorRating.value = vp.ratingVendor ?? 0;
        }
    } catch (error) {
        console.error('Ошибка загрузки профиля продавца:', error);
    }
};

onMounted(async () => {
    const session = localStorage.getItem('user_session');
    if (session) {
        userDate.value = JSON.parse(session);
        editName.value = userDate.value.userName || '';
        userId.value = userDate.value.id || userDate.value.userId;
        if (userDate.value.role === 'VENDOR' && userId.value) {
            await loadVendorProfile(userId.value);
        }
    } else {
        router.push('/enter_accaunt');
    }
});

const handleLogout = async () => {
    const confirmed = await notify.confirm({
        title: 'Выход из аккаунта',
        message: 'Вы действительно хотите выйти из своего профиля?',
        confirmText: 'Выйти',
        cancelText: 'Остаться'
    });
    if (!confirmed) return;

    localStorage.removeItem('user_session');
    userDate.value = null;
    notify.info('Вы вышли из аккаунта');
    router.push('/enter_accaunt');
};

const saveProfile = async () => {
    const confirmed = await notify.confirm({
        title: 'Сохранение изменений',
        message: 'Применить обновлённые данные профиля?',
        confirmText: 'Сохранить',
        cancelText: 'Отмена'
    });
    if (!confirmed) return;

    try {
        const response = await fetch(`${API_URL}/user/${userId.value}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                shopName: editShopName.value.trim(),
                shopDescription: editShopDesc.value.trim(),
                userName: editName.value.trim(),
                brandName: editBrandName.value.trim(),
                inn: editInn.value.trim(),
                legalAddress: editLegalAddress.value.trim(),
                logoUrl: editLogoUrl.value.trim()
            })
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || errorData.messege || 'Ошибка ввода данных, повторите попытку позже');
        }

        const updateUser = await response.json();
        localStorage.setItem('user_session', JSON.stringify(updateUser));
        userDate.value = updateUser;
        if (isVendor.value) {
            await loadVendorProfile(userId.value);
        }
        notify.success('Профиль успешно обновлён!');
        isEditing.value = false;
    } catch (error) {
        notify.error(error.message || 'Ошибка соединения с сервером');
    }
};

const stopVendor = async () => {
    const confirmed = await notify.confirm({
        title: 'Закрытие магазина',
        message: 'Вы уверены, что хотите перестать быть продавцом? Все данные витрины будут скрыты.',
        confirmText: 'Перестать быть продавцом',
        cancelText: 'Отмена',
        danger: true
    });
    if (!confirmed) return;

    try {
        const response = await fetch(`${API_URL}/users/${userId.value}/stop_vendor`, {
            method: 'POST'
        });

        if (response.ok) {
            const updatedUser = await response.json();
            localStorage.setItem('user_session', JSON.stringify(updatedUser));
            userDate.value = updatedUser;
            notify.info('Статус продавца отключён');
        } else {
            notify.error('Ошибка при обновлении статуса');
        }
    } catch (error) {
        notify.error('Ошибка соединения с сервером');
    }
};
</script>

<template>
    <div v-if="userDate" class="bento_wrapper">
        <section class="hero_banner">
            <div class="hero_left">
                <img v-if="isVendor && editLogoUrl" :src="editLogoUrl" alt="" class="hero_logo" />
                <div v-else class="hero_avatar">{{ (userDate.userName || 'U').charAt(0).toUpperCase() }}</div>
                <div>
                    <h1>{{ userDate.userName }}</h1>
                    <p v-if="isVendor">
                        {{ editShopName || 'Магазин продавца' }}
                        <span v-if="editBrandName"> · {{ editBrandName }}</span>
                        <span class="rating_chip">★ {{ Number(vendorRating).toFixed(1) }}</span>
                    </p>
                    <p v-else>Личный кабинет покупателя</p>
                </div>
            </div>
            <div class="hero_actions">
                <button class="glass_btn" @click="isEditing = !isEditing">
                    {{ isEditing ? 'Закрыть настройки' : 'Редактировать профиль' }}
                </button>
                <button class="glass_btn dark" @click="handleLogout">Выйти</button>
            </div>
        </section>

        <section v-if="isEditing" class="bento_edit_panel">
            <h2>Редактирование профиля</h2>
            <form class="bento_form" @submit.prevent="saveProfile">
                <div class="inputs_grid">
                    <label class="field">
                        <span>Имя аккаунта</span>
                        <input v-model="editName" type="text" placeholder="Имя аккаунта" required />
                    </label>
                    <label v-if="isVendor" class="field">
                        <span>Название магазина</span>
                        <input v-model="editShopName" type="text" placeholder="Название магазина" required />
                    </label>
                    <label v-if="isVendor" class="field">
                        <span>Название бренда</span>
                        <input v-model="editBrandName" type="text" placeholder="Название бренда" />
                    </label>
                    <label v-if="isVendor" class="field">
                        <span>ИНН</span>
                        <input v-model="editInn" type="text" placeholder="ИНН организации или ИП" required />
                    </label>
                    <label v-if="isVendor" class="field">
                        <span>Юридический адрес</span>
                        <input v-model="editLegalAddress" type="text" placeholder="Юридический адрес" required />
                    </label>
                    <label v-if="isVendor" class="field">
                        <span>Ссылка на логотип</span>
                        <input v-model="editLogoUrl" type="text" placeholder="https://..." />
                    </label>
                </div>
                <label v-if="isVendor" class="field">
                    <span>Описание магазина</span>
                    <textarea v-model="editShopDesc" rows="3" placeholder="Описание магазина"></textarea>
                </label>
                <button type="submit" class="save_bento_btn">Сохранить изменения</button>
            </form>
        </section>

        <section class="bento_grid">
            <RouterLink v-if="!isVendor" to="/vendor_b" class="bento_tile span_4 vendor_spotlight">
                <div class="spotlight_left">
                    <div class="spotlight_icon_wrap">
                        <img src="/Продавец_ЧБ.png" alt="" />
                    </div>
                    <div>
                        <span class="spotlight_badge">Партнёрская программа</span>
                        <h3>Стать продавцом на маркетплейсе</h3>
                        <p>Откройте свой магазин, добавляйте товары и отслеживайте статистику продаж</p>
                    </div>
                </div>
                <span class="spotlight_cta">Открыть магазин →</span>
            </RouterLink>

            <RouterLink v-if="isVendor" to="/vendor_dash_board" class="bento_tile span_4 vendor_spotlight active_vendor">
                <div class="spotlight_left">
                    <div class="spotlight_icon_wrap">
                        <img src="/СтатистикаПродавец_ЧБ.png" alt="" />
                    </div>
                    <div>
                        <span class="spotlight_badge">Кабинет продавца</span>
                        <h3>Панель управления и аналитика продаж</h3>
                        <p>Товары, заказы покупателей, выручка и показатели магазина</p>
                    </div>
                </div>
                <span class="spotlight_cta">В панель продавца →</span>
            </RouterLink>

            <RouterLink to="/delivery" class="bento_tile span_2 peach">
                <img src="/Коробка_доставка.png" alt="" />
                <div>
                    <h3>Доставка и заказы</h3>
                    <p>Отслеживание посылок и статус получения</p>
                </div>
            </RouterLink>

            <RouterLink to="/reviews" class="bento_tile span_2 lavender">
                <img src="/Покупки.png" alt="" />
                <div>
                    <h3>Покупки и отзывы</h3>
                    <p>История заказов и оценки товаров</p>
                </div>
            </RouterLink>

            <RouterLink to="/favorites" class="bento_tile">
                <img src="/Избранное.png" alt="" />
                <h3>Избранное</h3>
            </RouterLink>

            <RouterLink to="/messenger" class="bento_tile">
                <img src="/Поддержка.png" alt="" />
                <h3>Мессенджер</h3>
            </RouterLink>

            <a href="#" class="bento_tile span_2" @click.prevent>
                <img src="/Работа.png" alt="" />
                <div>
                    <h3>Работа на складе и в ПВЗ</h3>
                    <p>Вакансии в пунктах выдачи и логистике</p>
                </div>
            </a>
        </section>

        <div v-if="isVendor" class="bottom_zone">
            <button class="stop_vendor_link" @click="stopVendor">Перестать быть продавцом</button>
        </div>

        <SupportChat />
    </div>
</template>

<style scoped>
.bento_wrapper {
    max-width: 1020px;
    margin: 24px auto;
    padding: 0 18px 80px;
    display: flex;
    flex-direction: column;
    gap: 22px;
    font-family: system-ui, -apple-system, sans-serif;
}

.hero_banner {
    background: linear-gradient(120deg, #ff7e5f 0%, #feb47b 100%);
    border-radius: 24px;
    padding: 28px 32px;
    color: #fff;
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 20px;
    flex-wrap: wrap;
}

.hero_left {
    display: flex;
    align-items: center;
    gap: 18px;
}

.hero_avatar,
.hero_logo {
    width: 68px;
    height: 68px;
    border-radius: 20px;
    flex-shrink: 0;
}

.hero_avatar {
    background: rgba(255, 255, 255, 0.25);
    backdrop-filter: blur(6px);
    font-size: 28px;
    font-weight: 800;
    display: flex;
    align-items: center;
    justify-content: center;
}

.hero_logo {
    object-fit: cover;
    background: #fff;
    padding: 4px;
}

.hero_left h1 {
    margin: 0 0 6px;
    font-size: 26px;
}

.hero_left p {
    margin: 0;
    opacity: 0.95;
    font-size: 14px;
    display: flex;
    align-items: center;
    gap: 8px;
    flex-wrap: wrap;
}

.rating_chip {
    background: rgba(255, 255, 255, 0.25);
    padding: 2px 10px;
    border-radius: 12px;
    font-weight: 700;
    font-size: 13px;
}

.hero_actions {
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
}

.glass_btn {
    padding: 11px 18px;
    border-radius: 14px;
    border: 1px solid rgba(255, 255, 255, 0.45);
    background: rgba(255, 255, 255, 0.2);
    color: #fff;
    font-weight: 600;
    font-size: 14px;
    cursor: pointer;
    transition: background 0.2s;
}

.glass_btn:hover {
    background: rgba(255, 255, 255, 0.35);
}

.glass_btn.dark {
    background: rgba(0, 0, 0, 0.18);
    border-color: transparent;
}

.bento_edit_panel {
    background: #fff;
    border: 1px solid #eee8f2;
    border-radius: 22px;
    padding: 26px;
}

.bento_edit_panel h2 {
    margin: 0 0 18px;
    font-size: 19px;
    color: #1f2937;
}

.bento_form {
    display: flex;
    flex-direction: column;
    gap: 16px;
}

.inputs_grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: 14px;
}

.field {
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.field span {
    font-size: 13px;
    font-weight: 600;
    color: #4b5563;
}

.bento_form input,
.bento_form textarea {
    padding: 12px 16px;
    border-radius: 14px;
    border: 1px solid #ddd6e4;
    font-size: 15px;
    font-family: inherit;
    outline: none;
    transition: border-color 0.2s;
}

.bento_form input:focus,
.bento_form textarea:focus {
    border-color: #ff7e5f;
}

.save_bento_btn {
    align-self: flex-start;
    padding: 13px 28px;
    border-radius: 14px;
    border: none;
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    color: #fff;
    font-size: 15px;
    font-weight: 600;
    cursor: pointer;
}

.bento_grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 18px;
}

.bento_tile {
    background: #ffffff;
    border: 1px solid #eee8f2;
    border-radius: 24px;
    padding: 24px;
    text-decoration: none;
    color: #1f2937;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    justify-content: center;
    gap: 14px;
    transition: transform 0.2s, box-shadow 0.2s;
}

.bento_tile:hover {
    transform: translateY(-3px);
    box-shadow: 0 10px 24px rgba(0, 0, 0, 0.06);
}

.bento_tile img {
    width: 64px;
    height: 64px;
    object-fit: contain;
    flex-shrink: 0;
}

.bento_tile h3 {
    margin: 0;
    font-size: 17px;
}

.bento_tile p {
    margin: 6px 0 0;
    font-size: 13px;
    color: #6b7280;
}

.bento_tile.span_2 {
    grid-column: span 2;
    flex-direction: row;
    align-items: center;
    text-align: left;
    justify-content: flex-start;
    gap: 20px;
}

.bento_tile.span_2 img {
    width: 72px;
    height: 72px;
}

.bento_tile.span_4 {
    grid-column: span 4;
}

.vendor_spotlight {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    text-align: left;
    padding: 24px 28px;
    background: linear-gradient(135deg, #fff6ee 0%, #ffebd9 100%);
    border: 1px solid #ffd4b2;
}

.vendor_spotlight.active_vendor {
    background: linear-gradient(135deg, #f4f0fa 0%, #fff4ec 100%);
    border-color: #e3d6f5;
}

.spotlight_left {
    display: flex;
    align-items: center;
    gap: 20px;
}

.spotlight_icon_wrap {
    width: 76px;
    height: 76px;
    border-radius: 20px;
    background: #ffffff;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    box-shadow: 0 6px 16px rgba(255, 126, 95, 0.14);
}

.spotlight_icon_wrap img {
    width: 56px;
    height: 56px;
}

.spotlight_badge {
    display: inline-block;
    font-size: 12px;
    font-weight: 700;
    color: #ea580c;
    background: rgba(255, 126, 95, 0.15);
    padding: 3px 10px;
    border-radius: 10px;
    margin-bottom: 6px;
}

.vendor_spotlight h3 {
    font-size: 19px;
    color: #111827;
}

.spotlight_cta {
    padding: 13px 22px;
    border-radius: 16px;
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    color: #ffffff;
    font-weight: 700;
    font-size: 14px;
    white-space: nowrap;
    box-shadow: 0 6px 16px rgba(255, 126, 95, 0.25);
    transition: transform 0.2s;
}

.vendor_spotlight:hover .spotlight_cta {
    transform: translateX(3px);
}

.bento_tile.peach {
    background: #fff7f2;
    border-color: #ffe2d1;
}

.bento_tile.lavender {
    background: #f5f0fa;
    border-color: #e6dcf0;
}

.bottom_zone {
    text-align: center;
    margin-top: 8px;
}

.stop_vendor_link {
    background: none;
    border: none;
    color: #9ca3af;
    text-decoration: underline;
    cursor: pointer;
    font-size: 13px;
}

@media (max-width: 820px) {
    .bento_grid {
        grid-template-columns: repeat(2, 1fr);
    }

    .bento_tile.span_4 {
        grid-column: span 2;
    }

    .vendor_spotlight {
        flex-direction: column;
        align-items: flex-start;
        gap: 18px;
    }

    .spotlight_cta {
        width: 100%;
        text-align: center;
        box-sizing: border-box;
    }

    .bento_tile img {
        width: 58px;
        height: 58px;
    }

    .bento_tile.span_2 img {
        width: 64px;
        height: 64px;
    }
}

@media (max-width: 520px) {
    .hero_banner {
        padding: 20px;
    }

    .hero_actions,
    .glass_btn,
    .save_bento_btn {
        width: 100%;
    }

    .bento_grid {
        grid-template-columns: 1fr;
    }

    .bento_tile.span_2,
    .bento_tile.span_4 {
        grid-column: span 1;
    }

    .bento_tile {
        flex-direction: row;
        text-align: left;
        justify-content: flex-start;
        padding: 18px 20px;
    }

    .vendor_spotlight {
        flex-direction: column;
        align-items: flex-start;
    }

    .spotlight_left {
        gap: 14px;
    }

    .spotlight_icon_wrap {
        width: 62px;
        height: 62px;
    }

    .spotlight_icon_wrap img {
        width: 46px;
        height: 46px;
    }

    .bento_tile img,
    .bento_tile.span_2 img {
        width: 54px;
        height: 54px;
    }
}
</style>