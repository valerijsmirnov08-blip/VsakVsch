import { API_URL } from "./apiConfig.js";

export const getCart = async (userId) =>
{
    const response = await fetch(`${API_URL}/cart/${userId}`);
    if(response.ok)
    {
        return response.json();
    }
    else
    {
        return [];
    }
}
export const updateCartQuantity = async(userId, productId, quantity) =>
{
    const body = JSON.stringify(
        {
            userId:Number(userId),
            productId: Number(productId),
            quantity
        })
    const response = await fetch(`${API_URL}/cart/add`,
        {
            method: 'POST',
            headers:{ 'Content-Type': 'application/json' },
            body
        });
    return response.ok;
}
export const removeCartItem = async(cartItemId)=>
{
    const response = await fetch(`${API_URL}/cart/${cartItemId}`,
        {
            method: 'DELETE'
        });
    return response.ok;
}
export const checkoutCart = async(userId)=>
{
    const response = await fetch(`${API_URL}/orders/checkout/${userId}`,
        {
            method: 'POST'
        });
    const message = response.ok ? '' : await response.text();
    return {ok: response.ok, message};
}