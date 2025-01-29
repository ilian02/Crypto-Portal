import { useState, useEffect, useContext } from "react";
import AuthContext from "../context/AuthContext";
import { getCoinPrices } from "../api/api";
import "../styles/styles.css"

const Index = () => {
    const { user } = useContext(AuthContext);
    const [coinsInfo, setCoinPrices] = useState([]);

    useEffect(() => {
            const fetchCoinPrices = async () => {
                const data = await getCoinPrices();
                setCoinPrices(data.cryptos);
            };
            fetchCoinPrices();
        });

    return (
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
    );
        
}

export default Index;