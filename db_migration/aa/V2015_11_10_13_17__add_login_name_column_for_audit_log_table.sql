DROP TABLE IF EXISTS `aa`.`user_audit_log`;
CREATE TABLE `aa`.`audit_log` (
  `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`          VARCHAR(25)     NOT NULL,
  `operator_login_name` VARCHAR(25)     NOT NULL,
  `ip`                  VARCHAR(15)     NOT NULL,
  `operation_time`      DATETIME        NOT NULL,
  `description`         TEXT,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_AUDIT_LOG_OPERATOR_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_AUDIT_LOG_OPERATOR_OPERATOR_REF_USER_LOGIN_NAME FOREIGN KEY (`operator_login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;