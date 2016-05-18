BEGIN ;

UPDATE coupon SET product_types = 'SYL,WYX,JYF' WHERE id = 301;

INSERT INTO coupon
  SELECT
    303,
    0,
    0.02,
    0,
    1,
    '2016-04-01 00:00:00',
    '2016-07-31 23:59:59',
    0,
    2147483647,
    0,
    TRUE,
    FALSE,
    'sidneygao',
    '2016-04-01 00:00:00',
    NULL,
    NULL,
    NULL,
    NULL,
    0,
    'SYL,WYX,JYF',
    'INTEREST_COUPON',
    'WINNER',
    0,
    0
  FROM dual WHERE EXISTS (SELECT * from `user` where login_name = 'sidneygao') ;

INSERT INTO coupon
  SELECT
    304,
    200,
    0,
    0,
    1,
    '2016-04-01 00:00:00',
    '2016-07-31 23:59:59',
    0,
    2147483647,
    0,
    TRUE,
    FALSE,
    'sidneygao',
    '2016-04-01 00:00:00',
    NULL,
    NULL,
    NULL,
    NULL,
    0,
    'SYL,WYX,JYF',
    'RED_ENVELOPE',
    'WINNER',
    0,
    0
  FROM dual WHERE EXISTS (SELECT * from `user` where login_name = 'sidneygao') ;

UPDATE `point_prize` SET `active` = FALSE WHERE `name` IN ('HKMacaoTravel','InvestCoupon3000','Cash5','ThankYou','Cash2');

UPDATE
  `point_prize`
SET
  `name` = 'InterestCoupon0.2',
  `probability` = 26
WHERE `name` = 'InterestCoupon2' ;

INSERT INTO `point_prize` (`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) VALUES ('TtsdUDisk','拓天速贷U盘',NULL,NULL,2,TRUE);
INSERT INTO `point_prize` (`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) VALUES ('MangoTravel100','100元芒果旅游卡',NULL,NULL,2,TRUE);
INSERT INTO `point_prize` (`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) VALUES ('InsulationCup','青花瓷保温杯',NULL,NULL,5,TRUE);
INSERT INTO `point_prize` (`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) VALUES ('InterestCoupon2','2%加息券',303,NULL,5,TRUE);
INSERT INTO `point_prize` (`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) VALUES ('RedEnvelope2','2元红包',304,NULL,60,TRUE);

COMMIT ;