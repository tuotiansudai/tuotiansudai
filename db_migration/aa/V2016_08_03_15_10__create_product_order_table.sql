CREATE TABLE `aa`.`product_order` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `product_id`    BIGINT UNSIGNED NOT NULL,
  `num`           BIGINT UNSIGNED NOT NULL,
  `consignment`   TINYINT(1)  NOT NULL,
  `created_user`  VARCHAR(25) NOT NULL,
  `created_time`  DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_PRODUCT_ORDER_CREATED_BY_LOGIN_NAME` FOREIGN KEY (`created_user`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT `FK_PRODUCT_ORDER_PRODUCT_ID_BY_PRODUCT_ID` FOREIGN KEY (`product_id`) REFERENCES `aa`.`product` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8;