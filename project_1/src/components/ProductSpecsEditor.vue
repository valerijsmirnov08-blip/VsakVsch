<script setup>
defineProps({
    subCategory: {
        type: String,
        required: true
    },
    currentFields: {
        type: Array,
        default: () => []
    }
});

const dynamicSpecs = defineModel('dynamicSpecs', { default: () => ({}) });
const customSpecs = defineModel('customSpecs', { default: () => [] });

const addCustomSpec = () => {
    customSpecs.value.push({ name: '', value: '' });
};

const removeCustomSpec = (index) => {
    customSpecs.value.splice(index, 1);
};
</script>

<template>
    <div class="specs_block">
        <div class="specs_header">
            <h3 class="section_title">Характеристики: {{ subCategory }}</h3>
        </div>

        <div class="dynamic_spec_grid">
            <div v-for="field in currentFields" :key="field.key" class="spec_input_group">
                <label>{{ field.label }}</label>
                <select v-if="field.type === 'select'" v-model="dynamicSpecs[field.key]" class="mega_input spec">
                    <option value="" disabled selected>Выберите из списка</option>
                    <option v-for="opt in field.options" :key="opt" :value="opt">{{ opt }}</option>
                </select>
                <input 
                    v-else 
                    :type="field.type" 
                    v-model="dynamicSpecs[field.key]" 
                    :placeholder="field.placeholder" 
                    class="mega_input spec">
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
        </div>
    </div>
</template>

<style scoped>
.section_title {
    margin: 0;
    font-size: 24px;
    color: #111;
    font-weight: 800;
}
.specs_header {
    margin-bottom: 30px;
}
.dynamic_spec_grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
    gap: 24px;
}
.spec_input_group label {
    font-size: 15px;
    font-weight: 700;
    color: #555;
    margin-bottom: 10px;
    display: block;
}
.mega_input {
    padding: 16px 20px;
    border-radius: 16px;
    border: 1px solid #e0e4e8;
    background: #fbfbfc;
    font-size: 16px;
    color: #222;
    transition: 0.2s;
}
.mega_input:focus {
    border-color: #4caf50;
    background: #fff;
    outline: none;
    box-shadow: 0 0 0 4px rgba(76, 175, 80, 0.15);
}
.spec {
    width: 100%;
    box-sizing: border-box;
}
.custom_specs_section {
    margin-top: 35px;
    padding-top: 25px;
    border-top: 2px dashed #e2e8f0;
}
.custom_specs_header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
}
.btn_add_spec {
    background: #e8f5e9;
    color: #2e7d32;
    border: 1px solid #2e7d32;
    padding: 10px 20px;
    border-radius: 12px;
    font-weight: 700;
    cursor: pointer;
    transition: 0.2s;
}
.btn_add_spec:hover {
    background: #c8e6c9;
}
.custom_spec_row {
    display: flex;
    gap: 15px;
    margin-bottom: 15px;
    align-items: center;
}
.btn_remove_spec {
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
</style>