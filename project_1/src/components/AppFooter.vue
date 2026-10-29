<script setup>
import { ref, onMounted } from 'vue'


const windowHelp = ref(false);
const chatMesseges = ref([]);
const isBotTyping = ref(false);
const userId = ref('');

onMounted(() => {
    const session = localStorage.getItem('user_session');
    if (session) {
        const userData = JSON.parse(session);
        userId.value = userData.id || userData.userId;
    }
});
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
</script>
<template>
    <div class="bottom">
        <div class="column_bottom">
            <p class="column_title">Для покупателей</p>
            <a href="">Частые вопросы</a>
            <a href="">Доставка</a>
            <a href="">Страховка товара</a>
            <a href=""><img src="/Вацап_ЧБ.png" alt=""></a>
        </div>
        <div class="column_bottom">
            <p class="column_title">Для продавцов</p>
            <a href="">Продажа товара</a>
            <a href="">Открытие своего пунтка выдачи</a>
            <a href="">Доставка заказов</a>
            <a href=""><img src="/ТГ_Чб.png" alt=""></a>
        </div>
        <div class="column_bottom">
            <p class="column_title">Наша компания</p>
            <a href="">О нас</a>
            <a href="">Контакты</a>
            <p class="help_p" @click="windowHelpBot">Поддержка</p>
            <a href=""><img src="/Макс_ЧБ.png" alt=""></a>
        </div>
    </div>
     <div v-if="windowHelp" class="windowChat">
        <div class="chat_head">
            <h3>Поддержка</h3>
            <button @click="windowHelpBot" class="close_bot">✖</button>
        </div>
        
        <div class="chat_messeges">
            <div v-for="msg in chatMesseges" :key="msg.id" :class="['bot_messeges', msg.sender === 'bot' ? 'bot_msg_style' : 'user_msg_style']">
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
</template>
<style scoped>
.help_p
{
    font-size: 15px;
    text-decoration: none;
    color: #0000009f;
    cursor: pointer;
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


.floating-chat-btn {
    position: fixed;
    bottom: 20px;
    right: 20px;
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    color: white;
    border: none;
    padding: 15px 25px;
    border-radius: 30px;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    box-shadow: 0 4px 15px rgba(255, 126, 95, 0.3);
    z-index: 1000;
    transition: transform 0.2s;
}
.floating-chat-btn:hover {
    transform: scale(1.05);
}
</style>