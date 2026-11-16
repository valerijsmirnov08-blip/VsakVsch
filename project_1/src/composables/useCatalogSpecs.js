import { ref, computed, watch } from "vue";

export function useCatalogSpecs() {
    const categoryId = ref(1);
    const subCategory = ref('Смартфон');
    const dynamicSpecs = ref({});
    const customSpecs = ref([]);

    const catalogStructure = {
        1: { name: 'Электроника', subcategories: ['Смартфон', 'Ноутбук'] },
        2: { name: 'Одежда', subcategories: ['Футболка', 'Джинсы', 'Кофта'] },
        3: { name: 'Дом и сад', subcategories: ['Мебель', 'Декор', 'Инструменты'] },
        4: { name: 'Красота и здоровье', subcategories: ['Уход', 'Парфюмерия'] },
        5: { name: 'Спорт и отдых', subcategories: ['Тренажёры', 'Туризм'] },
        6: { name: 'Автотовары', subcategories: ['Аксессуары', 'Автохимия'] },
        7: { name: 'Книги и канцелярия', subcategories: ['Книги', 'Канцтовары'] },
        8: { name: 'Другое', subcategories: ['Другое'] }
    };

    const specsCatalog = {
        'Смартфон': [
            { key: 'memory', label: 'Встроенная память', type: 'select', options: ['64 ГБ', '128 ГБ', '256 ГБ'] },
            { key: 'os', label: 'Операционная система', type: 'text', placeholder: 'Например: Android' }
        ],
        'Футболка': [
            { key: 'size', label: 'Размер', type: 'select', options: ['S', 'M', 'L', 'XL'] },
            { key: 'material', label: 'Материал', type: 'text', placeholder: 'Например: 100% хлопок' }
        ],
        'Кофта': [
            { key: 'size', label: 'Размер', type: 'select', options: ['S', 'M', 'L', 'XL'] },
            { key: 'material', label: 'Материал', type: 'text', placeholder: 'Например: 100% хлопок' }
        ],
        'Ноутбук': [
        { key: 'ram', label: 'Оперативная память', type: 'select', options: ['8 ГБ', '16 ГБ', '32 ГБ'] },
        { key: 'storage', label: 'Накопитель SSD', type: 'select', options: ['256 ГБ', '512 ГБ', '1 ТБ'] }
        ],
        'Джинсы': [{ key: 'size', label: 'Размер', type: 'select', options: ['S', 'M', 'L', 'XL'] }],
        'Мебель': [{ key: 'material', label: 'Материал корпуса', type: 'text', placeholder: 'Например: Массив дуба, ЛДСП' }],
        'Декор': [{ key: 'material', label: 'Материал', type: 'text', placeholder: 'Керамика, стекло' }],
        'Инструменты': [{ key: 'powerType', label: 'Тип питания', type: 'select', options: ['Аккумулятор', 'Сеть 220В', 'Ручной'] }],
        'Уход': [{ key: 'volume', label: 'Объём (мл)', type: 'text', placeholder: 'Например: 150 мл' }],
        'Парфюмерия': [{ key: 'volume', label: 'Объём (мл)', type: 'select', options: ['30 мл', '50 мл', '100 мл'] }],
        'Тренажёры': [{ key: 'maxWeight', label: 'Макс. вес пользователя (кг)', type: 'text', placeholder: 'Например: 120 кг' }],
        'Туризм': [{ key: 'weight', label: 'Вес (кг)', type: 'text', placeholder: 'Например: 2.5 кг' }],
        'Аксессуары': [{ key: 'compatibility', label: 'Совместимость', type: 'text', placeholder: 'Универсальный / марка авто' }],
        'Автохимия': [{ key: 'volume', label: 'Объём', type: 'text', placeholder: 'Например: 500 мл' }],
        'Книги': [{ key: 'pages', label: 'Количество страниц', type: 'text', placeholder: 'Например: 320 стр.' }],
        'Канцтовары': [{ key: 'color', label: 'Цвет чернил / корпуса', type: 'text', placeholder: 'Например: Синий' }],
        'Другое': [{ key: 'feature', label: 'Основная особенность', type: 'text', placeholder: 'Укажите главную характеристику' }]
    };

    const availableSubcategories = computed(() => {
        return catalogStructure[categoryId.value]?.subcategories || [];
    });

    const currentFields = computed(() => {
        return specsCatalog[subCategory.value] || [];
    });

    watch(categoryId, () => {
        subCategory.value = availableSubcategories.value[0] || '';
    });

    watch(subCategory, () => {
        dynamicSpecs.value = {};
    });

    const addCustomSpec = () => {
        customSpecs.value.push({ name: '', value: '' });
    };

    const removeCustomSpec = (index) => {
        customSpecs.value.splice(index, 1);
    };

    return {
        categoryId,
        subCategory,
        dynamicSpecs,
        customSpecs,
        availableSubcategories,
        currentFields,
        addCustomSpec,
        removeCustomSpec
    };
}