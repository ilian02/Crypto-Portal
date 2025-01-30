import { useState, useEffect, useContext } from "react";
import AuthContext from "../context/AuthContext";
import { getMyProfile, getCoinPrices } from "../api/api";

const Transactions = () => {
    const { user } = useContext(AuthContext);
    const [userInfo, setUserInfo] = useState([])
    const [coinsInfo, setCoinPrices] = useState([]);

    const formatDate = (timestamp) => {
        const date = new Date(timestamp);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-indexed, so add 1
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    const findGain = (table_item) => {
        const coin = coinsInfo.find(item => item.cryptoSymbol === table_item.cryptoSymbol)
        let difference
        if (table_item.transactionType === "buy") {
            difference = Number(coin.price * table_item.quantity - table_item.price).toPrecision(5);
        } else {
            difference = Number(table_item.price - coin.price * table_item.quantity).toPrecision(5);
        }
        if (difference > 0) {
            return "+" + difference;
        } else {
            return difference;
        }
    }

    useEffect(() => {

        const fetchCoinPrices = async () => {
            const data = await getCoinPrices();
            setCoinPrices(data.cryptos);
        };

        const fetchUserInfo = async () => {
            if (user) {
                const data = await getMyProfile(localStorage.getItem("token"));
                setUserInfo(data.client)
            }
        }

        fetchCoinPrices();
        fetchUserInfo();
    }, [userInfo, coinsInfo]);

    return (
        <div className="table-container">
        {userInfo ? (
            <>
                <h2>{userInfo.username}'s transactions</h2>
                {userInfo.transactions && userInfo.transactions.length > 0 ? (
                    <table className="styled-table">
                    <thead>
                        <tr>
                            <th>Crypto</th>
                            <th>Price Per</th>
                            <th>Total Price</th>
                            <th>Quantity</th>
                            <th>Type</th>
                            <th>Date</th>
                            <th>$</th>
                        </tr> 
                    </thead>    
                    <tbody>
                    {userInfo.transactions.map((item) => (
                        <tr key={item.id}>
                            <td>{item.cryptoSymbol}</td>
                            <td>{Number(item.price / item.quantity).toPrecision(5)}</td>
                            <td>{Number(item.price).toPrecision(5)}</td>
                            <td>{Number(item.quantity).toPrecision(5)}</td>
                            <td>{item.transactionType}</td>
                            <td>{formatDate(item.timestamp)}</td>
                            <td>{findGain(item)}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>      
                ) : (
                    <p>No transactions available</p>
                )}
            </>
        ) : (
            <p>Loading user info...</p>
        )}
    </div>
    
/*
<div className="table-container">
            <h2>Coin prices</h2>

                <table className="styled-table">
                    <thead>
                        <tr>
                            <th>Crypto</th>
                            <th>Price</th>
                        </tr> 
                    </thead>    
                    <tbody>
                    {coinsInfo.map((item) => (
                        <tr key={item.cryptoSymbol}>
                            <td>{item.cryptoSymbol}</td>
                            <td>{item.price}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>        
        </div>
*/

    );
};

export default Transactions;