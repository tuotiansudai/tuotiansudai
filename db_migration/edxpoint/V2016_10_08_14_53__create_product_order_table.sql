CREATE TABLE `edxpoint`.`product_order` (
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
  CONSTRAINT `FK_PRODUCT_ORDER_PRODUCT_ID` FOREIGN KEY (`product_id`) REFERENCES `edxpoint`.`product` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8;