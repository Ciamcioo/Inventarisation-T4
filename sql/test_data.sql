INSERT INTO role (name) VALUES 
('Admin'), 
('Zarzad'), 
('Mosowicz'),
('Rekrut');

INSERT INTO location (name, role_id) VALUES 
('T4', 1),
('T4', 2),
('T4', 3),
('T4', 4);

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

