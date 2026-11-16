<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { useRouter, RouterLink } from 'vue-router';

const router = useRouter();

const brand = ref('');
const model = ref('');
const price = ref(0);
const quantity = ref(0);
const vendorSku = ref('');
const description = ref('');
const warranty = ref('1 год');
const estimatedDelivery = ref('Завтра');

const categoryId = ref(1);
const subCategory = ref('Смартфон');

const selectedBaseColor = ref('Черный');
const customHexColor = ref('#111111');
const customColorName = ref('');

const baseColorPalette = [
    { name: 'Черный', hex: '#111111' },
    { name: 'Белый', hex: '#ffffff' },
    { name: 'Серый', hex: '#888888' },
    { name: 'Серебристый', hex: '#d1d5db' },
    { name: 'Бежевый', hex: '#e5d3b3' },
    { name: 'Синий', hex: '#2563eb' },
    { name: 'Голубой', hex: '#38bdf8' },
    { name: 'Красный', hex: '#dc2626' },
    { name: 'Розовый', hex: '#f472b6' },
    { name: 'Зеленый', hex: '#16a34a' },
    { name: 'Желтый', hex: '#eab308' },
    { name: 'Фиолетовый', hex: '#9333ea' },
    { name: 'Коричневый', hex: '#78350f' }
];

const selectPaletteColor = (colorObj) => {
    selectedBaseColor.value = colorObj.name;
    customHexColor.value = colorObj.hex;
};

const mainImage = ref('');
const gallery = ref('');
const isMainDragover = ref(false);
const isGalleryDragover = ref(false);
const mainImagePreview = ref(null);
const galleryPreviews = ref([]);

const errorMessage = ref('');
const successMessage = ref('');
const currentVendorId = ref(null);

