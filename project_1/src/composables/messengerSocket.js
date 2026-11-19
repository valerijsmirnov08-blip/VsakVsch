import { Client } from "@stomp/stompjs";
import SockJS from 'sockjs-client';
import { API_URL } from './apiConfig.js';
const WS_URL = `${API_URL.replace('/api', '')}/ws-messenger`;

class MessengerSocketService
{
    constructor()
    {
        this.client = null;
        this.currentSubscription = null;
        this.readSubscription = null;
        this.isConnected = false;
    }
    connect(onConnectedCallback)
    {
        if(this.client && this.isConnected)
        {
            if(onConnectedCallback)
            {
                onConnectedCallback();
                
            }
            return;
        }
        this.client = new Client({
            webSocketFactory: () => new SockJS(WS_URL),
            reconnectDelay: 3000,
            onConnect: () =>
            {
                this.isConnected = true;
                if(onConnectedCallback)
                {
                    onConnectedCallback();
                }
            },
            onDisconnect: () =>
            {
                this.isConnected = false;
            },
            onStompError: (frame) =>
            {
                console.error('STOMP ошибка:', frame.headers['message'], frame.body);
            }  
        })
        this.client.activate();
    
}
subscribeToChat(conversationId, { onMessage, onRead }) {
        this.unsubscribeFromChat();

        const doSubscribe = () => {
            this.currentSubscription = this.client.subscribe(
                `/topic/chat/${conversationId}`,
                (frame) => {
                    const message = JSON.parse(frame.body);
                    if (onMessage) onMessage(message);
                }
            );
            this.readSubscription = this.client.subscribe(
                `/topic/chat/${conversationId}/read`,
                (frame) => {
                    const event = JSON.parse(frame.body);
                    if (onRead) onRead(event);
                }
            );
        };

        if (this.isConnected) {
            doSubscribe();
        } else {
            this.connect(doSubscribe);
        }
    }

    unsubscribeFromChat() {
        if (this.currentSubscription) {
            this.currentSubscription.unsubscribe();
            this.currentSubscription = null;
        }
        if (this.readSubscription) {
            this.readSubscription.unsubscribe();
            this.readSubscription = null;
        }
    }

    sendMessage(conversationId, senderId, text) {
        if (!this.client || !this.isConnected) {
            console.warn('WebSocket ещё не подключён');
            return false;
        }
        this.client.publish({
            destination: `/app/chat/${conversationId}/send`,
            body: JSON.stringify({ senderId, text }),
        });
        return true;
    }

    markAsRead(conversationId, readerId) {
        if (!this.client || !this.isConnected || !readerId) return;
        this.client.publish({
            destination: `/app/chat/${conversationId}/read`,
            body: JSON.stringify({ readerId }),
        });
    }

    disconnect() {
        this.unsubscribeFromChat();
        if (this.client) {
            this.client.deactivate();
            this.client = null;
            this.isConnected = false;
        }
    }
}

export const messengerSocket = new MessengerSocketService();



