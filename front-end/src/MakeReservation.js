import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './MakeReservation.css';

const MakeReservation = ({ user }) => {
  const [equipmentList, setEquipmentList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [reservationData, setReservationData] = useState({
    startDate: '',
    endDate: ''
  });
  const [showForm, setShowForm] = useState(false);
  const [transactionType, setTransactionType] = useState("RESERVATION");
  const [cart, setCart] = useState([]); // Use an array for cart to manage items and quantities

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

  // Function to add item to cart
  const addToCart = (item) => {
    const existingItem = cart.find(cartItem => cartItem.id === item.id);
    if (existingItem) {
      // If item already exists in cart, increase its quantity
      setCart(cart.map(cartItem =>
        cartItem.id === item.id ? { ...cartItem, quantity: cartItem.quantity + 1 } : cartItem
      ));
    } else {
      // Add new item to cart with quantity 1
      setCart([...cart, { ...item, quantity: 1 }]);
    }
  };

  // Function to remove item from cart or decrease quantity
  const removeFromCart = (itemId) => {
    setCart(prevCart => {
      const itemIndex = prevCart.findIndex(item => item.id === itemId);
      if (itemIndex === -1) return prevCart;

      const updatedCart = [...prevCart];
      if (updatedCart[itemIndex].quantity > 1) {
        updatedCart[itemIndex].quantity -= 1;
      } else {
        // Remove item from cart if quantity becomes zero
        updatedCart.splice(itemIndex, 1);
      }
      return updatedCart;
    });
  };

    const handleSubmit = async (e, type) => {
    e.preventDefault();
    try {
      let url = `${baseUrl}/make/reservation`;
      if (type === "RENTAL") {
        url = `${baseUrl}/make/rental`;
      }

      // Convert cart items to an array of UUID strings
      const equipmentIds = cart.map(item => item.id.toString());

      const response = await axios.post(url, {
        equipmentIDs: equipmentIds,
        startDate: reservationData.startDate,
        endDate: reservationData.endDate,
        transactionType: type
      }, {
        withCredentials: true
      });

      console.log('Transaction successful:', response.data);
      setCart([]);

      await fetchEquipment();

      setReservationData({ startDate: '', endDate: '' });
      setShowForm(false);
    } catch (err) {
      console.error('Error making transaction:', err);
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
                    onClick={() => addToCart(item)}
                    disabled={item.status.description !== "Available"}>
                    Add to Cart
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {/* Display cart contents */}
      <div className="cart-summary">
        <h3>Cart</h3>
        <ul>
          {cart.map(item => (
            <li key={item.id}>
              {item.name} - Quantity: {item.quantity}
              <button onClick={() => removeFromCart(item.id)}>Remove</button>
            </li>
          ))}
        </ul>
        <button onClick={() => setShowForm(true)} disabled={cart.length === 0}>Proceed to Checkout</button>
      </div>

      {showForm && (
        <form onSubmit={(e) => handleSubmit(e, transactionType)} className="reservation-form">
          <select onChange={(e) => setTransactionType(e.target.value)} value={transactionType}>
            <option value="reservation">Reservation</option>
            <option value="RENTAL">Rental</option>
          </select>
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
          <button type="submit">Submit {transactionType === "RENTAL" ? "RENTAL" : "RESERVATION"}</button>
        </form>
      )}
    </div>
  );
};

export default MakeReservation;
