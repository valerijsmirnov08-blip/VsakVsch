const host = window.location.hostname;
const port = import.meta.env.VITE_API_PORT || '8080';
export const API_URL = `http://${host}:8080/api`;