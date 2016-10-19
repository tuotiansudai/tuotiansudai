SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE `point_prize`;
insert into `point_prize`(`id`,`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) values
('100001','JapanKoreaTravel','日韩游',null,null,'0','1'),
('100002','HKMacaoTravel','港澳游',null,null,'0','0'),
('100003','Iphone6sPlus','iphone6s plus',null,null,'0','1'),
('100004','ThankYou','谢谢参与',null,null,'44','0'),
('100005','InterestCoupon0.2','0.2%加息券','301',null,'26','1'),
('100007','Cash5','现金5元',null,'500','3','0'),
('100008','Cash2','现金2元',null,'200','2','0'),
('100009','TtsdUDisk','拓天速贷U盘',null,null,'2','1'),
('100010','MangoTravel100','100元芒果旅游卡',null,null,'2','1'),
('100011','InsulationCup','青花瓷保温杯',null,null,'5','1'),
('100012','InterestCoupon2','2%加息券','303',null,'5','1'),
('100013','RedEnvelope2','2元红包','304',null,'60','1');
SET FOREIGN_KEY_CHECKS = 1;

