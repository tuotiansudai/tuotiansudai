CREATE TABLE `aa`.`invest` (
  `id`             BIGINT UNSIGNED NOT NULL,
  `login_name`     VARCHAR(25)     NOT NULL,
  `loan_id`        BIGINT UNSIGNED NOT NULL,
  `amount`         BIGINT UNSIGNED NOT NULL,
  `status`         VARCHAR(16)     NOT NULL,
  `source`         VARCHAR(16)     NOT NULL, /* ios,android端投资|WEB端投资 */
  `is_auto_invest` BOOLEAN         NOT NULL  DEFAULT FALSE,
  `created_time`   DATETIME        NOT NULL, /* 创建时间 */
  PRIMARY KEY (`id`),
  CONSTRAINT FK_INVEST_LOGIN_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_INVEST_LOAN_ID_REF_LOAN FOREIGN KEY (`loan_id`) REFERENCES `aa`.`loan` (`id`),
  INDEX INDEX_INVEST_STATUS (`status`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
