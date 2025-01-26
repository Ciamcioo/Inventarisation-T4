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
  const [isRecruit, isRecruitSet] = useState(false);

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
      const response = await axios.get(`http://localhost:8080/api/login`, {
        params: {
          email: email
        },
        withCredentials: true
      });

      if (response.data.message = "SUCCESFULL") {
        if (response.data.userRoleName == "Rekrut") {
          isRecruit = true;
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

  if (!isRecruit) {
    return (
      <div className="app-container">
        <header className="app-header">
          <h1>Rental Management System</h1>
          <button className="logout-button" onClick={handleLogout}>Logout</button>
        </header>
        <div className="main-content">
          <nav className="sidebar">
            <ul>
              <li><a href="#current-rentals" onClick={() => handleMenuClick('RentalEquipmentList')}>Current Rentals</a></li>
              <li><a href="#make-reservation" onClick={() => handleMenuClick('MakeReservation')}>Make Reservation</a></li>
              <li><a href="#locations" onClick={() => handleMenuClick('Locations')}>Locations</a></li>
            </ul>
          </nav>
          <section className="content">
            <div id="current-rentals" style={{display: activeComponent === 'RentalEquipmentList' ? 'block' : 'none'}}>
              <RentalEquipmentList />
            </div>
            <div id="make-reservation" style={{display: activeComponent === 'MakeReservation' ? 'block' : 'none'}}>
              <MakeReservation user={'mosowicz'}/>
            </div>
            <div id="locations" style={{display: activeComponent === 'Locations' ? 'block' : 'none'}}>
              <Locations user={'mosowicz'} />
            </div>
          </section>
        </div>
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
            <li><a href="#current-rentals" onClick={() => handleMenuClick('RentalEquipmentList')}>Current Rentals</a></li>
            <li><a href="#make-reservation" onClick={() => handleMenuClick('MakeReservation')}>Make Reservation</a></li>
          </ul>
        </nav>
        <section className="content">
          <div id="current-rentals" style={{display: activeComponent === 'RentalEquipmentList' ? 'block' : 'none'}}>
            <RentalEquipmentList />
          </div>
          <div id="make-reservation" style={{display: activeComponent === 'MakeReservation' ? 'block' : 'none'}}>
            <MakeReservation />
          </div>
        </section>
      </div>
    </div>
  );
}

export default App;
