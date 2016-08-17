CREATE TABLE `aa`.`product_order` (
  `id`                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `goods_id`          BIGINT UNSIGNED NOT NULL,
  `product_price`     BIGINT UNSIGNED NOT NULL,
  `num`               BIGINT UNSIGNED NOT NULL,
  `total_price`       BIGINT UNSIGNED NOT NULL,
  `real_name`         VARCHAR(50) NOT NULL,
  `mobile`            VARCHAR(18) NOT NULL,
  `address`           VARCHAR(200) NOT NULL,
  `consignment`       TINYINT(1)  NOT NULL,
  `consignment_time`  DATETIME,
  `created_by`        VARCHAR(25),
  `created_time`      DATETIME,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_PRODUCT_ORDER_CREATED_BY_LOGIN_NAME` FOREIGN KEY (`created_by`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8;