CREATE TABLE `aa`.`product` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `goods_type`    VARCHAR(10) NOT NULL,
  `product_name`  VARCHAR(100) NOT NULL,
  `seq`           BIGINT UNSIGNED NOT NULL,
  `image_url`     VARCHAR(200) NOT NULL,
  `description`   VARCHAR(500) NOT NULL,
  `total_count`   BIGINT UNSIGNED NOT NULL DEFAULT 0,
  `used_count`    BIGINT UNSIGNED NOT NULL DEFAULT 0,
  `product_price` BIGINT UNSIGNED NOT NULL DEFAULT 0,
  `start_time`    DATETIME  NOT NULL,
  `end_time`      DATETIME  NOT NULL,
  `active`        TINYINT(1)  NOT NULL DEFAULT 0,
  `created_by`  VARCHAR(25) NOT NULL,
  `created_time`  DATETIME NOT NULL,
  `active_by`   VARCHAR(25),
  `active_time`   DATETIME,
  `updated_by`  VARCHAR(25),
  `updated_time`  DATETIME,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_PRODUCT_CREATED_BY_LOGIN_NAME` FOREIGN KEY (`created_by`) REFERENCES  `aa`.`user` (`login_name`),
  CONSTRAINT `FK_PRODUCT_ACTIVE_BY_LOGIN_NAME` FOREIGN KEY (`created_by`) REFERENCES  `aa`.`user` (`login_name`),
  CONSTRAINT `FK_PRODUCT_UPDATED_BY_LOGIN_NAME` FOREIGN KEY (`created_by`) REFERENCES  `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8;