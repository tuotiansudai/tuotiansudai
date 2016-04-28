CREATE TABLE `aa`.`coupon_user_group` (
  `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `coupon_id`          BIGINT UNSIGNED NOT NULL,
  `user_group`         VARCHAR(32) NOT NULL,
  `user_group_items`  TEXT,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_USER_COUPON_USER_GROUP_COUPON_ID_REF_COUPON_ID FOREIGN KEY (`coupon_id`) REFERENCES `aa`.`coupon` (`id`)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;