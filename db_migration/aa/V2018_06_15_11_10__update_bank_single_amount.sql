begin;
TRUNCATE bank;

insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('邮储银行', '100', '/images/bank/PSBC.jpg', 500000, 500000, 'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('中国工商银行', '102', '/images/bank/ICBC.jpg', 5000000, 5000000, 'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('中国农业银行', '103', '/images/bank/ABC.jpg', 5000000, 5000000,'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('中国建设银行', '105', '/images/bank/CCB.jpg', 20000000, 20000000,'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('中国银行', '104', '/images/bank/BOC.jpg', 5000000, 5000000,'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('交通银行', '301', '/images/bank/COMM.jpg', 990000, 9990000,'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('中信银行', '302', '/images/bank/CITIC.jpg', 500000, 500000,'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('中国光大银行', '303', '/images/bank/CEB.jpg', 5000000, 5000000, 'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('华夏银行', '304', '/images/bank/HXB.jpg', 0, 0,'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('中国民生银行', '305', '/images/bank/CMBC.jpg', 5000000, 5000000,'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('广发银行', '306', '/images/bank/GDB.jpg', 5000000, 5000000,'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('平安银行', '307', '/images/bank/SPAB.jpg', 5000000, 5000000, 'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('招商银行', '308', '/images/bank/CMB.jpg', 100000, 1000000, 'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('兴业银行', '309', '/images/bank/CIB.jpg', 5000000, 5000000, 'sidneygao', now());
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time) values('浦东发展银行', '310', '/images/bank/SPDB.jpg', 5000000, 5000000, 'sidneygao', now());

COMMIT;