import React, { useState, useEffect } from 'react';
import './RentalEquipmentList.css'; // Import the CSS
import axios from 'axios';

const RentalEquipmentList = () => {
  const [equipmentList, setEquipmentList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchEquipment = async () => {
      try {
        const respo = await axios.get(`http://localhost:8080/api/login`);
        const response = await axios.get('http://localhost:8080/api/mosowicz/locations', {
          params: {
            email:"bob@example.com"
          }
        });
        console.log(response);
        console.log(resp);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch equipment list');
        setLoading(false);
      }
    };

    fetchEquipment();
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div>
      <h2>Rental Equipment List</h2>
      <ul>
        {equipmentList.map((item) => (
          <li key={item.id}>
            <strong>{item.name}</strong> - Status: {item.status}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default RentalEquipmentList;
