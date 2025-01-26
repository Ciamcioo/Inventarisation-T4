import React, { useState } from 'react';
import axios from 'axios';
import './App.css';
import './Login.css';
import RentalEquipmentList from './RentalEquipmentList';
import MakeReservation from './MakeReservation';
import Locations from './Locations';

function App() {
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [activeComponent, setActiveComponent] = useState(null);
  const [isRecruit, setIsRecruit] = useState(false);

  const handleLogin = () => {
    setIsLoggedIn(true);
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    setActiveComponent(null); // Reset active component on logout
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      console.log(email);
      const response = await axios.get(`http://localhost:8080/api/login`, {
        params: {
          email: email
        },
        withCredentials: true
      });

      console.log(response);
      if (response.data.message === "SUCCESSFUL") {
        if (response.data.userRoleName === "Rekrut") {
          setIsRecruit(true);
        }
        handleLogin();
      } else {
        setError("Login failed");
      }
    } catch (error) {
      if (error.response) {
        setError(error.response.data.message || "Login failed");
      } else {
        setError("An error occurred during login");
      }
      console.error('Login error:', error);
    }
  };

  const handleMenuClick = (componentName) => {
    setActiveComponent(componentName);
  };

  if (!isLoggedIn) {
    return (
      <div className="login-container">
        <form onSubmit={handleSubmit} className="login-form">
          <h2>Login</h2>
          {error && <p className="error">{error}</p>}
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email"
            required
          />
          <button type="submit">Log In</button>
        </form>
      </div>
    );
  }

  return (
    <div className="app-container">
      <header className="app-header">
        <h1>Rental Management System</h1>
        <button className="logout-button" onClick={handleLogout}>Logout</button>
      </header>
      <div className="main-content">
        <nav className="sidebar">
          <ul>
            <li>
              <a href="#current-rentals" onClick={() => handleMenuClick('RentalEquipmentList')}>
                Current Rentals
              </a>
            </li>
            <li>
              <a href="#make-reservation" onClick={() => handleMenuClick('MakeReservation')}>
                Make Reservation
              </a>
            </li>
            {!isRecruit && (
              <li>
                <a href="#locations" onClick={() => handleMenuClick('Locations')}>
                  Locations
                </a>
              </li>
            )}
          </ul>
        </nav>
        <section className="content">
          {activeComponent === 'RentalEquipmentList' && <RentalEquipmentList />}
          {activeComponent === 'MakeReservation' && <MakeReservation user="mosowicz" />}
          {activeComponent === 'Locations' && !isRecruit && <Locations user="mosowicz" />}
        </section>
      </div>
    </div>
  );
}

export default App;

