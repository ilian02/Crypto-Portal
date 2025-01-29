import { useState, useEffect, useContext } from "react";
import AuthContext from "../context/AuthContext";
import { getMyProfile } from "../api/api";

const Transactions = () => {
    const { user } = useContext(AuthContext);
    const [userInfo, setUserInfo] = useState([])

    useEffect(() => {

        const fetchUserInfo = async () => {
            if (user) {
                const data = await getMyProfile(localStorage.getItem("token"));
                setUserInfo(data.client)
            }
        }

        fetchUserInfo();
    }, [user]);

    return (
        <div>
        {userInfo ? (
            <>
                <h2>{userInfo.username}'s transactions</h2>
                {userInfo.transactions && userInfo.transactions.length > 0 ? (
                    <ul>
                        {userInfo.transactions.map((item) => (
                            <li key={item.id}>
                                {item.cryptoSymbol}: {item.quantity}, {item.price}, {item.transactionType}, {item.timestamp}
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No transactions available</p>
                )}
            </>
        ) : (
            <p>Loading user info...</p>
        )}
    </div>
    );
};

export default Transactions;