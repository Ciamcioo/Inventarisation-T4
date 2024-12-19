
CREATE ROLE Admin;
CREATE ROLE Management;
CREATE ROLE MOS;
CREATE ROLE Recruit;

CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
CREATE USER 'zarzad'@'localhost' IDENTIFIED BY 'zarzad';
CREATE USER 'mosowicz'@'localhost' IDENTIFIED BY 'mosowicz';
CREATE USER 'recruit'@'localhost' IDENTIFIED BY 'recruit';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, DROP ON baza.* TO Admin;

GRANT SELECT ON User TO Management;
GRANT SELECT, INSERT, UPDATE, DELETE ON RentalRegister TO Management;
GRANT SELECT, INSERT, UPDATE, DELETE ON ReservationRegister TO Management;
GRANT SELECT, INSERT, UPDATE, DELETE ON RentalEquipment TO Management;
GRANT SELECT, INSERT, UPDATE, DELETE ON ReservationEquipment TO Management;
GRANT SELECT, INSERT, UPDATE ON Equipment TO Management;
GRANT SELECT, INSERT, UPDATE ON TechnicalStatus TO Management;
GRANT SELECT ON Status TO Management;
GRANT SELECT, INSERT ON Category TO Management;
GRANT SELECT, INSERT ON Location TO Management;

GRANT SELECT ON RentalRegister TO MOS;
GRANT SELECT ON ReservationRegister TO MOS;
GRANT SELECT ON RentalEquipment TO MOS;
GRANT SELECT ON ReservationEquipment TO MOS;
GRANT SELECT ON Equipment TO MOS;
GRANT SELECT, UPDATE ON TechnicalStatus TO MOS;
GRANT SELECT ON Status TO MOS;
GRANT SELECT ON Location TO MOS;

GRANT SELECT ON Equipment TO Recruit;
GRANT SELECT, INSERT ON ReservationRegister TO Recruit;
GRANT SELECT ON ReservationEquipment TO Recruit;
GRANT SELECT ON Location TO Recruit;
GRANT SELECT ON Status TO Recruit;

GRANT Admin TO 'admin'@'localhost';
GRANT Management TO 'zarzad'@'localhost';
GRANT MOS TO 'mosowicz'@'localhost';
GRANT Recruit TO 'recruit'@'localhost';

SET DEFAULT ROLE Admin FOR 'admin'@'localhost';
SET DEFAULT ROLE Management FOR 'zarzad'@'localhost';
SET DEFAULT ROLE MOS FOR 'mosowicz'@'localhost';
SET DEFAULT ROLE Recruit FOR 'recruit'@'localhost';

FLUSH PRIVILEGES;

