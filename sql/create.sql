CREATE DATABASE IF NOT EXISTS baza;
USE baza;

CREATE TABLE Role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE Location (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES Role(id)
);

CREATE TABLE User (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    Role_ID INT NOT NULL,
    hire_date DATE,
    FOREIGN KEY (Role_ID) REFERENCES Role(id)
);

CREATE TABLE ContactUserInformation (
    user_id UUID PRIMARY KEY,
    Phone_Number VARCHAR(255) NOT NULL,
    Email VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE TechnicalStatus (
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE Status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE Category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE Equipment (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity INT,
    description VARCHAR(255),
    technical_status_id INT,
    status_id INT,
    category_id INT,
    location_id INT,
    FOREIGN KEY (technical_status_id) REFERENCES TechnicalStatus(id),
    FOREIGN KEY (status_id) REFERENCES Status(id),
    FOREIGN KEY (category_id) REFERENCES Category(id),
    FOREIGN KEY (location_id) REFERENCES Location(location_id)
);

CREATE TABLE RentalRegister (
    id UUID PRIMARY KEY,
    user_id UUID,
    equipment_id UUID,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES Equipment(id) ON DELETE CASCADE
);

CREATE TABLE ReservationRegister (
    reservation_id UUID PRIMARY KEY,
    user_id UUID,
    timeout DATE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    equipment_id UUID,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES Equipment(id) ON DELETE CASCADE
);

CREATE TABLE ReservationEquipment (
    reservation_id UUID,
    equipment_id UUID,
    identifier UUID,
    quantity INT,
    PRIMARY KEY (reservation_id, equipment_id),
    FOREIGN KEY (reservation_id) REFERENCES ReservationRegister(reservation_id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES Equipment(id) ON DELETE CASCADE
);

CREATE TABLE RentalEquipment (
    rental_id UUID NOT NULL,
    equipment_id UUID NOT NULL,
    technical_status INT NOT NULL,
    identifier UUID NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (rental_id, equipment_id),
    FOREIGN KEY (rental_id) REFERENCES RentalRegister(id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES Equipment(id) ON DELETE CASCADE
);

DELIMITER $$
CREATE TRIGGER block_cascade_delete_role
BEFORE DELETE ON Role
FOR EACH ROW 
BEGIN
  SIGNAL SQLSTATE '45000'
  SET MESSAGE_TEXT = 'Cascade delete is not allowed on Role';
END$$
DELIMITER ;
