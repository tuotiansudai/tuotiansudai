CREATE TABLE `aa`.`point_prize` (
  `id`                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`               VARCHAR(100) NULL,
  `description`         VARCHAR(25) NULL,
  `coupon_id`         BIGINT UNSIGNED NULL,
  `cash`               BIGINT UNSIGNED NULL,
  `probability`       BIGINT UNSIGNED NOT NULL,
  `active`             TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_POINT_PRIZE_COUPON_ID_REF_COUPON_ID FOREIGN KEY (`coupon_id`) REFERENCES `aa`.`coupon` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`user_point_prize` (
  `point_prize_id`    BIGINT UNSIGNED NOT NULL,
  `login_name`         VARCHAR(25) NOT NULL,
  `created_time`       DATETIME NOT NULL,
  `reality`            TINYINT(1) not null,
  CONSTRAINT FK_USER_POINT_PRIZE_POINT_PRIZE_ID_REF_POINT_PRIZE_ID FOREIGN KEY (`point_prize_id`) REFERENCES `aa`.`point_prize` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

BEGIN ;
INSERT INTO `point_prize` (`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('JapanKoreaTravel','日韩游',NULL,NULL,0,TRUE);
INSERT INTO `point_prize` (`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('HKMacaoTravel','港澳游',NULL,NULL,0,TRUE);
INSERT INTO `point_prize` (`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('Iphone6sPlus','iphone6s plus',NULL,NULL,0,TRUE);
INSERT INTO `point_prize` (`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('ThankYou','谢谢参与',NULL,NULL,69,TRUE);
INSERT INTO `point_prize` (`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) SELECT 'InterestCoupon2','0.2%加息券' ,301,NULL,25,TRUE FROM dual where EXISTS(SELECT * FROM coupon where id = 301);
INSERT INTO `point_prize` (`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) SELECT 'InvestCoupon3000','3000元体验金' ,302,NULL,1,TRUE FROM dual where EXISTS(SELECT * FROM coupon where id = 302);
INSERT INTO `point_prize` (`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('Cash5','现金5元',NULL,500,3,TRUE);
INSERT INTO `point_prize` (`name`,`description`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('Cash2','现金2元',NULL,200,2,TRUE);
COMMIT ;
