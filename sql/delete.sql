
-- Delete the entire database
DROP DATABASE IF EXISTS baza;

DROP USER IF EXISTS 'admin'@'localhost';
DROP USER IF EXISTS 'zarzad'@'localhost';
DROP USER IF EXISTS 'mosowicz'@'localhost';
DROP USER IF EXISTS 'recruit'@'localhost';

DROP ROLE IF EXISTS 'admin';
DROP ROLE IF EXISTS 'management';
DROP ROLE IF EXISTS 'mos';
DROP ROLE IF EXISTS 'recruit';

