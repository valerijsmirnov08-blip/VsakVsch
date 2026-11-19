import { API_URL } from './apiConfig.js';
const BASE_URL = `${API_URL}/messenger`;

export const  messengerApi =
{
    async getConversations(userId)
    {
        const response = await fetch(`${BASE_URL}/conversations?userId=${userId}`);
        return response.json(); 
    },
    async openProductChat(buyerId, vendorId, productId)
    {
        const params = new URLSearchParams({buyerId, vendorId, productId});
        const response = await fetch(`${BASE_URL}/open/product?${params}`,
            {
                method: 'POST'
            })
        return response.json();
    },
    async openSupportChat(buyerId, guestEmail)
    {
        const params = new URLSearchParams();
        if(buyerId) params.append('buyerId', buyerId);
        if(guestEmail) params.append('guestEmail', guestEmail);
        const response = await fetch(`${BASE_URL}/open/support?${params}`,
            {
                method: 'POST'
            })
        return response.json();
    },
    async getMessege(conversationId, readerId)
    {
        const query = readerId ? `?readerId=${readerId}` : '';
        const response = await fetch(`${BASE_URL}/${conversationId}/messages${query}`);
        return response.json();
    },
    async sendMessege(conversationId, senderId, text)
    {
        const response = await fetch(`${BASE_URL}/${conversationId}/messages`,
            {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({senderId, text})
            });
        return response.json();
    }
}