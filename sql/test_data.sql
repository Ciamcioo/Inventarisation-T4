INSERT INTO Role (name) VALUES 
('Admin'), 
('Manager'), 
('User');

INSERT INTO Location (name, role_id) VALUES 
('T4', 1),
('T4', 2),
('T4', 3);

INSERT INTO User (id, first_name, last_name, Role_ID, hire_date) VALUES 
(UUID(), 'Alice', 'Smith', 1, '2020-01-15'),
(UUID(), 'Bob', 'Johnson', 2, '2021-06-01'),
(UUID(), 'Charlie', 'Brown', 3, '2022-09-12');

INSERT INTO ContactUserInformation (user_id, Phone_Number, Email) VALUES 
((SELECT id FROM User WHERE first_name = 'Alice'), '555-1234', 'alice@example.com'),
((SELECT id FROM User WHERE first_name = 'Bob'), '555-5678', 'bob@example.com'),
((SELECT id FROM User WHERE first_name = 'Charlie'), '555-9012', 'charlie@example.com');

INSERT INTO TechnicalStatus (description) VALUES 
('Good'), 
('Maintenance'), 
('Broken');

INSERT INTO Status (name) VALUES 
('Available'), 
('Reserved'), 
('Rented');

INSERT INTO Category (name) VALUES 
('Electronics'), 
('Tools');

INSERT INTO Equipment (id, name, quantity, description, technical_status_id, status_id, category_id, location_id) VALUES 
(UUID(), 'Laptop', 10, 'High-performance laptops', 1, 1, 1, 1),
(UUID(), 'Raspberry', 5, 'Pico W', 3, 3, 1, 3),
(UUID(), 'Drill', 5, 'Power drills', 2, 2, 2, 2);

INSERT INTO RentalRegister (id, user_id, equipment_id, start_date, end_date) VALUES 
(UUID(), (SELECT id FROM User WHERE first_name = 'Alice'), 
 (SELECT id FROM Equipment WHERE name = 'Laptop'), '2023-01-01', '2023-01-10'),
(UUID(), (SELECT id FROM User WHERE first_name = 'Bob'), 
 (SELECT id FROM Equipment WHERE name = 'Drill'), '2023-02-01', '2023-02-05');

INSERT INTO ReservationRegister (reservation_id, user_id, timeout, start_date, end_date, equipment_id) VALUES 
(UUID(), (SELECT id FROM User WHERE first_name = 'Charlie'), '2023-03-01', '2023-03-01', '2023-03-07', 
 (SELECT id FROM Equipment WHERE name = 'Raspberry'));

INSERT INTO ReservationEquipment (reservation_id, equipment_id, identifier, quantity) VALUES 
((SELECT reservation_id FROM ReservationRegister WHERE user_id = (SELECT id FROM User WHERE first_name = 'Charlie')), 
 (SELECT id FROM Equipment WHERE name = 'Raspberry'), UUID(), 3);


INSERT INTO RentalEquipment (rental_id, equipment_id, technical_status, identifier, quantity)
VALUES
(
   (SELECT id FROM RentalRegister WHERE user_id = (SELECT id FROM User WHERE first_name = 'Alice') AND equipment_id = (SELECT id FROM Equipment WHERE name = 'Laptop') LIMIT 1),
   (SELECT id FROM Equipment WHERE name = 'Laptop'),
   1, UUID(), 2
),
(
   (SELECT id FROM RentalRegister WHERE user_id = (SELECT id FROM User WHERE first_name = 'Bob') AND equipment_id = (SELECT id FROM Equipment WHERE name = 'Drill') LIMIT 1),
   (SELECT id FROM Equipment WHERE name = 'Drill'),
   2, UUID(), 1 
);
