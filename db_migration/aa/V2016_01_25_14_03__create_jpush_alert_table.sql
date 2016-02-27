CREATE TABLE `aa`.`jpush_alert` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`         VARCHAR(100)    NOT NULL,
  `push_type`    VARCHAR(100)    NOT NULL,
  `push_objects` VARCHAR(200)             DEFAULT NULL,
  `push_source`  VARCHAR(500)    NOT NULL,
  `status`       VARCHAR(100)             DEFAULT NULL,
  `content`      TEXT            NOT NULL,
  `jump_to`      VARCHAR(30)              DEFAULT NULL,
  `jump_to_link` VARCHAR(100)             DEFAULT NULL,
  `created_time` DATETIME        NOT NULL,
  `created_by`   VARCHAR(25)     NOT NULL,
  `updated_time` DATETIME                 DEFAULT NULL,
  `updated_by`   VARCHAR(25)              DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_JPUSH_ALERT_CREATED_BY_REF_USER_LOGIN_NAME` FOREIGN KEY (`created_by`) REFERENCES `user` (`login_name`),
  CONSTRAINT `FK_JPUSH_ALERT_UPDATED_BY_REF_USER_LOGIN_NAME` FOREIGN KEY (`updated_by`) REFERENCES `user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;