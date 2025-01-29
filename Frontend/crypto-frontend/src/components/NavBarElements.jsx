import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import AuthContext from '../context/AuthContext';
import { useNavigate } from "react-router-dom";

const Navbar = () => {

    const { user, logout } = useContext(AuthContext);

    const navigate = useNavigate();

    const callLogOut = async () => {
        logout()
        navigate('/');
    }

    return (
        <nav style={navbarStyles}>
            <ul style={listStyles}>
                <li style={listItemStyles}><Link style={linkStyles} to="/">Home</Link></li>

                {user ? (
                    <>
                        <li style={listItemStyles}><Link style={linkStyles} to="/wallet">Wallet</Link></li>
                        <li style={listItemStyles}><Link style={linkStyles} to="/transactions">Transactions</Link></li>
                        <li onClick={callLogOut} style={linkStyles}>Logout</li>
                    </>  
                ) : (
                    <>
                        <li style={listItemStyles}><Link style={linkStyles} to="/login">Login</Link></li>
                        <li style={listItemStyles}><Link style={linkStyles} to="/register">Register</Link></li>
                    </>
                )}
            </ul>
        </nav>
    );
};

const navbarStyles = {
    background: '#333',
    padding: '10px 0',
    position: 'fixed',
    width: '100%',
    top: '0',
    left: '0',
    zIndex: '1000',
};

const listStyles = {
    display: 'flex',
    justifyContent: 'center',
    margin: '0',
    padding: '0',
    listStyleType: 'none',
};

const listItemStyles = {
    margin: '0 15px',
};

const linkStyles = {
    color: 'white',
    textDecoration: 'none',
    fontSize: '16px',
};

export default Navbar;