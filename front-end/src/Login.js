import React, { useState } from 'react';
import axios from 'axios';
import './Login.css';

const Login = ({ onLogin }) => {
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.get(`http://localhost:8080/api/login`, {
        params: {
          email: email
        }
      });

      if (response.data.message = "SUCCESFULL") {
        onLogin(response.data);
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
};

export default Login;
