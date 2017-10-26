CREATE TABLE `aa`.`payroll` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title`         VARCHAR(50)     NOT NULL,
  `total_amount`  BIGINT UNSIGNED NOT NULL,
  `head_count`    BIGINT UNSIGNED NOT NULL,
  `status`        VARCHAR(16)     NOT NULL DEFAULT 'PENDING',
  `remark`        VARCHAR(400),
  `grant_time`    DATETIME,
  `created_time`  DATETIME        NOT NULL,
  `updated_time`  DATETIME,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;


CREATE TABLE `aa`.`payroll_detail` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `payroll_id`    BIGINT UNSIGNED NOT NULL,
  `user_name`     VARCHAR(50)     NOT NULL,
  `mobile`        VARCHAR(15)     NOT NULL,
  `amount`        BIGINT UNSIGNED NOT NULL,
  `status`        VARCHAR(16),
  `created_time`  DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_PAYROLL_DETAIL_PAYROLL_ID_REF_PAYROLL_ID FOREIGN KEY (`payroll_id`) REFERENCES `aa`.`payroll` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;