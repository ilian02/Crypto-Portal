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

export const sellCoins = async (cryptoSymbol, quantity) => {
    try {
    const response = await api.post("/coins/sell", {
        transactionRequest: {
            cryptoSymbol: cryptoSymbol,
            quantity: quantity
        }
    },{
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
    })
    return response.data;
    } catch (error) {
        throw error.response ? error.response.data : new Error("Server error")
    }
}

export const buyCoins = async (cryptoSymbol, quantity) => {
    try {
    const response = await api.post("/coins/buy", {
        transactionRequest: {
            cryptoSymbol: cryptoSymbol,
            quantity: quantity
        }
    },{
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
    })
    return response.data;
    } catch (error) {
        throw error.response ? error.response.data : new Error("Server error")
    }
}

export const resetProfile = async () => {
    try {
    const response = await api.delete("/auth/reset-user",{
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
    })
    return response.data;
    } catch (error) {
        throw error.response ? error.response.data : new Error("Server error")
    }
}