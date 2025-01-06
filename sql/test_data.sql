INSERT INTO role (name) VALUES 
('Admin'), 
('Manager'), 
('User');

INSERT INTO location (name, role_id) VALUES 
('T4', 1),
('T4', 2),
('T4', 3);

INSERT INTO user (id, first_name, last_name, role_id, hire_date) VALUES 
(UUID(), 'Alice', 'Smith', 1, '2020-01-15'),
(UUID(), 'Bob', 'Johnson', 2, '2021-06-01'),
(UUID(), 'Charlie', 'Brown', 3, '2022-09-12');

INSERT INTO contact_user_information (user_id, phone_number, email) VALUES 
((SELECT id FROM user WHERE first_name = 'Alice'), '555-1234', 'alice@example.com'),
((SELECT id FROM user WHERE first_name = 'Bob'), '555-5678', 'bob@example.com'),
((SELECT id FROM user WHERE first_name = 'Charlie'), '555-9012', 'charlie@example.com');

INSERT INTO technical_status (description) VALUES 
('Good'), 
('Maintenance'), 
('Broken');

INSERT INTO status (name) VALUES 
('Available'), 
('Reserved'), 
('Rented');

INSERT INTO category (name) VALUES 
('Electronics'), 
('Tools');

INSERT INTO equipment (id, name, quantity, description, technical_status_id, status_id, category_id, location_id) VALUES 
(UUID(), 'Laptop', 10, 'High-performance laptops', 1, 1, 1, 1),
(UUID(), 'Raspberry', 5, 'Pico W', 3, 3, 1, 3),
(UUID(), 'Drill', 5, 'Power drills', 2, 2, 2, 2);

INSERT INTO rental_register (id, user_id, equipment_id, start_date, end_date) VALUES 
(UUID(), (SELECT id FROM user WHERE first_name = 'Alice'), 
 (SELECT id FROM equipment WHERE name = 'Laptop'), '2023-01-01', '2023-01-10'),
(UUID(), (SELECT id FROM user WHERE first_name = 'Bob'), 
 (SELECT id FROM equipment WHERE name = 'Drill'), '2023-02-01', '2023-02-05');

INSERT INTO reservation_register (reservation_id, user_id, timeout, start_date, end_date, equipment_id) VALUES 
(UUID(), (SELECT id FROM user WHERE first_name = 'Charlie'), '2023-03-01', '2023-03-01', '2023-03-07', 
 (SELECT id FROM equipment WHERE name = 'Raspberry'));

INSERT INTO reservation_equipment (reservation_id, equipment_id, identifier, quantity) VALUES 
((SELECT reservation_id FROM reservation_register WHERE user_id = (SELECT id FROM user WHERE first_name = 'Charlie')), 
 (SELECT id FROM equipment WHERE name = 'Raspberry'), UUID(), 3);

INSERT INTO rental_equipment (rental_id, equipment_id, technical_status, identifier, quantity)
VALUES
(
   (SELECT id FROM rental_register WHERE user_id = (SELECT id FROM user WHERE first_name = 'Alice') AND equipment_id = (SELECT id FROM equipment WHERE name = 'Laptop') LIMIT 1),
   (SELECT id FROM equipment WHERE name = 'Laptop'),
   1, UUID(), 2
),
(
   (SELECT id FROM rental_register WHERE user_id = (SELECT id FROM user WHERE first_name = 'Bob') AND equipment_id = (SELECT id FROM equipment WHERE name = 'Drill') LIMIT 1),
   (SELECT id FROM equipment WHERE name = 'Drill'),
   2, UUID(), 1 
);

