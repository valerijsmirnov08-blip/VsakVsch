import {API_URL} from './apiConfig.js';

export const getProducts = async(page = 0, size = 20) =>
{
    const response = await fetch(`${API_URL}/products?page=${page}&size=${size}`);
    return response.ok ? response.json() : null;
}
export const getFavorites = async(userId) =>
{
    const response = await fetch(`${API_URL}/favorites/user/${userId}?size=1000`);
    return response.ok ? response.json() : null;
}
export const toggleFavoriteItem = async (userId, productId) =>
{
    const body = JSON.stringify({ userId: Number(userId), productId: Number(productId) });
    const response = await fetch(`${API_URL}/favorites/toggle`,
    {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body
    });
    return response.ok;
};