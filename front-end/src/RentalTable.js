import React from 'react';
import './RentalTable.css'; // Import the CSS for the table

const RentalInfo = ({ rental }) => {
  // Convert timestamps to readable dates
  const startDate = new Date(rental.rentalStartDate).toLocaleDateString();
  const endDate = new Date(rental.rentalEndDate).toLocaleDateString();

  return (
    <table className="rental-table">
      <thead>
        <tr>
          <th>Field</th>
          <th>Value</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Rental Start Date</td>
          <td>{startDate}</td>
        </tr>
        <tr>
          <td>Rental End Date</td>
          <td>{endDate}</td>
        </tr>
        {rental.rentalEquipment.map((item, index) => (
          <React.Fragment key={index}>
            <tr>
              <td>Equipment Name</td>
              <td>{item.equipmentName}</td>
            </tr>
            <tr>
              <td>Equipment Description</td>
              <td>{item.equipmentDescription}</td>
            </tr>
          </React.Fragment>
        ))}
      </tbody>
    </table>
  );
};

const RentalTable = () => {
  const rentalData = {
    "rentalUUID": "9e7bf426-db15-11ef-a1d6-5c80b60e665e",
    "rentalStartDate": 1675206000000,
    "rentalEndDate": 1675551600000,
    "rentalEquipment": [
      {
        "equipmentName": "Drill",
        "equipmentDescription": "Power drills"
      }
    ]
  };

  return (
    <div>
      <h2>Rental Information</h2>
      <RentalInfo rental={rentalData} />
    </div>
  );
};

export default RentalTable;
