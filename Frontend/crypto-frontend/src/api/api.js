import axios from "axios";

const API_URL = "http://localhost:1010";

export const api = axios.create({
    baseURL: API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

export const loginUser = async (credentials) => {
    const response = await api.post("/auth/login", credentials);
    return response.data;
};

export const registerUser = async (userData) => {
    const response = await api.post("/auth/register", userData);
    return response.data;
};

export const getCoinPrices = async () => {
    const response = await api.get("/public/coins");
    return response.data;
};

export const getWalletItems = async (token) => {
    const response = await api.get("/coins/wallet", {
        headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
};

export const getMyProfile = async (token) => {
    const response = await api.get("/auth/profile", {
        headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
};