const catalogStructure = {
    1: {
        name: 'Электроника и техника',
        subcategories: {
            'Смартфон': [
                { key: 'memory', label: 'Встроенная память', type: 'select', options: ['64 ГБ', '128 ГБ', '256 ГБ', '512 ГБ', '1 ТБ'] },
                { key: 'ram', label: 'Оперативная память', type: 'select', options: ['4 ГБ', '6 ГБ', '8 ГБ', '12 ГБ', '16 ГБ'] },
                { key: 'screen', label: 'Диагональ экрана (дюймы)', type: 'text', placeholder: 'Например: 6.7"' },
                { key: 'os', label: 'Операционная система', type: 'select', options: ['iOS', 'Android', 'HarmonyOS', 'Другая'] },
                { key: 'battery', label: 'Емкость аккумулятора (мАч)', type: 'number', placeholder: '5000' }
            ],
            'Ноутбук / ПК': [
                { key: 'cpu', label: 'Процессор', type: 'text', placeholder: 'Например: Intel Core i5-12450H / Apple M3' },
                { key: 'ram', label: 'Оперативная память', type: 'select', options: ['8 ГБ', '16 ГБ', '32 ГБ', '64 ГБ'] },
                { key: 'ssd', label: 'Объем накопителя SSD', type: 'select', options: ['256 ГБ', '512 ГБ', '1 ТБ', '2 ТБ'] },
                { key: 'gpu', label: 'Видеокарта', type: 'text', placeholder: 'Например: RTX 4060 / Встроенная' },
                { key: 'screen', label: 'Экран и частота', type: 'text', placeholder: 'Например: 15.6" IPS 144 Гц' }
            ],
            'Бытовая техника': [
                { key: 'power', label: 'Мощность (Вт)', type: 'number', placeholder: '2000' },
                { key: 'power_type', label: 'Тип питания', type: 'select', options: ['От сети 220В', 'От аккумулятора', 'Комбинированное'] },
                { key: 'noise', label: 'Уровень шума (дБ)', type: 'number', placeholder: '65' },
                { key: 'material', label: 'Материал корпуса', type: 'text', placeholder: 'Пластик, нерж. сталь' }
            ],
            'Аудио и Аксессуары': [
                { key: 'connection', label: 'Тип подключения', type: 'select', options: ['Беспроводное (Bluetooth)', 'Проводное', 'Радиоканал 2.4 ГГц'] },
                { key: 'connector', label: 'Разъем', type: 'select', options: ['USB Type-C', 'Lightning', 'Jack 3.5 мм', 'USB-A', 'Нет'] },
                { key: 'work_time', label: 'Время автономной работы (ч)', type: 'number', placeholder: '24' }
            ]
        }
    },
    2: {
        name: 'Одежда и обувь',
        subcategories: {
            'Плечевая одежда (Худи, Футболки, Куртки)': [
                { key: 'size', label: 'Российский размер', type: 'select', options: ['42 (XS)', '44 (S)', '46 (M)', '48 (L)', '50 (XL)', '52 (XXL)', 'Оверсайз'] },
                { key: 'gender', label: 'Пол', type: 'select', options: ['Мужской', 'Женский', 'Унисекс'] },
                { key: 'season', label: 'Сезон', type: 'select', options: ['Лето', 'Демисезон', 'Зима', 'Круглогодичный'] },
                { key: 'material', label: 'Состав ткани', type: 'text', placeholder: 'Например: 80% хлопок, 20% полиэстер' },
                { key: 'cut', label: 'Покрой / Посадка', type: 'select', options: ['Свободный (Oversize)', 'Прямой', 'Приталенный'] }
            ],
            'Брюки и Джинсы': [
                { key: 'size', label: 'Размер (W/L или РФ)', type: 'text', placeholder: 'Например: 48 или W32/L34' },
                { key: 'gender', label: 'Пол', type: 'select', options: ['Мужской', 'Женский', 'Унисекс'] },
                { key: 'material', label: 'Состав ткани', type: 'text', placeholder: 'Например: 98% хлопок, 2% эластан' },
                { key: 'rise', label: 'Посадка', type: 'select', options: ['Высокая', 'Средняя', 'Низкая'] }
            ],
            'Обувь': [
                { key: 'shoe_size', label: 'Размер обуви (EU/RU)', type: 'select', options: ['36', '37', '38', '39', '40', '41', '42', '43', '44', '45'] },
                { key: 'insole_length', label: 'Длина стельки (см)', type: 'number', placeholder: '27.5' },
                { key: 'upper_material', label: 'Материал верха', type: 'select', options: ['Натуральная кожа', 'Экокожа', 'Текстиль / Сетка', 'Замша'] },
                { key: 'sole_material', label: 'Материал подошвы', type: 'select', options: ['Резина', 'ЭВА (EVA)', 'ТПУ', 'Полиуретан'] },
                { key: 'season', label: 'Сезон', type: 'select', options: ['Лето', 'Демисезон', 'Зима'] }
            ]
        }
    },
    3: {
        name: 'Дом и сад',
        subcategories: {
            'Мебель': [
                { key: 'dimensions', label: 'Габариты ШхГхВ (см)', type: 'text', placeholder: 'Например: 120x60x75 см' },
                { key: 'material', label: 'Материал каркаса/фасада', type: 'text', placeholder: 'ЛДСП, массив дуба, металл' },
                { key: 'max_load', label: 'Макс. нагрузка (кг)', type: 'number', placeholder: '120' },
                { key: 'assembly', label: 'Требуется сборка', type: 'select', options: ['Да (в разобранном виде)', 'Нет (в собранном виде)'] }
            ],
            'Посуда и Кухня': [
                { key: 'volume', label: 'Объем (л / мл)', type: 'text', placeholder: 'Например: 500 мл или 2.5 л' },
                { key: 'material', label: 'Материал', type: 'select', options: ['Керамика', 'Фарфор', 'Стекло', 'Нержавеющая сталь', 'Чугун', 'Пластик'] },
                { key: 'dishwasher', label: 'Можно мыть в посудомойке', type: 'select', options: ['Да', 'Нет'] }
            ],
            'Текстиль и Декор': [
                { key: 'dimensions', label: 'Размер (ДхШ)', type: 'text', placeholder: 'Например: 200x220 см' },
                { key: 'material', label: 'Материал', type: 'text', placeholder: 'Сатин, бязь, велюр' },
                { key: 'weight', label: 'Вес товара (кг)', type: 'number', placeholder: '1.2' }
            ]
        }
    }
};

