import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './MakeReservation.css'

const MakeReservation = ({ user }) => {
  const [equipmentList, setEquipmentList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [reservationData, setReservationData] = useState({
    startDate: '',
    endDate: ''
  });
  const [selectedEquipmentId, setSelectedEquipmentId] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [transactionType, setTransactionType] = useState("reservation");

  // Determine the base URL based on the user prop
  const baseUrl = user === 'mosowicz' ? 'http://localhost:8080/api/mosowicz' : 'http://localhost:8080/api/rekrut';

  useEffect(() => {
    fetchEquipment();
  }, []);

  const fetchEquipment = async () => {
    setLoading(true);
    try {
      const response = await axios.get(`${baseUrl}/equipment`, {
        withCredentials: true
      });
      setEquipmentList(response.data);
      setError(null);
    } catch (err) {
      setError('Failed to fetch equipment list');
    }
    setLoading(false);
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setReservationData({ ...reservationData, [name]: value });
  };

  const handleSubmit = async (e, type) => {
    e.preventDefault();
    try {
      let url = `${baseUrl}/reservations`;
      if (type === "RENTAL") {
        url = `${baseUrl}/make/rental`;
      }

      const response = await axios.post(url, {
        equipmentIDs: [selectedEquipmentId],
        ...reservationData,
        transactionType: type
      }, {
        withCredentials: true
      });
      // Refresh the equipment list after the transaction
      await fetchEquipment();

      // Reset form or show success message
      setReservationData({ startDate: '', endDate: '' });
      setSelectedEquipmentId(null);
      setShowForm(false);
    } catch (err) {
      console.error('Error making transaction:', err);
      // Handle errors, e.g., show an error message to the user
    }
  };

  return (
    <div className="make-reservation">
      <h2>Make a Reservation or Rental</h2>
      <table className="equipment-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Quantity</th>
            <th>Description</th>
            <th>Location</th>
            <th>Status</th>
            <th>Technical Status</th>
            <th>Category</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {equipmentList.map((item) => (
            <tr key={item.id}>
              <td>{item.name}</td>
              <td>{item.quantity}</td>
              <td>{item.description}</td>
              <td>{item.location.name}</td>
              <td>{item.status.description}</td>
              <td>{item.technicalStatus.description}</td>
              <td>{item.category.name}</td>
              <td>
                <div className="action-buttons">
                  <button
                    onClick={() => {
                      setSelectedEquipmentId(item.id);
                      setTransactionType("reservation");
                      setShowForm(true);
                    }}
                    disabled={item.status.description !== "Available"}>
                    Reserve
                  </button>
                  <button
                    onClick={() => {
                      setSelectedEquipmentId(item.id);
                      setTransactionType("RENTAL");
                      setShowForm(true);
                    }}
                    disabled={item.status.description !== "Available"}>
                    Rent
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {showForm && (
        <form onSubmit={(e) => handleSubmit(e, transactionType)} className="reservation-form">
          <input
            type="date"
            name="startDate"
            value={reservationData.startDate}
            onChange={handleInputChange}
            required
          />
          <input
            type="date"
            name="endDate"
            value={reservationData.endDate}
            onChange={handleInputChange}
            required
          />
          <button type="submit">{transactionType === "RENTAL" ? "Submit Rental" : "Submit Reservation"}</button>
        </form>
      )}
    </div>
  );
};

export default MakeReservation;
