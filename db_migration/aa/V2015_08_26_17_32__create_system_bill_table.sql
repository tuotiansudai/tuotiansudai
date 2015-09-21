CREATE TABLE `aa`.`system_bill` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id`      VARCHAR(32),
  `amount`        BIGINT UNSIGNED NOT NULL,
  `type`          VARCHAR(8),
  `detail`        VARCHAR(200),
  `business_type` VARCHAR(32)     NOT NULL,
  `created_time`  DATETIME        NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8