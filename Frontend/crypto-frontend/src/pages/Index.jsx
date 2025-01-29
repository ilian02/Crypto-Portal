import { useState, useEffect, useContext } from "react";
import AuthContext from "../context/AuthContext";
import { getCoinPrices } from "../api/api";

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
        <div>
            <h2>Coin prices</h2>
            <ul>
                {coinsInfo.map((item) => (
                    <li key={item.cryptoSymbol}>
                        {item.cryptoSymbol}: {item.price}
                    </li>
                ))}
            </ul>
        </div>
    );
        
}

export default Index;