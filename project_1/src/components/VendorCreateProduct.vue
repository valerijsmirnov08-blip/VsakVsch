<script setup>
import {ref, computed, watch, onMounted} from 'vue';
import { useRouter } from 'vue-router';

const categoryId = ref(1);
const subCategory = ref('Смартфон');

const brand = ref('');
const model = ref('');
const price = ref(0);
const quantity = ref(0);
const description = ref('');
const warranty = ref('');
const vendorSku = ref('');

const mainImage = ref(null);
const mainImagePreview = ref(null);
const isDragover = ref(false);

const gallery = ref('');
const galleryPreview = ref([]);
const isGalleryDragover = ref(false)

const router = useRouter();
const currentVendorId = ref(null);

onMounted(() =>
{
    const sessionStr = localStorage.getItem('user_session');
    
    if (!sessionStr) {
        alert("Для создания товара необходимо авторизоваться!");
        router.push("/accaunt");
        return;
    }
    const sessionData = JSON.parse(sessionStr);
    currentVendorId.value = sessionData.id;
});

const catalogStructure = 
{
    1:
    { 
        name: 'Электроника', 
        subcategories: ['Смартфон', 'Ноутбук'] 
    },
    2:
    { 
        name: 'Одежда', 
        subcategories: ['Футболка', 'Джинсы', 'Кофта'] 
    }
}
const specsCatalog = 
{
    'Смартфон':
    [
        { key: 'memory', label: 'Встроенная память', type: 'select', options: ['64 ГБ', '128 ГБ', '256 ГБ'] },
        { key: 'os', label: 'Операционная система', type: 'text', placeholder: 'Например: Android' }
    ],
    'Футболка':
    [
        { key: 'size', label: 'Размер', type: 'select', options: ['S', 'M', 'L', 'XL'] },
        { key: 'material', label: 'Материал', type: 'text', placeholder: 'Например: 100% хлопок' }
    ],
    'Кофта':
    [
        {key: 'size', label: 'Размер', type: 'select', options: ['S', 'M', 'L', 'XL'] },
        {key: 'material', label: 'Материал', type: 'text', placeholder: 'Например: 100% хлопок' }
    ]
};

const dynamicSpecs = ref({});

const currentFields = computed(() =>
{
    return specsCatalog[subCategory.value] || [];
})
watch(subCategory, () =>
{
    dynamicSpecs.value = {};
});
const availableSubcategories = computed(() =>
{
    return catalogStructure[categoryId.value].subcategories;
})
watch(categoryId, () =>
{
    subCategory.value = availableSubcategories.value[0];
})

const handleMainImageDrop = (event) =>
{
    isDragover.value = false;
    const file = event.dataTransfer.files[0];
    
    if(file)
{
    mainImage.value = file;
    mainImagePreview.value = URL.createObjectURL(file);
}
}
const handleGalleryDrop = (event) =>
{
    isGalleryDragover.value = false;
    const filesList = event.dataTransfer.files;

    if(filesList && filesList.length > 0)
    {
        const files = Array.from(filesList);
        gallery.value = files.map(file => file.name).join(', ');
        galleryPreview.value = files.map(file => URL.createObjectURL(file));
    }
}


const selectedBaseColor = ref('Черный');
const customHexColor = ref('111111');
const customColorName = ref('');

const baseColorPalette = 
[
    { name: 'Черный', hex: '#111111' },
    { name: 'Белый', hex: '#ffffff' },
    { name: 'Красный', hex: '#dc2626' },
    { name: 'Синий', hex: '#2563eb' },
    { name: 'Зеленый', hex: '#16a34a' }
]
const selectPaletteColor = (colorObj) =>
{   
    selectedBaseColor.value = colorObj.name;
    customHexColor.value = colorObj.hex;
}

const customSpecs = ref([]);

