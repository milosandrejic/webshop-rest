CREATE DATABASE webshop_rest_dev;
CREATE DATABASE webshop_rest_prod;

CREATE USER 'webshop_dev_user'@'localhost' IDENTIFIED BY 'password';
CREATE USER 'webshop_prod_user'@'localhost' IDENTIFIED BY 'password';

GRANT SELECT ON webshop_rest_dev.* TO 'webshop_dev_user'@'localhost';
GRANT UPDATE ON webshop_rest_dev.* TO 'webshop_dev_user'@'localhost';
GRANT INSERT ON webshop_rest_dev.* TO 'webshop_dev_user'@'localhost';
GRANT DELETE ON webshop_rest_dev.* TO 'webshop_dev_user'@'localhost';

GRANT SELECT ON webshop_rest_prod.* TO 'webshop_prod_user'@'localhost';
GRANT UPDATE ON webshop_rest_prod.* TO 'webshop_prod_user'@'localhost';
GRANT INSERT ON webshop_rest_prod.* TO 'webshop_prod_user'@'localhost';
GRANT DELETE ON webshop_rest_prod.* TO 'webshop_prod_user'@'localhost';




