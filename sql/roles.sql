CREATE ROLE admin;
CREATE ROLE management;
CREATE ROLE mos;
CREATE ROLE recruit;

CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
CREATE USER 'zarzad'@'localhost' IDENTIFIED BY 'zarzad';
CREATE USER 'mosowicz'@'localhost' IDENTIFIED BY 'mosowicz';
CREATE USER 'recruit'@'localhost' IDENTIFIED BY 'recruit';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, DROP ON baza.* TO admin;

GRANT SELECT ON user TO management;
GRANT SELECT, INSERT, UPDATE, DELETE ON rental_register TO management;
GRANT SELECT, INSERT, UPDATE, DELETE ON reservation_register TO management;
GRANT SELECT, INSERT, UPDATE, DELETE ON rental_equipment TO management;
GRANT SELECT, INSERT, UPDATE, DELETE ON reservation_equipment TO management;
GRANT SELECT, INSERT, UPDATE ON equipment TO management;
GRANT SELECT, INSERT, UPDATE ON technical_status TO management;
GRANT SELECT ON status TO management;
GRANT SELECT, INSERT ON category TO management;
GRANT SELECT, INSERT ON location TO management;

GRANT SELECT ON rental_register TO mos;
GRANT SELECT ON reservation_register TO mos;
GRANT SELECT ON rental_equipment TO mos;
GRANT SELECT ON reservation_equipment TO mos;
GRANT SELECT ON equipment TO mos;
GRANT SELECT, UPDATE ON technical_status TO mos;
GRANT SELECT ON status TO mos;
GRANT SELECT ON location TO mos;

GRANT SELECT ON equipment TO recruit;
GRANT SELECT, INSERT ON reservation_register TO recruit;
GRANT SELECT ON reservation_equipment TO recruit;
GRANT SELECT ON location TO recruit;
GRANT SELECT ON status TO recruit;

FLUSH PRIVILEGES;
