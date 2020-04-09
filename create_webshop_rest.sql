create table _order (order_id bigint not null auto_increment, order_date datetime, total_amount double precision, order_number_generator_id bigint, user_id bigint, primary key (order_id)) engine=InnoDB
create table address (address_id bigint not null auto_increment, street varchar(255) not null, street_number bigint, city_id bigint, primary key (address_id)) engine=InnoDB
create table city (city_id bigint not null auto_increment, city_name varchar(255) not null, country_id bigint, primary key (city_id)) engine=InnoDB
create table country (country_id bigint not null auto_increment, country_name varchar(255), primary key (country_id)) engine=InnoDB
create table item (item_id bigint not null auto_increment, description text, discount double precision, name varchar(255), price double precision, qty bigint, unit_of_measure varchar(255), item_category_id bigint, item_level_id bigint, primary key (item_id)) engine=InnoDB
create table item_category (item_category_id bigint not null auto_increment, item_category varchar(255), primary key (item_category_id)) engine=InnoDB
create table item_level (item_level_id bigint not null auto_increment, level varchar(255), primary key (item_level_id)) engine=InnoDB
create table order_item (order_item_id bigint not null auto_increment, order_item_name varchar(255), order_item_price double precision, ordered_qty bigint, total_item_price double precision, primary key (order_item_id)) engine=InnoDB
create table order_item_order (order_item_order_id bigint not null auto_increment, order_id bigint, order_item_id bigint, primary key (order_item_order_id)) engine=InnoDB
create table order_number_generator (order_number bigint not null, primary key (order_number)) engine=InnoDB
create table order_number_seq (next_val bigint) engine=InnoDB
insert into order_number_seq values ( 1000000000 )
create table role (role_id bigint not null auto_increment, role varchar(255), primary key (role_id)) engine=InnoDB
create table shopping_cart (shopping_cart_id bigint not null auto_increment, total_amount double precision, user_id bigint, primary key (shopping_cart_id)) engine=InnoDB
create table shopping_cart_item (shopping_cart_item_id bigint not null auto_increment, ordered_qty bigint, total_item_price double precision, item_id bigint, shopping_cart_id bigint, primary key (shopping_cart_item_id)) engine=InnoDB
create table token (token_id bigint not null auto_increment, created bigint, expiration bigint, token varchar(255), primary key (token_id)) engine=InnoDB
create table user (user_id bigint not null auto_increment, first_name varchar(255) not null, last_name varchar(255) not null, dob date not null, email varchar(255) not null, password varchar(255) not null, phone_number varchar(255) not null, total_spent double precision, username varchar(255) not null, address_id bigint, role_id bigint, token_id bigint, primary key (user_id)) engine=InnoDB
alter table city add constraint UK_djnk44fptegbsu6drhc9xvlfj unique (city_name)
alter table country add constraint UK_qrkn270121aljmucrdbnmd35p unique (country_name)
alter table user add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email)
alter table user add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username)
alter table _order add constraint FKbflijfsx3uwbvdfmkrr2qnmf2 foreign key (order_number_generator_id) references order_number_generator (order_number)
alter table _order add constraint FKaouc4j3tavb2vfvvl0292kgir foreign key (user_id) references user (user_id)
alter table address add constraint FKpo044ng5x4gynb291cv24vtea foreign key (city_id) references city (city_id)
alter table city add constraint FKrpd7j1p7yxr784adkx4pyepba foreign key (country_id) references country (country_id)
alter table item add constraint FKasph99xmslm0pmfyyc8ga2iyr foreign key (item_category_id) references item_category (item_category_id)
alter table item add constraint FK5qdre52e6he7k7m1srmlx2ub4 foreign key (item_level_id) references item_level (item_level_id)
alter table order_item_order add constraint FK1b84dnqh0dkmit230gbajc9ph foreign key (order_id) references _order (order_id)
alter table order_item_order add constraint FKh4i8gw3e3y53hhi9ip0gutd8w foreign key (order_item_id) references order_item (order_item_id)
alter table shopping_cart add constraint FK254qp5akhuaaj9n5co4jww3fk foreign key (user_id) references user (user_id)
alter table shopping_cart_item add constraint FKkyhotp7m7j9fop2u7nst8msdv foreign key (item_id) references item (item_id)
alter table shopping_cart_item add constraint FKtaxfo8drwlxjtg1f1y9h4t5t2 foreign key (shopping_cart_id) references shopping_cart (shopping_cart_id)
alter table user add constraint FKddefmvbrws3hvl5t0hnnsv8ox foreign key (address_id) references address (address_id)
alter table user add constraint FKn82ha3ccdebhokx3a8fgdqeyy foreign key (role_id) references role (role_id)
alter table user add constraint FKr7pd8gnabslvvptf7rvb4jij4 foreign key (token_id) references token (token_id)
create table _order (order_id bigint not null auto_increment, order_date datetime, total_amount double precision, order_number_generator_id bigint, user_id bigint, primary key (order_id)) engine=InnoDB
create table address (address_id bigint not null auto_increment, street varchar(255) not null, street_number bigint, city_id bigint, primary key (address_id)) engine=InnoDB
create table city (city_id bigint not null auto_increment, city_name varchar(255) not null, country_id bigint, primary key (city_id)) engine=InnoDB
create table country (country_id bigint not null auto_increment, country_name varchar(255), primary key (country_id)) engine=InnoDB
create table item (item_id bigint not null auto_increment, description text, discount double precision, name varchar(255), price double precision, qty bigint, unit_of_measure varchar(255), item_category_id bigint, item_level_id bigint, primary key (item_id)) engine=InnoDB
create table item_category (item_category_id bigint not null auto_increment, item_category varchar(255), primary key (item_category_id)) engine=InnoDB
create table item_level (item_level_id bigint not null auto_increment, level varchar(255), primary key (item_level_id)) engine=InnoDB
create table order_item (order_item_id bigint not null auto_increment, order_item_name varchar(255), order_item_price double precision, ordered_qty bigint, total_item_price double precision, primary key (order_item_id)) engine=InnoDB
create table order_item_order (order_item_order_id bigint not null auto_increment, order_id bigint, order_item_id bigint, primary key (order_item_order_id)) engine=InnoDB
create table order_number_generator (order_number bigint not null, primary key (order_number)) engine=InnoDB
create table order_number_seq (next_val bigint) engine=InnoDB
insert into order_number_seq values ( 1000000000 )
create table role (role_id bigint not null auto_increment, role varchar(255), primary key (role_id)) engine=InnoDB
create table shopping_cart (shopping_cart_id bigint not null auto_increment, total_amount double precision, user_id bigint, primary key (shopping_cart_id)) engine=InnoDB
create table shopping_cart_item (shopping_cart_item_id bigint not null auto_increment, ordered_qty bigint, total_item_price double precision, item_id bigint, shopping_cart_id bigint, primary key (shopping_cart_item_id)) engine=InnoDB
create table token (token_id bigint not null auto_increment, created bigint, expiration bigint, token varchar(255), primary key (token_id)) engine=InnoDB
create table user (user_id bigint not null auto_increment, first_name varchar(255) not null, last_name varchar(255) not null, dob date not null, email varchar(255) not null, password varchar(255) not null, phone_number varchar(255) not null, total_spent double precision, username varchar(255) not null, address_id bigint, role_id bigint, token_id bigint, primary key (user_id)) engine=InnoDB
alter table city add constraint UK_djnk44fptegbsu6drhc9xvlfj unique (city_name)
alter table country add constraint UK_qrkn270121aljmucrdbnmd35p unique (country_name)
alter table user add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email)
alter table user add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username)
alter table _order add constraint FKbflijfsx3uwbvdfmkrr2qnmf2 foreign key (order_number_generator_id) references order_number_generator (order_number)
alter table _order add constraint FKaouc4j3tavb2vfvvl0292kgir foreign key (user_id) references user (user_id)
alter table address add constraint FKpo044ng5x4gynb291cv24vtea foreign key (city_id) references city (city_id)
alter table city add constraint FKrpd7j1p7yxr784adkx4pyepba foreign key (country_id) references country (country_id)
alter table item add constraint FKasph99xmslm0pmfyyc8ga2iyr foreign key (item_category_id) references item_category (item_category_id)
alter table item add constraint FK5qdre52e6he7k7m1srmlx2ub4 foreign key (item_level_id) references item_level (item_level_id)
alter table order_item_order add constraint FK1b84dnqh0dkmit230gbajc9ph foreign key (order_id) references _order (order_id)
alter table order_item_order add constraint FKh4i8gw3e3y53hhi9ip0gutd8w foreign key (order_item_id) references order_item (order_item_id)
alter table shopping_cart add constraint FK254qp5akhuaaj9n5co4jww3fk foreign key (user_id) references user (user_id)
alter table shopping_cart_item add constraint FKkyhotp7m7j9fop2u7nst8msdv foreign key (item_id) references item (item_id)
alter table shopping_cart_item add constraint FKtaxfo8drwlxjtg1f1y9h4t5t2 foreign key (shopping_cart_id) references shopping_cart (shopping_cart_id)
alter table user add constraint FKddefmvbrws3hvl5t0hnnsv8ox foreign key (address_id) references address (address_id)
alter table user add constraint FKn82ha3ccdebhokx3a8fgdqeyy foreign key (role_id) references role (role_id)
alter table user add constraint FKr7pd8gnabslvvptf7rvb4jij4 foreign key (token_id) references token (token_id)
