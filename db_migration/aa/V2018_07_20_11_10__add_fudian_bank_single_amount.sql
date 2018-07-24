begin;

ALTER TABLE `aa`.`bank` ADD COLUMN `is_bank` TINYINT(1) DEFAULT FALSE;

insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('邮储银行', '100', '/images/bank/100.jpg', 500000, 500000, 'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('中国工商银行', '102', '/images/bank/102.jpg', 5000000, 5000000, 'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('中国农业银行', '103', '/images/bank/103.jpg', 5000000, 5000000,'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('中国银行', '104', '/images/bank/104.jpg', 5000000, 5000000,'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('中国建设银行', '105', '/images/bank/105.jpg', 20000000, 20000000,'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('交通银行', '301', '/images/bank/301.jpg', 990000, 9990000,'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('中信银行', '302', '/images/bank/302.jpg', 500000, 500000,'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('中国光大银行', '303', '/images/bank/303.jpg', 5000000, 5000000, 'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('华夏银行', '304', '/images/bank/304.jpg', 0, 0,'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('中国民生银行', '305', '/images/bank/305.jpg', 5000000, 5000000,'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('广发银行', '306', '/images/bank/306.jpg', 5000000, 5000000,'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('平安银行', '307', '/images/bank/307.jpg', 5000000, 5000000, 'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('招商银行', '308', '/images/bank/308.jpg', 100000, 1000000, 'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('兴业银行', '309', '/images/bank/309.jpg', 5000000, 5000000, 'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('浦东发展银行', '310', '/images/bank/310.jpg', 5000000, 5000000, 'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('恒丰银行', '311', '/images/bank/311.jpg', 5000000, 5000000, 'sidneygao', now(), 1);
insert into bank(name,bank_code,image_url,single_amount,single_day_amount,created_by,created_time,is_bank) values('富滇银行', '313', '/images/bank/313.jpg', 5000000, 5000000, 'sidneygao', now(), 1);

COMMIT;