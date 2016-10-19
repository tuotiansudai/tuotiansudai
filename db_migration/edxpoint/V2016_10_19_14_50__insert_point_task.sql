SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE `point_task`;
insert into `point_task`(`id`,`name`,`point`,`multiple`,`active`,`max_level`,`created_time`) values
('1','REGISTER','100','0','1','1','2016-03-16 20:15:34'),
('2','BIND_EMAIL','50','0','0','1','2016-03-16 20:15:34'),
('3','BIND_BANK_CARD','100','0','1','1','2016-03-16 20:15:34'),
('4','FIRST_RECHARGE','200','0','1','1','2016-03-16 20:15:34'),
('5','FIRST_INVEST','200','0','1','1','2016-03-16 20:15:34'),
('6','SUM_INVEST_10000','500','0','0','1','2016-03-16 20:15:34'),
('7','EACH_SUM_INVEST','5000','0','1','1','2016-07-18 15:37:02'),
('8','FIRST_SINGLE_INVEST','10000','0','1','1','2016-07-18 15:37:02'),
('9','EACH_RECOMMEND','50','0','1','1','2016-07-18 15:37:02'),
('10','EACH_REFERRER_INVEST','1000','0','0','1','2016-07-18 15:37:02'),
('11','FIRST_REFERRER_INVEST','50','0','1','1','2016-07-18 15:37:02'),
('12','FIRST_INVEST_180','50','0','1','1','2016-07-18 15:37:03'),
('13','FIRST_INVEST_360','200','0','1','1','2016-07-18 15:37:03'),
('14','FIRST_TURN_ON_NO_PASSWORD_INVEST','100','0','1','1','2016-07-18 15:37:03'),
('15','FIRST_TURN_ON_AUTO_INVEST','50','0','1','1','2016-07-18 15:37:03');
SET FOREIGN_KEY_CHECKS = 1;

