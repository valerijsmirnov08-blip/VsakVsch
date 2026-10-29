<script setup>
    import { onMounted, ref } from 'vue'
    import {useRouter} from 'vue-router';

    const router = useRouter();
    

    const windowHelp = ref(false);
    const chatMesseges = ref([]);
    const isBotTyping = ref(false);

    const editName = ref('');
    const editShopName = ref('');
    const editShopDesc = ref('');
    const editBrandName = ref('');

    const isVendor = ref(true);
    const userDate = ref(null);
    const userId = ref('');

    const errorMessage = ref('');
    onMounted(() => 
    {
        const session = localStorage.getItem('user_session');

        if(session)
        {
            userDate.value = JSON.parse(session);
            editName.value = userDate.value.userName;
            editShopName.value = userDate.value.ShopName || '';
            editShopDesc.value = userDate.value.ShopDescription || '';
            editBrandName.value = userDate.value.brandName || '';
            userId.value = userDate.value.id || userDate.value.userId;
        }else
        {
            router.push('/enter_accaunt')
        }
    })

    const windowHelpBot = () =>
    {
        windowHelp.value = !windowHelp.value; 
        loadChatHistory();
    }
    const sendMessegeForBot = async(text) =>
    {
        chatMesseges.value.push
        ({
            id: Date.now(),
            sender: 'user',
            text: text
        })
        isBotTyping.value = true;
        try
        {
            
            const response = await fetch('http://localhost:8080/api/chat/bot',
                {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({message: text, userId: userId.value})
                })
            if(response.ok)
            {
                const data = await response.json();
               
                   chatMesseges.value.push
                   ({
                        id: Date.now() + 1,
                        sender: 'bot',
                        text: data.reply
                   })
    

            }
            else
            {
                console.error("Ошибка сервера:", response.status);
            }
        }
        catch(error)
        {
            console.error("Ошибка связи с сервером:", error);
        }
        finally
        {
            isBotTyping.value = false;
        }
    }
    
    const loadChatHistory = async() =>
    {
        if(!userId.value)
        {
            return;
        }
        try
        {
            const response = await fetch(`http://localhost:8080/api/chat/history/${userId.value}`)
            if(response.ok)
            {
                const history = await response.json();
                chatMesseges.value = history;
                if (chatMesseges.value.length === 0) {
                chatMesseges.value.push({
                    id: Date.now(),
                    sender: 'bot',
                    text: 'Здравствуйте! Что у вас случилось?'
                });
            }
            }
        
            }
        catch(error)
        {
            console.error("Ошибка загрузки истории:", error);
        }
       
    }
    const handleLogout = () => 
    {
        const confirmed = confirm("Выйти из аккаунта?");
        if(!confirmed)
        {
            return;
        }
        localStorage.removeItem('user_session');
        userDate.value = null;
        router.push('/enter_accaunt')
    };

    const saveProfile = async() =>
    {
        const confirmed = confirm("Сохранить внесённые изменения?");
        if(!confirmed)
        {
            return;
        }
        try
        {
            const response = await fetch(`http://localhost:8080/api/user/${userId.value}`,
                {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify
                    ({
                        shopName: editShopName.value.trim(),
                        shopDescription: editShopDesc.value.trim(),
                        userName: editName.value.trim()
                    })

                })
            if(!response.ok)
            {
                const errorData = await response.json().catch(() => ({}));
                throw new Error (errorData.messege  || "Ошибука ввода данных, повтарите попытку позже");
            }
            const updateUser = await response.json();
            localStorage.setItem('user_session', JSON.stringify(updateUser));
            userDate.value = updateUser;
            alert("Профиль успешно обновлен!");
        }
        catch(error)
        {

        }
    }
    const stopVendor = async () => {

    const confirmed = confirm("Пересать быть продавцом(вы всё потеряете)?");
    if(!confirmed)
    {
        return;
    }
    try {
        const response = await fetch(`http://localhost:8080/api/users/${userId.value}/stop_vendor`, {
            method: 'POST'
        });

        if (response.ok) {
            const updatedUser = await response.json();
            localStorage.setItem('user_session', JSON.stringify(updatedUser));
            userDate.value = updatedUser;
            alert("Вы больше не являетесь продавцом");
        } else {
            alert("Ошибка при обновлении статуса");
        }
    } catch (error) {
        console.error("Ошибка соединения:", error);
    }
};
    </script>
