drop database if exists xiaolvquan_db;
drop user if exists 'xiaolvquan'@'localhost';
-- 支持emoji：需要mysql数据库参数： character_set_server=utf8mb4
create database xiaolvquan_db default character set utf8mb4 collate utf8mb4_unicode_ci;
use xiaolvquan_db;
create user 'xiaolvquan'@'localhost' identified by '123456';
grant all privileges on xiaolvquan_db.* to 'xiaolvquan'@'localhost';
flush privileges;