const addCustomSpec = () =>
{
    customSpecs.value.push({name: '', value: ''})
}
const removeCustomSpec = (index) =>
{
    customSpecs.value.splice(index, 1)
}
const submitProduct = async () => {

    if (!brand.value.trim() || !model.value.trim() || price.value <= 0) {
        alert("Заполните обязательные поля: Бренд, Модель и укажите корректную цену.");
        return;
    }
    
    if (!mainImage.value) {
        alert("Пожалуйста, загрузите главное фото товара.");
        return;
    }
    const payload = {
        brand: brand.value,
        model: model.value,
        price: Number(price.value),
        quantity: Number(quantity.value),
        vendor: { id: currentVendorId.value },
        category: { id: categoryId.value },
        warranty: warranty.value,
        description: description.value,
        details: {
            mainImage: mainImage.value ? mainImage.value.name : '',
            vendorSku: vendorSku.value,
            gallery: gallery.value,
            color: selectedBaseColor.value,
            colorHex: customHexColor.value,
            colorCustomName: customColorName.value,
            ...dynamicSpecs.value,
            ...Object.fromEntries(customSpecs.value.map(s => [s.name, s.value]).filter(s => s[0] !== ''))   
        }
    };

    try {
        const response = await fetch('http://localhost:8080/api/products/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("Товар успешно отправлен в базу!");
        } else {
            alert("Ошибка бэкенда при создании");
        }
    } catch (error) {
        console.error("Ошибка сети:", error);
    }
};


</script>
<template>
<div class="page_wrapper">
        <div class="header-nav">
            <h1>Конструктор карточки товара</h1>
        </div>


    <form @submit.prevent="submitProduct" class="mock_product_card">
        <div class="card_top">
            <div class="card_media">
                <label class="section_label">Главное фото *</label>
                <div class="main_dropzone"
                @dragover.prevent="isDragover = true"
                @dragleave="isDragover = false"
                @drop.prevent="handleMainImageDrop">
                    <div v-if="!mainImagePreview" class="empty_state">
                        <p>Перетащите главное фото</p>
                    </div>
                    <img v-else :src="mainImagePreview" class="preview_main">
                </div>
                    <label class="section_label mt-20" >Дополнительные фото *</label>
                    <div
                    class="gallery_dropzone"
                    :class="{dragover: isGalleryDragover}"
                    @dragover.prevent="isGalleryDragover = true"
                    @dragleave.prevent="isGalleryDragover = false"
                    @drop.prevent="handleGalleryDrop">

                    <div v-if="galleryPreview.length === 0" class="empty_state small">
                        <p>Перетащите доп. фото</p>
                    </div>
                    <div v-else class="gallery_grid">
                        <img v-for="(src, idx) in galleryPreview" :key="idx" :src="src" class="preview_micro">
                    </div>

                    </div>
                
            </div>
            <div class="card_info">
                <div class="info_row">
                     <div class="info_group half">
                        <label>Глобальная категория</label>
                        <select v-model.number="categoryId" class="mega_input select">
                            <option :value="1">Электроника</option>
                            <option :value="2">Одежда</option>
                        </select>
                     </div>
                     <div class="info_group half">
                        <label>Тип товара</label>
                        <select v-model="subCategory" class="mega_input select">
                            <option v-for="sub in availableSubcategories" :key="sub" :value="sub">
                                {{ sub }}
                            </option>
                        </select>
                     </div>
                </div>
                <div class="info_group">
                        <label>Цена для покупателя (₽) *</label>
                        <input v-model="price" type="text" class="mega_input price_input" placeholder="0">
                </div>
                <div class="info_group">
                    <label>Название / Модель товара *</label>
                    <input v-model="model" type="text" class="mega_input title_input" placeholder="Например: Смартфон iPhone 15">
                </div>
                <div class="info_row">
                    <div class="info_group third">
                        <label>Бренд *</label>
                        <input v-model="brand" type="text" class="mega_input" placeholder="Например: Apple">
                    </div>
                    <div class="info_group third">
                        <label>Остаток на складе (шт)</label>
                        <input v-model="quantity" type="number" class="mega_input" placeholder="0">
                    </div>
                    <div class="info_group third">
                        <label>Артикул</label>
                        <input v-model="vendorSku" type="text" class="mega_input" placeholder="Придумайте свой уникапльный артикул">
                    </div>
                    <div class="info_group third">
                        <label>Срок гарантии</label>
                        <select v-model="warranty" class="mega_input select">
                            <option>Нет</option>
                            <option>1 месяц</option>
                            <option>6 месяцов</option>
                            <option>1 год</option>
                        </select>
                    </div>
                </div>
                <div class="color_picker_block">
                    <label class="color_block_label">
                        Цвет товара: <span class="selected_color_text">{{ selectedBaseColor }}</span>
                    </label>
                    <div class="palatte_row">
                        <button
                        v-for="i in baseColorPalette"
                        :key="i.name"
                        type="button"
                        class="color_swatch"
                        :class="{active: selectedBaseColor === i.name}"
                        :style="{backgroundColor: i.hex}"
                        :title="i.name"
                        @click="selectPaletteColor(i)">
                        </button>
                        <label class="custom_color_wheel" title="Выбрать точный оттенок">
                            <input v-model="customHexColor" type="color">
                            <span class="wheel_icon" :style="{backgroundColor: customHexColor}">🎨</span>
                        </label>
                    </div>
                    <div class="color_extra_row">
                        <div class="color_preview_badge" :style="{backgroundColor: customHexColor}"></div>
                        <input v-model="customColorName" type="text" class="mega_input small_input" placeholder="Уточните оттенок (например: Изумрудный)">
                    </div>
                    
                </div>
            </div>
        </div>
        <div class="card_bottom">
            <div class="specs_header">
                <h3 class="section_title">Характеристики: {{ subCategory }}</h3>
            </div>
            <div class="dynamic_spec_grid">
                <div v-for="field in currentFields":key="field.key" class="spec_input_group">
                    <label>{{ field.label }}</label>
                    <select v-if="field.type === 'select'" v-model="dynamicSpecs[field.key]" class="mega_input spec">
                        <option value="" disabled selected>Выберите из списка</option>
                        <option v-for="opt in field.options" :key="opt" :value="opt">{{ opt }}</option>
                    </select>
                    <input v-else :type="field.type" v-model="dynamicSpecs[field.key]" :placeholder="field.placeholder" class="mega_input spec">
                </div>
            </div>
            <div class="custom_specs_section">
                <div class="custom_specs_header">
                    <h4>Дополнительные характеристики (по желанию)</h4>
                    <button @click="addCustomSpec" type="button" class="btn_add_spec">+ Добавить параметр</button>
                </div>
                <div v-for="(spec, idx) in customSpecs" :key="idx" class="custom_spec_row">
                    <input v-model="spec.name" type="text" class="mega_input spec" placeholder="Название (напр. Страна производства)">
                    <input v-model="spec.value" type="text" class="mega_input spec" placeholder="Значение (напр. Китай)">
                    <button @click="removeCustomSpec(idx)" type="button" class="btn_remove_spec">✕</button>
                </div>
                <div class="info_group">
                    <label style="font-size: 18px; font-weight: 800; color: #111;">Полное описание товара</label>
                    <textarea v-model="description" class="mega_input textarea" placeholder="Подробно расскажите о преимуществах, особенностях использования и комплектации товара"></textarea>
                </div>
            </div>
        </div>

        <div class="submit_zone">
            <button class="btn_publish" type="submit">Опубликовать карточку товара</button>
        </div>
    </form>
