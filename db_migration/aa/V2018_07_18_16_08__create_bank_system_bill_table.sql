CREATE TABLE `aa`.`bank_system_bill` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `amount`         BIGINT UNSIGNED NOT NULL,
  `operation_type` varchar(8) NOT NULL,
  `detail`         VARCHAR(200),
  `business_type`  VARCHAR(32)     NOT NULL,
  `created_time`   DATETIME        NOT NULL,
  `bank_order_no`   VARCHAR(20)     NOT NULL ,
  `bank_order_date` VARCHAR(8)      NOT NULL,
  PRIMARY KEY (`id`),
  index(`bank_order_no`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;