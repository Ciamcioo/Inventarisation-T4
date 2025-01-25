// src/App.js
import React, { useState } from 'react';
import './App.css';
import Login from './Login';
import RentalEquipmentList from './RentalEquipmentList';

function App() {
  const [loggedIn, setLoggedIn] = useState(false);

  const handleLogin = () => {
    // Here you would typically send login data to backend and handle the response
    setLoggedIn(true);
  };

  return (
    <div className="App">
      {!loggedIn ? (
        <Login onLogin={handleLogin} />
      ) : (
         <div className="App">
            <RentalEquipmentList />
          </div>
      )}
    </div>
  );
}

export default App;
