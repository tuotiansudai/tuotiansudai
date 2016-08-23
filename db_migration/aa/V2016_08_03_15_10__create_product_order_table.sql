CREATE TABLE `aa`.`product_order` (
  `id`                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `product_id`        BIGINT UNSIGNED NOT NULL,
  `points`            BIGINT UNSIGNED NOT NULL,
  `num`               BIGINT UNSIGNED NOT NULL,
  `total_points`      BIGINT UNSIGNED NOT NULL,
  `contact`           VARCHAR(50) NOT NULL,
  `mobile`            VARCHAR(18) NOT NULL,
  `address`           VARCHAR(200) NOT NULL,
  `consignment`       TINYINT(1)  NOT NULL,
  `consignment_time`  DATETIME,
  `created_by`        VARCHAR(25),
  `created_time`      DATETIME,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_PRODUCT_ORDER_CREATED_BY_LOGIN_NAME` FOREIGN KEY (`created_by`) REFERENCES `aa`.`user` (`login_name`)
  CONSTRAINT `FK_PRODUCT_ORDER_PRODUCT_ID` FOREIGN KEY (`product_id`) REFERENCES `aa`.`product` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8;