
-- Delete the entire database
DROP DATABASE IF EXISTS baza;

DROP USER IF EXISTS 'admin'@'localhost';
DROP USER IF EXISTS 'zarzad'@'localhost';
DROP USER IF EXISTS 'mosowicz'@'localhost';
DROP USER IF EXISTS 'recruit'@'localhost';

DROP ROLE IF EXISTS 'Admin';
DROP ROLE IF EXISTS 'Management';
DROP ROLE IF EXISTS 'MOS';
DROP ROLE IF EXISTS 'Recruit';