<template>   
    <div v-if="userDate" class="main_Accaunt">
        <h1>Здравствуйте, {{ userDate.userName }}!</h1>
        <div class="content_wrapper">
             <div class="profil_container">
            
            <div class="profil_links">
                <img src="/Избранное.png" alt="">
                <RouterLink to="/favorites">Избранное</RouterLink>
            </div>
            <div class="profil_links">
                <img src="/Покупки.png" alt="">
                <RouterLink to="/reviews">Покупки и отзывы</RouterLink>
            </div>
            <div class="profil_links">
                <img src="/Коробка_доставка.png" alt="">
                <RouterLink to="/delivery">Доставка</RouterLink>
            </div>
            <div v-if="!userDate.isVendor" class="profil_links">
                <img src="/Продавец_ЧБ.png" alt="">
                <RouterLink to="/vendor_b">Стать продавцом</RouterLink>
            </div>
            <div v-if="userDate.isVendor" class="profil_links" @click="stopVendor">
                <img src="/Продавец_ЧБ.png" alt="">
                <a href="#" @click.prevent>Перестать быть продавцом</a>
            </div>
            <div v-if="userDate.isVendor" class="profil_links">
                <img src="/ЗаказыПродавец_ЧБ.png" alt="">
                <RouterLink to="/vendor_o">Заказы</RouterLink>
            </div>
            <div v-if="userDate.isVendor" class="profil_links">
                <img src="/СтатистикаПродавец_ЧБ.png" alt="">
                <RouterLink to="/vendor_s">Статистика</RouterLink>
            </div>
            <div v-if="userDate.isVendor" class="profil_links">
                <img src="/Склад_Продавец_ЧБ.png" alt="">
                <RouterLink to="/vendor_w">Склад</RouterLink>
            </div>
            <div class="profil_links">
                <img src="/Работа.png" alt="">
                <a class="accaunt_work" href ="">Работа на складе/<wbr>Пункте выдачи</a>
            </div>
            <div class="profil_links">
                <img src="/Поддержка.png" alt="">
                <p @click="windowHelpBot" href="">Поддержка</p>
            </div>
            <div v-if="windowHelp" class="windowChat">
                <div class="chat_head">
                    <h3>Поддержка</h3>
                    <button @click="windowHelpBot" class="close_bot">✖</button>
                </div>
                <div class="chat_messeges">
                    <div v-for="msg in chatMesseges" :key = "msg.id" :class="['bot_messeges', msg.sender === 'bot' ? 'bot_msg_style' : 'user_msg_style']">
                        <p :class="msg.sender === 'bot' ? 'bot_messege' : 'user_messege'">
                            {{ msg.text }}
                        </p>
                    </div>
                    <div v-if="isBotTyping" class="bot_msg_style loader-container">
                        <div class="spinner"></div>
                    </div>
                </div>
                <div class="chat_quick_replaies">
                    <button @click="sendMessegeForBot('Как зарегистрироваться?')" class="replay_btn">Как зарегистрироваться?</button>
                    <button @click="sendMessegeForBot('Как стать продавцом?')" class="replay_btn">Как стать продавцом?</button>
                    <button @click="sendMessegeForBot('Почему мой заказ задерживается?')" class="replay_btn">Почему мой заказ задерживается?</button>
                    <button @click="sendMessegeForBot('Не отправляется заказ')" class="replay_btn">Не отправляется заказ</button>
                    <button @click="sendMessegeForBot('Позвать оператора')" class="replay_btn alert">Позвать оператора</button>
                </div>
            </div>
                
        </div>
        <div class="edit_profile">
            <form action="">
                <h2>Изменение аккаунта</h2>
                <input v-model="editName" type="text" placeholder="Изменить имя аккаунта">
                <input v-model="editShopName" v-if="userDate.isVendor" type="text" placeholder="Изменить название магазина">
                <input v-model="editShopDesc" v-if="userDate.isVendor" type="text" placeholder="Изменить описание магазина">
                <input v-model="editBrandName" v-if="userDate.isVendor" type="text" placeholder="Изменить название бренда">
                <button class="save_btn" @click.prevent="saveProfile">Сохранить изменения</button>
            </form>
        </div>
        </div>
       <button class="logout_btn" @click="handleLogout">Выйти</button>
       
    </div>
</template>

<style scoped>

.main_Accaunt
{
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 50px;
    padding: 20px;
    background-color: #fdfbf9;
    min-height: 70vh;
    font-family: system-ui, -apple-system, sans-serif;  
}
.content_wrapper
{
    display: flex;
    flex-direction: row;
    gap: 50px;
    justify-content: center;
    width: 100%;
    max-width: 1000px;
}
.profil_container
{
    display: flex;
    flex-direction: column;
    gap: 30px;
    width: 400px;
    flex-shrink: 0;
    margin: 0 auto;
}
.profil_container img
{
    width: 70px;
    margin-left: 10px;
}
.profil_links
{
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 0 20px;
    text-align: center;
    font-size: 18px;
    border-radius: 30px;
    background-color: rgba(254, 180, 123, 0.8);
}
.profil_links:hover
{
    background-color: #f0ebf4;
    cursor: pointer;
}
.profil_links a
{
    text-decoration: none;
    color: #000000;
    display: inline-block;
    max-width: 100%;
    word-break: break-word;
}
.accaunt_work
{
    text-align: center;
}
.edit_profile
{
    display: flex;
    flex-direction: column;
    align-items: center;
    flex: 1;
    padding: 40px;
    background-color: #f0ebf4;
    max-width: 300px;
    border-radius: 20px;
    gap: 20px;
}

