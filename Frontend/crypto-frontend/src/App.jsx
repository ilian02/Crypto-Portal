import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import Login from "./pages/Login";
import Register from "./pages/Register"
import Wallet from "./pages/Wallet";
import PrivateRoute from "./components/PrivateRoute";
import Index from "./pages/Index";
import Navbar from "./components/NavBarElements";
import Transactions from "./pages/Transactions";

const App = () => {
    return (
        <AuthProvider>
            <Router>
                <Navbar /> {}
                <div style={{ marginTop: '60px' }}> </div>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route element={<PrivateRoute />}>
                        <Route path="/wallet" element={<Wallet />} />
                        <Route path="/transactions" element={<Transactions />} />
                    </Route>
                    <Route path="/" element={<Index />} />
                </Routes>
            </Router>
        </AuthProvider>
    );
};

export default App;