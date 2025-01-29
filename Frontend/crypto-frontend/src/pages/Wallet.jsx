import { useState, useEffect, useContext } from "react";
import AuthContext from "../context/AuthContext";
import { getMyProfile } from "../api/api";

const Wallet = () => {
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
                <h2>{userInfo.username}'s wallet</h2>
                <h3>Current balance: {userInfo.balance}$</h3>
                {userInfo.transactions && userInfo.transactions.length > 0 ? (
                    <ul>
                        {userInfo.walletItems.map((item) => (
                            <li key={item.id}>
                                {item.symbol}: {item.quantity}
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No coins in your profile</p>
                )}
            </>
        ) : (
            <p>Loading user info...</p>
        )}
    </div>
    );
};

export default Wallet;