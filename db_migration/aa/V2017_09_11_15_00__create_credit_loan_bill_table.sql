CREATE TABLE `aa`.`credit_loan_bill` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id`      BIGINT UNSIGNED,
  `amount`        BIGINT UNSIGNED NOT NULL,
  `operation_type` VARCHAR(8)     NOT NULL,
  `business_type` VARCHAR(32)     NOT NULL,
  `detail`        TEXT,
  `created_time`  DATETIME        NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8