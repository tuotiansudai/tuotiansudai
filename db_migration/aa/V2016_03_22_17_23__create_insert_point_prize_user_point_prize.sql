CREATE TABLE `aa`.`point_prize` (
  `id`                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`               VARCHAR(25) NULL,
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
  PRIMARY KEY (`point_prize_id`,`login_name`),
  CONSTRAINT FK_USER_POINT_PRIZE_POINT_PRIZE_ID_REF_POINT_PRIZE_ID FOREIGN KEY (`point_prize_id`) REFERENCES `aa`.`point_prize` (`id`),
  CONSTRAINT FK_USER_POINT_PRIZE_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

BEGIN ;
INSERT INTO `point_prize` (`name`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('日韩游',NULL,NULL,0,TRUE);
INSERT INTO `point_prize` (`name`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('港澳游',NULL,NULL,0,TRUE);
INSERT INTO `point_prize` (`name`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('iphone6s plus',NULL,NULL,0,TRUE);
INSERT INTO `point_prize` (`name`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('谢谢参与',NULL,NULL,69,TRUE);
INSERT INTO `point_prize` (`name`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('0.2%加息券' ,301,NULL,94,TRUE);
INSERT INTO `point_prize` (`name`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('3000元体验金' ,302,NULL,95,TRUE);
INSERT INTO `point_prize` (`name`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('现金5元',NULL,500,98,TRUE);
INSERT INTO `point_prize` (`name`,`coupon_id`,`cash`,`probability`,`active`) VALUE ('现金2元',NULL,200,100,TRUE);
COMMIT ;