</div>
</template>
<style scoped>
.page_wrapper
{
    min-height: 100vh;
    background:#f4f6f8;
    padding: 50px 10px;
    font-family: system-ui, -apple-system, sans-serif;
    display: flex;
    flex-direction: column;
    align-items: center;
}
.header_nav
{
    width: 100%;
    max-width: 1300px;
    margin-bottom: 30px;
}
.header_nav h1
{
    font-size: 32px;
    color: #111;
    margin: 0;
    font-weight: 700;
}
.mock_product_card
{
    width: 100%;
    max-width: 1300px;
    background: #fff;
    border-radius: 32px;
    box-shadow: 0 16px 40px rgba(0, 0, 0, 0.08);
    overflow: hidden;
    display: flex;
    flex-direction: column;
}
.card_top
{
    display: flex;
    padding: 50px;
    gap: 60px;
    border-bottom: 1px solid #eaeaea;
}
.card_media
{
    flex: 0 0 450px;
    display: flex;
    flex-direction: column;
}
.section_label
{
    font-size: 15px;
    font-weight: 700;
    color: #444;
    margin-bottom: 12px;
    text-transform: uppercase;
    letter-spacing: 0.8px;
}
.mt-20
{
    margin-top: 20px;
}
.main_dropzone
{
    height: 450px;
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
.gallery_dropzone
{
    height: 120px;
    background: #f9f9fb;
    border: 3px dashed #d0d4d9;
    border-radius: 16px;
    display: flex;
    align-items: center;
    padding: 15px;
    overflow-x: auto;
}
.dragover
{
    border-color: #4caf50;
    background: #e8f5e9;
}
.empty_state
{
    text-align: center;
    color: #888;
    font-weight: 500;
}
.empty_state.small p
{
    font-size: 14px;
}
.preview_main
{
    width: 100%;
    height: 100%;
    object-fit: contain;
}
.gallery_grid
{
    display: flex;
    gap: 12px;
}
.preview_micro
{
    width: 90px;
    height: 90px;
    object-fit: cover;
    border-radius: 12px;
    border: 1px solid #ddd;
}
.card_info
{
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 24px;
}
.info_group
{
    display: flex;
    flex-direction: column;
    gap: 8px;
}
.info_row
{
    display: flex;
    gap: 20px;
}
.info_row .half
{
    flex: 1;
}
.info_group label
{
    font-size: 15px;
    font-weight: 700;
    color: #555;
}
.mega_input
{
    padding: 16px 20px;
    border-radius: 16px;
    border: 1px solid #e0e4e8;
    background: #fbfbfc;
    font-size: 16px;
    color: #222;
    transition: 0.2s;
    appearance: auto;
}
.mega_input:focus
{
    border-color: #4caf50;
    background: #fff;
    outline: none;
    box-shadow: 0 0 0 4px rgba(76, 175, 80, 0.15);
}
.price_input
{
    font-size: 28px;
    font-weight: 800;
    color: #111;
}
.title_input
{
    font-size: 20px;
    font-weight: 700;
}
.color_picker_block
{
    background: #f8fafc;
    border: 2px solid #e2e8f0;
    border-radius: 20px;
    padding: 20px;
    display: flex;
    flex-direction: column;
    gap: 14px;
}
.color_block_label
{
    font-size: 15px;
    font-weight: 700;
    color: #555;
}
.palatte_row
{
    display: flex;
    align-items: center;
    gap:12px;
}
.color_swatch
{
    width: 38px;
    height: 38px;
    border-radius: 50%;
    border: 2px solid #cbd5e1;
    cursor: pointer;
    transition: transform 0.15s;
}
.color_swatch.active
{
    border-color: #4caf50;
    box-shadow: 0 0 0 4px rgba(76, 175, 80, 0.35);
    transform: scale(1.12);
}
.custom_color_wheel
{
    position: relative;
    cursor: pointer;
    display: flex;
    align-items: center;
}
.custom_color_wheel input[type="color"]
{
    position: absolute;
    opacity: 0;
    width: 42px;
    height: 42px;
    cursor: pointer;
}
.wheel_icon
{
    width: 42px;
    height: 42px;
    border-radius: 50%;
    border: 2px dashed #64748b;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
}
.color_extra_row
{
    display: flex;
    align-items: center;
    gap: 14px;
}
.color_preview_badge
{
    width: 48px;
    height: 48px;
    border-radius: 12px;
    border: 2px solid #cbd5e1;
    flex-shrink: 0;
}
.small_input
{
    flex: 1;
    padding: 12px 16px;
}
.card_bottom
{
    padding: 50px;
    background: #fcfcfd;
}
.section_title
{
    margin: 0;
    font-size: 24px;
    color: #111;
    font-weight: 800;
}
.specs_header
{
    margin-bottom: 30px;
}
.dynamic_spec_grid
{
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
    gap: 24px;
}
.spec_input_group label
{
    font-size: 15px;
    font-weight: 700;
    color: #555;
    margin-bottom: 10px;
    display: block;
}
.spec
{
    width: 100%;
    box-sizing: border-box;
}
.custom_specs_section
{
    margin-top: 35px;
    padding-top: 25px;
    border-top: 2px dashed #e2e8f0;
}
.custom_specs_header
{
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
}
.btn_add_spec
{
    background: #e8f5e9;
    color: #2e7d32;
    border: 1px solid #2e7d32;
    padding: 10px 20px;
    border-radius: 12px;
    font-weight: 700;
    cursor: pointer;
    transition: 0.2s;
}
.btn_add_spec:hover
{
    background: #c8e6c9;
}
.custom_spec_row
{
    display: flex;
    gap:15px;
    margin-bottom: 15px;
    align-items: center;

}
.btn_remove_spec
{
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
.textarea
{
    height: 160px;
    resize: vertical;
    line-height: 1.6;
    font-family: inherit;
}
.submit_zone
{
    padding: 40px 50px;
    background: #fff;
    border-top: 1px solid #eaeaea;
    display: flex;
    justify-content: center;
}
.btn_publish
{
    background: linear-gradient(135deg, #4caf50, #2e7d32);
    color: #fff;
    border: none;
    padding: 22px 80px;
    border-radius: 20px;
    font-size: 22px;
    font-weight: 700;
    cursor: pointer;
}
.btn_publish:hover
{
    transform: translateY(-3px);
    box-shadow: 0 12px 28px rgba(76, 175, 80, 0.35);
}
@media(max-width: 1000px)
{   
    .card_top
    {
        flex-direction: column;
    }
    .card_media
    {
        flex: none;
    }
}
</style>