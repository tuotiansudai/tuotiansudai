CREATE TABLE `aa`.`login_log_201511` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201511_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `aa`.`login_log_201512` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201512_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`login_log_201601` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201601_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`login_log_201602` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201602_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`login_log_201603` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201603_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`login_log_201604` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201604_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`login_log_201605` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201605_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`login_log_201606` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201607_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`login_log_201608` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201608_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`login_log_201609` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201609_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`login_log_201610` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201610_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`login_log_201611` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201611_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`login_log_201612` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201612_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
