CREATE DATABASE IF NOT EXISTS baza;
USE baza;

CREATE TABLE role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE location (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE user (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    hire_date DATE,
    FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE contact_user_information (
    user_id UUID PRIMARY KEY,
    phone_number VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE technical_status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE equipment (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity INT,
    description VARCHAR(255),
    technical_status_id INT,
    status_id INT,
    category_id INT,
    location_id INT,
    FOREIGN KEY (technical_status_id) REFERENCES technical_status(id),
    FOREIGN KEY (status_id) REFERENCES status(id),
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (location_id) REFERENCES location(location_id)
);

CREATE TABLE rental_register (
    id UUID PRIMARY KEY,
    user_id UUID,
    equipment_id UUID,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE
);

CREATE TABLE reservation_register (
    reservation_id UUID PRIMARY KEY,
    user_id UUID,
    timeout DATE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    equipment_id UUID,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE
);

CREATE TABLE reservation_equipment (
    reservation_id UUID,
    equipment_id UUID,
    identifier UUID,
    quantity INT,
    PRIMARY KEY (reservation_id, equipment_id),
    FOREIGN KEY (reservation_id) REFERENCES reservation_register(reservation_id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE
);

CREATE TABLE rental_equipment (
    rental_id UUID NOT NULL,
    equipment_id UUID NOT NULL,
    technical_status INT NOT NULL,
    identifier UUID NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (rental_id, equipment_id),
    FOREIGN KEY (rental_id) REFERENCES rental_register(id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE
);

DELIMITER $$
CREATE TRIGGER block_cascade_delete_role
BEFORE DELETE ON role
FOR EACH ROW 
BEGIN
  SIGNAL SQLSTATE '45000'
  SET MESSAGE_TEXT = 'Cascade delete is not allowed on role';
END$$
DELIMITER ;
