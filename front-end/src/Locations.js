import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './Locations.css'; // Assuming you'll have a separate CSS file for this component

const Locations = () => {
  const [locations, setLocations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchLocations = async () => {
      setLoading(true);
      try {
        const response = await axios.get('http://localhost:8080/api/mosowicz/locations', {
          withCredentials: true
        });
        setLocations(response.data);
        setError(null);
      } catch (err) {
        setError('Failed to fetch locations');
      }
      setLoading(false);
    };

    fetchLocations();
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div className="locations-container">
      <h2>Locations</h2>
      <ul className="locations-list">
        {locations.map((location) => (
          <li key={location.locationID} className="location-item">
            <strong>{location.name}</strong> - Role: {location.role.name}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Locations;
