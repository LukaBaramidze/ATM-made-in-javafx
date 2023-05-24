create database if not exists ATM;
use ATM;
drop table if exists cards;
create table cards(
	card_num varchar(50),
    card_cvv varchar(50),
    card_name varchar(50),
    card_date varchar(50),
    card_balance double
)