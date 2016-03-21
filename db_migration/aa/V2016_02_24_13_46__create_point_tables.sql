ALTER TABLE `aa`.`account` ADD COLUMN `point` BIGINT UNSIGNED NOT NULL DEFAULT 0
AFTER `freeze`;

CREATE TABLE `aa`.`point_bill` (
  `id`            INT(32)     NOT NULL AUTO_INCREMENT,
  `login_name`    VARCHAR(25) NOT NULL,
  `point`         BIGINT      NOT NULL,
  `business_type` VARCHAR(32) NOT NULL,
  `note`          TEXT,
  `created_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_POINT_BILL_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`point_task` (
  `id`           INT(32)         NOT NULL AUTO_INCREMENT,
  `name`         VARCHAR(100)    NOT NULL,
  `point`        BIGINT UNSIGNED NOT NULL,
  `created_time` DATETIME        NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

INSERT INTO `aa`.point_task (name, point, created_time) VALUES ('REGISTER', 50, now());
INSERT INTO `aa`.point_task (name, point, created_time) VALUES ('BIND_EMAIL', 50, now());
INSERT INTO `aa`.point_task (name, point, created_time) VALUES ('BIND_BANK_CARD', 50, now());
INSERT INTO `aa`.point_task (name, point, created_time) VALUES ('FIRST_RECHARGE', 100, now());
INSERT INTO `aa`.point_task (name, point, created_time) VALUES ('FIRST_INVEST', 200, now());
INSERT INTO `aa`.point_task (name, point, created_time) VALUES ('SUM_INVEST_10000', 500, now());

CREATE TABLE `aa`.`user_point_task` (
  `login_name`    VARCHAR(25) NOT NULL,
  `point_task_id` INT(32)     NOT NULL,
  `created_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`login_name`, `point_task_id`),
  CONSTRAINT FK_USER_POINT_TASK_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_USER_POINT_TASK_REF_POINT_TASK_ID FOREIGN KEY (`point_task_id`) REFERENCES `aa`.`point_task` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;