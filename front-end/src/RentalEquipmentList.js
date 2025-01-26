import React, { useState, useEffect } from 'react';
import './RentalEquipmentList.css';
import axios from 'axios';

const RentalEquipmentList = () => {
  const [rentals, setRentals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchRentals();
  }, []);

  const fetchRentals = async () => {
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/mosowicz/current/rentals', {
        withCredentials: true
      });
      // Assuming the API now returns more detailed information about each rental
      setRentals(response.data);
      setError(null);
    } catch (err) {
      setError('Failed to fetch current rentals');
    }
    setLoading(false);
  };

   const handleReturn = async (rentalUUID) => {
    try {
      // Using query parameters instead of body for DELETE request
      await axios.delete('http://localhost:8080/api/mosowicz/make/return', {
        params: {
          rentalID: rentalUUID
        },
        withCredentials: true,
      });
      console.log('Rental returned:', rentalUUID);
      // Refresh the list after returning an item
      await fetchRentals();
    } catch (err) {
      console.error('Error returning rental:', err);
      // Show error to user or handle appropriately
    }
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div className="rental-list-container">
      <h2>Current Rentals</h2>
      <ul className="rental-list">
        {rentals.map((rental) => (
          <li key={rental.id} className="rental-item">
            <div className="rental-details">
              <h3>{rental.name}</h3>
              <p><strong>Start Date:</strong> {new Date(rental.rentalStartDate).toLocaleDateString()}</p>
              <p><strong>End Date:</strong> {new Date(rental.rentalEndDate).toLocaleDateString()}</p>
          {Array.from({ length: rental.rentalEquipment.length }, (_, index) => (
                <React.Fragment key={index}>
                  <p><strong>Name:</strong> {rental.rentalEquipment[index].equipmentName}</p>
                  <p><strong>Description:</strong> {rental.rentalEquipment[index].equipmentDescription}</p>
                </React.Fragment>
              ))}
            </div>
            <div className="rental-actions">
              <button
                onClick={() => handleReturn(rental.rentalUUID)}
                className="return-button">
                Return
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default RentalEquipmentList;
