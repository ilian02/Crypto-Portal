import { useState, useEffect, useContext } from "react";
import AuthContext from "../context/AuthContext";
import { getMyProfile, getCoinPrices, sellCoins, buyCoins, resetProfile } from "../api/api";
import "../styles/styles.css"

const Wallet = () => {
    const { user } = useContext(AuthContext);
    const [userInfo, setUserInfo] = useState([])
    const [coinsInfo, setCoinPrices] = useState([]);
    const [error, setError] = useState("");

    const handleSell = async (e, coinSymbol) => {
        e.preventDefault();
        const quantity = e.target.quantity.value;
        e.target.quantity.value = ""
        setError("");
        try {
            let resp = await sellCoins(coinSymbol, quantity)
        }
        catch (err) {
            setError(err.error || "An error occurred.")
        }
    }

    const handleBuy = async (e, coinSymbol) => {
        e.preventDefault();
        const quantity = e.target.quantity.value;
        e.target.quantity.value = ""
        setError("");
        try {
            const resp = await buyCoins(coinSymbol, quantity)
        }
        catch (err) {
            setError(err.error || "An error occurred.")
        }
    }

    const handleReset = async () => {
        try {
            setError("");
            const resp = await resetProfile();
            alert("Profile reset successfully!");
        } catch (err) {
            setError(err);
        }
    };


    useEffect(() => {

        const fetchUserInfo = async () => {
            if (user) {
                const data = await getMyProfile(localStorage.getItem("token"));
                setUserInfo(data.client)
            }
        }
    
        const fetchCoinPrices = async () => {
            const data = await getCoinPrices();
            setCoinPrices(data.cryptos);
        };


        fetchCoinPrices();
        fetchUserInfo();
    }, [userInfo, coinsInfo]);

    return (
        <>
        {error && <p style={{ color: "red" }}>{error}</p>}
        <div className="container">
            <div className="wallet_container">
            {userInfo ? (
                <>
                    <h2>{userInfo.username}'s wallet</h2>
                    <h3>Current balance: {(Number(userInfo.balance).toFixed(4))}$</h3>
                    {userInfo.transactions && userInfo.transactions.length > 0 ? (
                    <table>
                        <thead>
                            <tr>
                                <th>Crypto</th>
                                <th>Quanity</th>
                                <th>Sell offer</th>
                            </tr> 
                        </thead>    
                        <tbody>
                        {userInfo.walletItems.map((item) => (
                            item.quantity > 0 &&
                            <tr key={item.id}>
                                <td>{item.symbol}</td>
                                <td>{item.quantity}</td>
                                <td>
                                    <form onSubmit={(e) => handleSell(e, item.symbol)}>
                                        <input 
                                            type="number"
                                            min="0"
                                            step="any" 
                                            name="quantity"
                                            placeholder="Quantity"
                                            required
                                        />
                                        <button type="submit">Sell</button>
                                    </form>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>      
                    ) : (
                        <p>No coins in your profile</p>
                    )}
                </>
            ) : (
                <p>Loading user info...</p>
            )}
            </div>
            <div className="coin_container">
                <h2>Coin prices</h2>

                <table>
                    <thead>
                        <tr>
                            <th>Crypto</th>
                            <th>Price</th>
                            <th>Offer</th>
                        </tr> 
                    </thead>    
                    <tbody>
                    {coinsInfo.map((item) => (
                        <tr key={item.cryptoSymbol}>
                            <td>{item.cryptoSymbol}</td>
                            <td>{item.price}</td>
                            <td><form onSubmit={(e) => handleBuy(e, item.cryptoSymbol)}>
                                        <input 
                                            type="number"
                                            min="0"
                                            step="any"
                                            name="quantity"
                                            placeholder="Quantity"
                                            required
                                        />
                                        <button type="submit">Buy</button>
                                    </form></td>
                        </tr>
                    ))}
                    </tbody>
                </table>            
            </div>
        </div>

        <h2>If you wish to reset your profile to balance 10000 and lose your transaction history and wallet click here!</h2>
        <button onClick={handleReset} className="reset-button">Reset Account</button>
        </>
    );
};

export default Wallet;