.edit_profile input
{
    margin-top: 20px;
    width: 100%;
    height: 56px;
    border-radius: 40px;
    border: 1px solid #f0edf0;
    background: #fcfbfc;
    padding: 0 20px;
    box-sizing: border-box;
    font-size: 16px;
    outline: none;
    transition: all 0.2s ease;
    border-color: #000000;
}
.edit_profile input:focus
{
    border-color: #fa5f39;
    background-color: #ffff;
    box-shadow: 0 0 0 4px rgba(255, 126, 95, 0.1);
}
.profile_content
{
    flex: 1;
    background-color: #ffffff;
    border: 1px solid #f2edf0;
    border-radius: 20px;
    padding: 40px;
    min-height: 380px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.02);
    box-sizing: border-box;
}

.profile_content h2
{
    margin-top: 0;
    color: #222222;
    font-size: 24px;
}

.profile_content p
{
    color: #666666;
    line-height: 1.6;
}
.logout_btn
{
    width: 400px;
    height: 50px;
    border-radius: 40px;
    border: none;
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    color: #ffff;
    font-size: 18px;
    font-weight: 600;
    cursor: pointer;
    transition: opacity 0.2s;
    box-shadow: 0 4px 10px rgba(255, 126, 95, 0.2);
}

.logout_btn:hover
{
    background-color: #ffebeb;
    border-color: #ffcccc;
}
.save_btn
{
    margin-top: 20px;
    width: 400px;
    height: 50px;
    border-radius: 40px;
    border: none;
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    color: #ffff;
    font-size: 18px;
    font-weight: 600;
    cursor: pointer;
    transition: opacity 0.2s;
    box-shadow: 0 4px 10px rgba(255, 126, 95, 0.2);
}
.save_btn:hover
{
    background-color: #ffebeb;
    border-color: #ffcccc;
}
.windowChat
{
    display: flex;
    flex-direction: column;
    padding: 20px;
    box-sizing: border-box; 
    position: fixed;
    bottom: 20px;
    right: 20px;
    width: 350px;
    height: 500px;
    background-color: #ffffff;
    border: 1px solid #ccc;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
    z-index: 1000;

}
.chat_head
{
    display: flex;
    justify-content: center;
    border-bottom: 1px solid #eee;
    padding-bottom: 10px;
    margin-bottom: 10px;
}
.chat_head h3
{
    margin: 0;
    font-size: 18px;
    color: #333;
}
.close_bot
{
    background-color: none;
    border: none;
    font-size: 20px;
    cursor: pointer;
    color: #888;
    margin-left: 18px;
}
.close_bot:hover
{
    color: #333;
}
.chat_messeges
{
    flex: 1;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    gap: 10px;
    margin-bottom: 15px;
}
.bot_messeges
{
    padding: 10px 15px;
    border-radius: 15px;
    max-width: 85%;
    font-size: 14px;
    line-height: 1.4;
}
.bot_messege
{
    background-color: #f0ebf4;
    align-self: flex-start;
    border-bottom-left-radius: 5px;
    color: #333;
}
.chat_quick_replaies
{
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
}
.replay_btn
{
    background-color: #fff;
    border: 1px solid #feb47b;
    color: #ff7e5f;
    padding: 8px 12px;
    border-radius: 20px;
    font-size: 13px;
    cursor: pointer;
    transition: all;
}
.replay_btn:hover
{
    background-color: #ff7e5f;
    color: #fff;
}
.replay_btn.alert
{
    border-color: #ccc;
    color: #666;
}
.replay_btn.alert:hover
{
    background-color: #666;
    color: #fff;
}
.bot_msg_style
{
    align-self: flex-start;
}
.user_msg_style
{
    align-self: flex-end;
}
.user_messege
{
    background-color: #ff7e5f;
    color: #fff;
    padding: 10px 15px;
    border-radius: 15px;
    border-bottom-right-radius: 5px;
    margin: 0;
}
.loader_container {
    padding: 10px 15px;
    align-self: flex-start;
}

.spinner {
    width: 24px;
    height: 24px;
    border: 3px solid #f0ebf4; /* Светлое базовое кольцо */
    border-top: 3px solid #ff7e5f; /* Оранжевый акцент сверху */
    border-radius: 50%; /* Делает квадрат круглым */
    animation: spin 1s linear infinite; /* Запускает анимацию с именем spin */
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
</style>