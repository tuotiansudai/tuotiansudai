CREATE TABLE `aa`.`coupon_exchange` (
  `id`                         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `coupon_id`                 BIGINT UNSIGNED NOT NULL,
  `exchange_point`           BIGINT UNSIGNED NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_COUPON_EXCHANGE_COUPON_ID_REF_COUPON_ID FOREIGN KEY (`coupon_id`) REFERENCES `aa`.`coupon` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;