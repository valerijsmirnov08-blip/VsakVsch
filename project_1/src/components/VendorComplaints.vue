<script setup>
    import {computed, ref, onMounted} from 'vue';


    const complaints = ref([]);
    const filter = ref('Все');
    const activeMenu = ref(null);

    const toggleMenu = (id) =>
    {
        activeMenu.value = activeMenu.value === id ? null : id; 
    }
   
    const loadComplaints  = async() =>
    {
        const session = localStorage.getItem('user_session');
        const user = JSON.parse(session);
        const vendorId = user.id;
        try
        {
            const response  = await fetch(`http://localhost:8080/api/reviews/vendor/${vendorId}`)
            if(response.ok)
            {
                const compData = await response.json();
                complaints.value = compData.filter(item => item.isComplaint === true)
                console.log("Загруженные жалобы:", complaints.value);
            } else
            {
            const errorText = await response.text();
            console.error("Текст ошибки 500 от Java:", errorText);
            }
        }
        catch(error)
        {
            console.error("Ошибка загрузки жалоб:", error);
        }
    } 
    const formatedComplaints = computed(() =>
    {
        let filtered = complaints.value;
        
        if (filter.value === 'Просмотренные') {
            filtered = filtered.filter(r => r.isRead === true);
        } else if (filter.value === 'Не просмотренные' || filter.value === 'Новые') {
            filtered = filtered.filter(r => r.isRead === false);
        }

        return filtered.map(r => ({
            id: r.id,
            statusComplaint: r.complaintStatus,
            date: r.createdAt ? new Date(r.createdAt).toLocaleDateString() : 'Нет даты',
            nameProduct: r.productId,
            groupComplaints: r.parentCategories || 'Без группы',
            subComplaints: r.subCategories || 'Не указана',
            complaintText: r.complaintText || 'Нет текста',
            commentText: r.comment || 'Нет текста'
        }));
    })


    const changeComplaintStatus = async (item, newStatusStr) => {
        try {
            const response = await fetch(`http://localhost:8080/api/reviews/update-complaint-status`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    id: item.id,
                    status: newStatusStr
                })
            });
            
            if (response.ok) {
                console.log("Статус жалобы успешно изменен", item.id);
                await loadComplaints();
            } else {
                const errText = await response.text();
                console.error("Ошибка от бэкенда:", errText);
            }
        } catch (error) {
            console.error("Сбой сети:", error);
        } finally {
            activeMenu.value = null;
        }
    }


    onMounted(() =>
    {
        loadComplaints();
    })
</script>
<template>
    <div class="complaints_grid">
        
        <div class="date_complaints_for_vendor">
            <h2>Жалобы покупателей</h2>
            <div class="for_vendor">
                
                <nav>
                    <button @click="filter = 'Все'" :class="{ active: filter === 'Все' }">Все</button>
                    <button @click="filter = 'Новые'" :class="{ active: filter === 'Новые' }">Новые</button>
                    <button @click="filter = 'Закрытые'" :class="{ active: filter === 'Закрытые' }">Закрытые</button>
                    <button @click="filter = 'Не закрытые'" :class="{ active: filter === 'Не закрытые' }">Не закрытые</button>
                </nav>
            </div>
        </div>
        <div>
            <input  class="search_product"   type="text" placeholder="Название, артикул">
        </div>
        <table class="complaints_table">
        <thead>
            <tr>
                <th>№ Жалобы</th>
                <th>Статус жалобы</th>
                <th>Дата создания жалобы</th>
                <th>ID товара</th>
                <th>Группа жалоб</th>
                <th>Жалоба</th>
                <th>Комментарий</th>
                <th>Действия</th>
            </tr>   
        </thead>
        <tbody>
            <tr v-for="item in formatedComplaints" :key="item.id">
                <td>{{ item.id }}</td>
                <td >{{ item.statusComplaint }}</td>
                <td>{{ item.date }}</td>
                <td>{{ item.nameProduct }}</td>
                <td>{{ item.groupComplaints }}</td>
                <td>{{ item.subComplaints }}</td>
                <td>{{ item.commentText }}</td>
                <td class="action_cell">
                    <button class="dots_btn" @click="toggleMenu(item.id)">⋮</button>
                    <div v-if="activeMenu === item.id" class="dropdown_menu">
                        <button @click="changeComplaintStatus(item, 'IN_PROGRESS')">Взять в работу</button>
                        <button @click="changeComplaintStatus(item, 'RESOLVED_CUSTOMER')">Согласиться с претензией</button>
                        <button @click="changeComplaintStatus(item, 'ESCALATED')">Оспорить (Передать в поддержку)</button>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
    </div>
    
</template>

<style scoped>
.complaints_grid
{
    margin: 30px;
}
.date_complaints_for_vendor
{
    margin-bottom: 50px;
}
.for_vendor
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
.for_vendor nav .active
{
    border-bottom: 3px solid #FF5B46;
    font-weight: bold;
}
.for_vendor nav button
{
    width: 135px;
    height: 20px;
    border: none;
    background: none;
    cursor: pointer;
    font-size: 14px;
}
.search_product
{
    position: relative;
    z-index: 1001;
    width: 500px;
    height: 30px;
    padding: 14px 25px;
    border-radius: 30px;
    border: none;
    background-color: #f0f0f0;
    font-size: 18px;
    outline: none;
    margin-bottom: 40px;
}
.complaints_table
{
    width: 100%;
    border-collapse: collapse;
}
.complaints_table th
{
    text-align: center;
    text-align: left;
    color: #363636;
    padding: 15px 10px;
    border-bottom: 1px solid #E0E0E0;
}
.complaints_table td
{
    text-align:justify;
    padding: 10px 10px;
    border-bottom: 1px solid #E0E0E0;
}
.action_cell {
    position: relative; 
    text-align: center;
}

.dots_btn {
    background: none;
    border: none;
    font-size: 20px;
    font-weight: bold;
    cursor: pointer;
    color: #363636;
    padding: 0 10px;
}

.dropdown_menu {
    position: absolute;
    top: 100%;
    right: 0; 
    background: white;
    border: 1px solid #E0E0E0;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15); 
    z-index: 9999; 
    min-width: 220px;
    padding: 5px 0;
    display: flex;
    flex-direction: column;
}

.dropdown_menu button {
    background: none;
    border: none;
    padding: 10px 15px;
    text-align: left;
    cursor: pointer;
    font-size: 14px;
    color: #333;
    width: 100%;
}

.dropdown_menu button:hover {
    background-color: #f5f5f5;
}
</style>