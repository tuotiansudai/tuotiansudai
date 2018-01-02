CREATE TABLE `login_log_201701` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201701_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8;

CREATE TABLE `login_log_201702` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201702_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `login_log_201703` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201703_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `login_log_201704` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201704_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `login_log_201705` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201705_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `login_log_201706` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201706_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `login_log_201707` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201707_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `login_log_201708` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201708_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `login_log_201709` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201709_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `login_log_201710` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201710_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `login_log_201711` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201711_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `login_log_201712` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201712_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;




CREATE TABLE IF NOT EXISTS `login_log_201801` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201801_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8;

 CREATE TABLE IF NOT EXISTS `login_log_201802` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201802_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

 CREATE TABLE IF NOT EXISTS `login_log_201803` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201803_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

 CREATE TABLE IF NOT EXISTS `login_log_201804` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201804_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

 CREATE TABLE IF NOT EXISTS `login_log_201805` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201705_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

 CREATE TABLE IF NOT EXISTS `login_log_201806` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201806_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

 CREATE TABLE IF NOT EXISTS `login_log_201807` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201807_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

 CREATE TABLE IF NOT EXISTS `login_log_201808` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201808_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

 CREATE TABLE IF NOT EXISTS `login_log_201809` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201809_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

 CREATE TABLE IF NOT EXISTS `login_log_201810` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201810_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


 CREATE TABLE IF NOT EXISTS `login_log_201811` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201811_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


 CREATE TABLE IF NOT EXISTS `login_log_201812` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOGIN_LOG_201812_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