const availableSubcategories = computed(() => {
    const cat = catalogStructure[categoryId.value];
    return cat ? Object.keys(cat.subcategories) : [];
});

const currentCategoryFields = computed(() => {
    const cat = catalogStructure[categoryId.value];
    if (!cat || !cat.subcategories[subCategory.value]) return [];
    return cat.subcategories[subCategory.value];
});

const dynamicSpecs = reactive({});

const customSpecs = ref([]);
const addCustomSpec = () => {
    customSpecs.value.push({ name: '', value: '' });
};
const removeCustomSpec = (idx) => {
    customSpecs.value.splice(idx, 1);
};

watch(categoryId, () => {
    const subs = availableSubcategories.value;
    subCategory.value = subs.length > 0 ? subs[0] : '';
    for (const key in dynamicSpecs) delete dynamicSpecs[key];
});

watch(subCategory, () => {
    for (const key in dynamicSpecs) delete dynamicSpecs[key];
});

onMounted(() => {
    const session = localStorage.getItem('user_session');
    if(!session) {
        alert("Пожалуйста, войдите в аккаунт");
        router.push("/accaunt");
        return;
    }
    currentVendorId.value = JSON.parse(session).id;
});

const handleMainImageDrop = (e) => {
    isMainDragover.value = false;
    const files = e.dataTransfer.files;
    if (files && files.length > 0) {
        const file = files[0];
        mainImage.value = `/${file.name}`;
        mainImagePreview.value = URL.createObjectURL(file);
    }
};

const handleGalleryDrop = (e) => {
    isGalleryDragover.value = false;
    const filesList = e.dataTransfer.files;
    if (filesList && filesList.length > 0) {
        const files = Array.from(filesList);
        gallery.value = files.map(file => `/${file.name}`).join(', ');
        galleryPreviews.value = files.map(file => URL.createObjectURL(file));
    }
};

const areBothImagesLoaded = computed(() => mainImagePreview.value !== null || mainImage.value.trim() !== '');

const handleCreateProduct = async () => {
    errorMessage.value = '';
    successMessage.value = '';

    if(!brand.value.trim() || !model.value.trim() || !vendorSku.value.trim()) {
        errorMessage.value = "Заполните обязательные поля (Бренд, Модель, Артикул)";
        return;
    }

    const activeSpecs = {};
    currentCategoryFields.value.forEach(field => {
        if (dynamicSpecs[field.key]) activeSpecs[field.key] = dynamicSpecs[field.key];
    });

    customSpecs.value.forEach(item => {
        if (item.name.trim() && item.value.trim()) {
            activeSpecs[item.name.trim()] = item.value.trim();
        }
    });

    try {
        const payload = {
            brand: brand.value.trim(),
            price: Number(price.value),
            quantity: Number(quantity.value),
            vendor: { id: currentVendorId.value },
            category: { id: Number(categoryId.value) },
            details: {
                subCategory: subCategory.value,
                model: model.value.trim(),
                vendorSku: vendorSku.value.trim(),
                description: description.value.trim(),
                warranty: warranty.value,
                estimatedDelivery: estimatedDelivery.value,
                color: selectedBaseColor.value,
                colorHex: customHexColor.value,
                colorCustomName: customColorName.value.trim() || selectedBaseColor.value,
                mainImage: mainImage.value.trim() || "/Шампунь.jpg",
                gallery: gallery.value.trim(),
                ...activeSpecs 
            }
        };

        const response = await fetch('http://localhost:8080/api/products/create', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        });

        if(response.ok) {
            successMessage.value = "Товар успешно добавлен на витрину!";
            setTimeout(() => router.push('/accaunt'), 1500);
        } else {
            const errText = await response.text();
            throw new Error(errText || "Ошибка при создании товара");
        }
    } catch(error) {
        errorMessage.value = error.message;
    }
};
</script>

