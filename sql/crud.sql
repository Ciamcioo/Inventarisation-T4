-- ================================
-- CREATE - Wstawianie nowych danych
-- ================================

INSERT INTO user (id, first_name, last_name, role_id, hire_date) VALUES
(UUID(), 'Jan', 'Kowalski', 1, '2024-12-12');

INSERT INTO equipment (id, name, quantity, description, technical_status_id, status_id, category_id, location_id) VALUES
(UUID(), 'Laptop Lenovo', 3, 'laptop', 1, 1, 1, 1);

INSERT INTO rental_register (id, user_id, equipment_id, start_date, end_date) VALUES
(UUID(), (SELECT id FROM user WHERE first_name = 'Jan'),
(SELECT id FROM equipment WHERE name = 'Laptop Lenovo'), '2024-01-01', '2024-01-10');

INSERT INTO reservation_register (reservation_id, user_id, timeout, start_date, end_date, equipment_id) VALUES
(UUID(), (SELECT id FROM user WHERE first_name = 'Jan'), '2024-03-01', '2024-03-01', '2024-03-07', 
 (SELECT id FROM equipment WHERE name = 'Laptop Lenovo'));

-- ================================
-- READ - Pobieranie danych
-- ================================

-- Pobierz wszystkich użytkowników
SELECT * FROM user;

-- Pobierz dostępny sprzęt w lokalizacji 1
SELECT eq.name, s.name, location_id FROM equipment eq
JOIN status s ON eq.status_id = s.id
WHERE location_id = 1;

-- Pobierz historię wypożyczeń użytkownika
SELECT rr.id, rr.start_date, rr.end_date, eq.name, u.first_name
FROM rental_register rr
JOIN rental_equipment re ON rr.id = re.rental_id
JOIN equipment eq ON re.equipment_id = eq.id
JOIN user u ON rr.user_id = u.id
WHERE u.first_name = 'Alice';

-- Pobierz szczegóły rezerwacji dla danego sprzętu
SELECT rr.reservation_id, rr.start_date, eq.name
FROM reservation_register rr
JOIN equipment eq ON rr.equipment_id = eq.id
WHERE eq.name = 'Laptop Lenovo';

-- ================================
-- UPDATE - Aktualizacja danych
-- ================================

-- Zmień status sprzętu na „rented”
UPDATE equipment
SET status_id = 3
WHERE name = 'Laptop';

-- Zaktualizuj numer telefonu użytkownika
UPDATE contact_user_information c
JOIN user u ON c.user_id = u.id
SET c.phone_number = '+48111222333'
WHERE u.first_name = 'Jan';

-- ================================
-- DELETE - Usuwanie danych
-- ================================

-- Usuń użytkownika wraz z powiązanymi danymi
DELETE FROM user
WHERE first_name = 'Jan';

-- Usuń sprzęt
DELETE FROM equipment
WHERE name = 'Laptop';