<template>
    <div class="page-wrapper">
        <div class="header-nav">
            <RouterLink to="/accaunt">
                <button class="back_btn"><img src="/Назад.png" alt="Назад"></button>
            </RouterLink>
            <h1>Конструктор карточки товара</h1>
        </div>

        <form @submit.prevent="handleCreateProduct" class="mock-product-card">
            
            <div class="card-top">
                <div class="card-media">
                    <label class="section-label">Главное фото *</label>
                    <div class="main-dropzone"
                        :class="{ dragover: isMainDragover }"
                        @dragover.prevent="isMainDragover = true"
                        @dragleave.prevent="isMainDragover = false"
                        @drop.prevent="handleMainImageDrop">
                        
                        <div v-if="!mainImagePreview" class="empty-state">
                            <img src="/Корзина.png" alt="Upload">
                            <p>Перетащите главное фото</p>
                        </div>
                        <img v-else :src="mainImagePreview" class="preview-main">
                    </div>
                    <input v-model="mainImage" type="text" class="hidden-path" placeholder="Или укажите системный путь (напр. /img.jpg)">

                    <label class="section-label mt-20">Дополнительные фото (Галерея) *</label>
                    <div class="gallery-dropzone"
                        :class="{ dragover: isGalleryDragover }"
                        @dragover.prevent="isGalleryDragover = true"
                        @dragleave.prevent="isGalleryDragover = false"
                        @drop.prevent="handleGalleryDrop">
                        
                        <div v-if="galleryPreviews.length === 0" class="empty-state small">
                            <p>Перетащите доп. фото</p>
                        </div>
                        <div v-else class="gallery-grid">
                            <img v-for="(src, idx) in galleryPreviews" :key="idx" :src="src" class="preview-micro">
                        </div>
                    </div>
                </div>

                <div class="card-info">
                    <div class="info-row">
                        <div class="info-group half">
                            <label>Глобальная категория</label>
                            <select v-model.number="categoryId" class="mega-input select">
                                <option :value="1">Электроника и техника</option>
                                <option :value="2">Одежда и обувь</option>
                                <option :value="3">Дом и сад</option>
                            </select>
                        </div>
                        <div class="info-group half">
                            <label>Тип товара (Подкатегория)</label>
                            <select v-model="subCategory" class="mega-input select">
                                <option v-for="sub in availableSubcategories" :key="sub" :value="sub">
                                    {{ sub }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <div class="info-group price-group">
                        <label>Цена для покупателя (₽)</label>
                        <input v-model="price" type="number" class="mega-input price-input" placeholder="0">
                    </div>

                    <div class="info-group">
                        <label>Название / Модель товара *</label>
                        <input v-model="model" type="text" class="mega-input title-input" placeholder="Например: Смартфон iPhone 15 Pro">
                    </div>

                    <div class="info-row">
                        <div class="info-group half">
                            <label>Бренд *</label>
                            <input v-model="brand" type="text" class="mega-input" placeholder="Например: Apple">
                        </div>
                        <div class="info-group half">
                            <label>Артикул продавца (SKU) *</label>
                            <input v-model="vendorSku" type="text" class="mega-input" placeholder="SKU-12345">
                        </div>
                    </div>

                    <div class="color-picker-block">
                        <label class="color-block-label">
                            Цвет товара: <span class="selected-color-text">{{ selectedBaseColor }}</span>
                        </label>
                        
                        <div class="palette-row">
                            <button 
                                v-for="c in baseColorPalette" 
                                :key="c.name"
                                type="button"
                                class="color-swatch"
                                :class="{ active: selectedBaseColor === c.name }"
                                :style="{ backgroundColor: c.hex }"
                                :title="c.name"
                                @click="selectPaletteColor(c)">
                            </button>

                            <label class="custom-color-wheel" title="Выбрать точный оттенок на круге">
                                <input type="color" v-model="customHexColor">
                                <span class="wheel-icon" :style="{ backgroundColor: customHexColor }">🎨</span>
                            </label>
                        </div>

                        <div class="color-extra-row">
                            <div class="color-preview-badge" :style="{ backgroundColor: customHexColor }"></div>
                            <input 
                                v-model="customColorName" 
                                type="text" 
                                class="mega-input small-input" 
                                placeholder="Уточните оттенок (например: Титановый серый, Изумрудный)">
                        </div>
                    </div>

                    <div class="info-row">
                        <div class="info-group third">
                            <label>Остаток на складе (шт)</label>
                            <input v-model.number="quantity" type="number" class="mega-input" placeholder="100">
                        </div>
                        <div class="info-group third">
                            <label>Гарантия</label>
                            <select v-model="warranty" class="mega-input select">
                                <option value="Без гарантии">Без гарантии</option>
                                <option value="6 месяцев">6 месяцев</option>
                                <option value="1 год">1 год</option>
                                <option value="2 года">2 года</option>
                            </select>
                        </div>
                        <div class="info-group third">
                            <label>Срок доставки</label>
                            <input v-model="estimatedDelivery" type="text" class="mega-input" placeholder="Завтра / 2-3 дня">
                        </div>
                    </div>
                </div>
            </div>

            <div class="card-bottom">
                <div class="specs-header">
                    <h3 class="section-title">Характеристики: {{ subCategory }}</h3>
                    <span class="specs-subtitle">Набор полей адаптирован под выбранный тип товара</span>
                </div>
                
                <div class="dynamic-specs-grid">
                    <div v-for="field in currentCategoryFields" :key="field.key" class="spec-input-group">
                        <label>{{ field.label }}</label>
                        
                        <select v-if="field.type === 'select'" v-model="dynamicSpecs[field.key]" class="mega-input spec">
                            <option value="" disabled selected>Выберите из списка...</option>
                            <option v-for="opt in field.options" :key="opt" :value="opt">{{ opt }}</option>
                        </select>

                        <input v-else :type="field.type" v-model="dynamicSpecs[field.key]" :placeholder="field.placeholder" class="mega-input spec">
                    </div>
                </div>

                <div class="custom-specs-section">
                    <div class="custom-specs-header">
                        <h4>Дополнительные характеристики (по желанию)</h4>
                        <button type="button" class="btn-add-spec" @click="addCustomSpec">+ Добавить параметр</button>
                    </div>

                    <div v-for="(spec, idx) in customSpecs" :key="idx" class="custom-spec-row">
                        <input v-model="spec.name" type="text" class="mega-input spec" placeholder="Название (напр. Страна производства)">
                        <input v-model="spec.value" type="text" class="mega-input spec" placeholder="Значение (напр. Китай)">
                        <button type="button" class="btn-remove-spec" @click="removeCustomSpec(idx)">✕</button>
                    </div>
                </div>

                <div class="info-group mt-30">
                    <label>Полное описание товара</label>
                    <textarea v-model="description" class="mega-input textarea" placeholder="Подробно расскажите о преимуществах, особенностях использования и комплектации товара..."></textarea>
                </div>
            </div>

            <div class="submit-zone">
                <p v-if="errorMessage" class="msg error">{{ errorMessage }}</p>
                <p v-if="successMessage" class="msg success">{{ successMessage }}</p>
                
                <button type="submit" :disabled="!areBothImagesLoaded" class="btn-publish">
                    Опубликовать карточку товара
                </button>
            </div>
        </form>
    </div>
</template>

<style scoped>
.page-wrapper {
    min-height: 100vh;
    background: #f4f6f8;
    padding: 50px 10px;
    font-family: system-ui, -apple-system, sans-serif;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.header-nav {
    width: 100%;
    max-width: 1500px;
    display: flex;
    align-items: center;
    gap: 24px;
    margin-bottom: 30px;
}

.back_btn {
    background: #fff;
    border: 1px solid #dcdfe4;
    border-radius: 50%;
    width: 56px;
    height: 56px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    box-shadow: 0 4px 10px rgba(0,0,0,0.06);
    transition: 0.2s;
}
.back_btn:hover { background: #f0f0f0; }
.back_btn img { width: 24px; }
.header-nav h1 { font-size: 32px; color: #111; margin: 0; font-weight: 700; }

.mock-product-card {
    width: 100%;
    max-width: 1500px;
    background: #fff;
    border-radius: 32px;
    box-shadow: 0 16px 40px rgba(0,0,0,0.08);
    overflow: hidden;
    display: flex;
    flex-direction: column;
}

.card-top {
    display: flex;
    padding: 50px;
    gap: 60px;
    border-bottom: 1px solid #eaeaea;
}

.card-media {
    flex: 0 0 520px;
    display: flex;
    flex-direction: column;
}

.section-label {
    font-size: 15px;
    font-weight: 700;
    color: #444;
    margin-bottom: 12px;
    text-transform: uppercase;
    letter-spacing: 0.8px;
}

.mt-20 { margin-top: 20px; }
.mt-30 { margin-top: 30px; }

.main-dropzone {
    height: 560px;
    background: #f9f9fb;
    border: 3px dashed #d0d4d9;
    border-radius: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s;
    overflow: hidden;
    position: relative;
}

.gallery-dropzone {
    height: 140px;
    background: #f9f9fb;
    border: 3px dashed #d0d4d9;
    border-radius: 16px;
    display: flex;
    align-items: center;
    padding: 15px;
    overflow-x: auto;
}

.dragover {
    border-color: #4caf50;
    background: #e8f5e9;
}

.empty-state { text-align: center; color: #888; }
.empty-state img { width: 56px; opacity: 0.4; margin-bottom: 15px; }
.empty-state p { font-size: 18px; margin: 0; font-weight: 500; }
.empty-state.small p { font-size: 15px; }

.preview-main { width: 100%; height: 100%; object-fit: contain; }
.gallery-grid { display: flex; gap: 12px; }
.preview-micro {
    width: 100px;
    height: 100px;
    object-fit: cover;
    border-radius: 12px;
    border: 1px solid #ddd;
}

.hidden-path {
    font-size: 14px;
    color: #666;
    background: #f9f9fb;
    border: 1px solid #e0e4e8;
    border-radius: 10px;
    padding: 8px 12px;
    margin-top: 10px;
    outline: none;
    width: 100%;
    box-sizing: border-box;
}

.card-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 24px;
}

.info-group {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.info-row {
    display: flex;
    gap: 20px;
}
.info-row .half { flex: 1; }
.info-row .third { flex: 1; }

.info-group label {
    font-size: 15px;
    font-weight: 700;
    color: #555;
}

.mega-input {
    padding: 16px 20px;
    border-radius: 16px;
    border: 2px solid #e0e4e8;
    background: #fbfbfc;
    font-size: 18px;
    color: #222;
    transition: 0.2s;
    appearance: auto;
}
.mega-input:focus {
    border-color: #4caf50;
    background: #fff;
    outline: none;
    box-shadow: 0 0 0 4px rgba(76, 175, 80, 0.15);
}

.price-input {
    font-size: 34px;
    font-weight: 800;
    color: #111;
    padding: 12px 20px;
}
.title-input {
    font-size: 24px;
    font-weight: 700;
}

.color-picker-block {
    background: #f8fafc;
    border: 2px solid #e2e8f0;
    border-radius: 20px;
    padding: 20px;
    display: flex;
    flex-direction: column;
    gap: 14px;
}

.color-block-label {
    font-size: 15px;
    font-weight: 700;
    color: #555;
}
.selected-color-text {
    color: #111;
    font-weight: 800;
}

.palette-row {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 12px;
}

.color-swatch {
    width: 38px;
    height: 38px;
    border-radius: 50%;
    border: 2px solid #cbd5e1;
    cursor: pointer;
    transition: transform 0.15s, box-shadow 0.15s;
}
.color-swatch:hover {
    transform: scale(1.12);
}
.color-swatch.active {
    border-color: #4caf50;
    box-shadow: 0 0 0 4px rgba(76, 175, 80, 0.35);
    transform: scale(1.12);
}

.custom-color-wheel {
    position: relative;
    cursor: pointer;
    display: flex;
    align-items: center;
}
.custom-color-wheel input[type="color"] {
    position: absolute;
    opacity: 0;
    width: 42px;
    height: 42px;
    cursor: pointer;
}
.wheel-icon {
    width: 42px;
    height: 42px;
    border-radius: 50%;
    border: 2px dashed #64748b;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
}

.color-extra-row {
    display: flex;
    align-items: center;
    gap: 14px;
}
.color-preview-badge {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    border: 2px solid #cbd5e1;
    flex-shrink: 0;
}
.small-input {
    flex: 1;
    padding: 12px 16px;
    font-size: 16px;
}

.card-bottom {
    padding: 50px;
    background: #fcfcfd;
}

.specs-header {
    margin-bottom: 30px;
}
.section-title {
    margin: 0 0 6px 0;
    font-size: 26px;
    color: #111;
    font-weight: 800;
}
.specs-subtitle {
    font-size: 15px;
    color: #777;
}

.dynamic-specs-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
    gap: 28px;
}

.spec-input-group label {
    font-size: 15px;
    font-weight: 700;
    color: #555;
    margin-bottom: 10px;
    display: block;
}

.spec { padding: 16px 20px; font-size: 18px; width: 100%; box-sizing: border-box; }
.textarea { height: 180px; resize: vertical; line-height: 1.6; width: 100%; box-sizing: border-box; }

.custom-specs-section {
    margin-top: 35px;
    padding-top: 25px;
    border-top: 2px dashed #e2e8f0;
}
.custom-specs-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
}
.custom-specs-header h4 {
    margin: 0;
    font-size: 18px;
    color: #333;
}
.btn-add-spec {
    background: #e8f5e9;
    color: #2e7d32;
    border: 1px solid #a5d6a7;
    padding: 10px 20px;
    border-radius: 12px;
    font-size: 15px;
    font-weight: 700;
    cursor: pointer;
    transition: 0.2s;
}
.btn-add-spec:hover { background: #c8e6c9; }

.custom-spec-row {
    display: flex;
    gap: 15px;
    margin-bottom: 12px;
    align-items: center;
}
.btn-remove-spec {
    background: #fee2e2;
    color: #dc2626;
    border: none;
    width: 48px;
    height: 48px;
    border-radius: 12px;
    font-size: 18px;
    font-weight: 700;
    cursor: pointer;
    flex-shrink: 0;
}

.submit-zone {
    padding: 40px 50px;
    background: #fff;
    border-top: 1px solid #eaeaea;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 15px;
}

.btn-publish {
    background: linear-gradient(135deg, #4caf50, #2e7d32);
    color: #fff;
    border: none;
    padding: 22px 80px;
    border-radius: 20px;
    font-size: 22px;
    font-weight: 700;
    cursor: pointer;
    transition: 0.2s;
}
.btn-publish:hover:not(:disabled) {
    transform: translateY(-3px);
    box-shadow: 0 12px 28px rgba(76, 175, 80, 0.35);
}
.btn-publish:disabled {
    background: #e0e0e0;
    color: #999;
    cursor: not-allowed;
}

.msg { font-weight: 700; margin: 0; font-size: 16px; }
.error { color: #d32f2f; }
.success { color: #388e3c; }

@media (max-width: 1100px) {
    .card-top { flex-direction: column; }
    .card-media { flex: none; height: auto; }
    .main-dropzone { height: 400px; }
}
</style>