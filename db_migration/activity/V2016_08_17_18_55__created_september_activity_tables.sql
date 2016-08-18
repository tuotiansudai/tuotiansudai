CREATE TABLE `activity`.`luxury_prize` (
  `id`                               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `brand`                            VARCHAR(100)    NOT NULL,
  `name`                             VARCHAR(100)    NOT NULL,
  `price`                            BIGINT UNSIGNED NOT NULL,
  `image`                            VARCHAR(255)    NOT NULL,
  `invest_amount`                    BIGINT UNSIGNED NOT NULL,
  `ten_percent_off_invest_amount`    BIGINT UNSIGNED NOT NULL,
  `twenty_percent_off_invest_amount` BIGINT UNSIGNED NOT NULL,
  `thirty_percent_off_invest_amount` BIGINT UNSIGNED NOT NULL,
  `introduce`                        TEXT            NOT NULL,
  `created_by`                       VARCHAR(25)     NOT NULL,
  `created_time`                     DATETIME        NOT NULL,
  `updated_by`                       VARCHAR(25),
  `updated_time`                     DATETIME,
  PRIMARY KEY (`id`),
  INDEX INDEX_LUXURY_PRIZE_CREATED_BY (`created_by`),
  INDEX INDEX_LUXURY_PRIZE_UPDATED_BY (`updated_by`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

CREATE TABLE `activity`.`user_luxury_prize` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `prize_id`      BIGINT UNSIGNED NOT NULL,
  `prize`         VARCHAR(100)    NOT NULL,
  `login_name`    VARCHAR(25)     NOT NULL,
  `mobile`        VARCHAR(11)     NOT NULL,
  `invest_amount` BIGINT UNSIGNED NOT NULL,
  `created_time`  DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  INDEX INDEX_USER_LUXURY_PRIZE_LOGIN_NAME (`login_name`),
  INDEX INDEX_USER_LUXURY_PRIZE_MOBILE (`mobile`),
  CONSTRAINT FK_USER_LUXURY_PRIZE_PRIZE_ID_REF_LUXURY_PRIZE_ID FOREIGN KEY (`prize_id`) REFERENCES `activity`.`luxury_prize` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

CREATE TABLE `activity`.`travel_prize` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`          VARCHAR(100)    NOT NULL,
  `description`   VARCHAR(100),
  `price`         BIGINT UNSIGNED NOT NULL,
  `image`         VARCHAR(255)    NOT NULL,
  `invest_amount` BIGINT UNSIGNED NOT NULL,
  `introduce`     TEXT            NOT NULL,
  `created_by`    VARCHAR(25)     NOT NULL,
  `created_time`  DATETIME        NOT NULL,
  `updated_by`    VARCHAR(100),
  `updated_time`  DATETIME,
  PRIMARY KEY (`id`),
  INDEX INDEX_TRAVEL_PRIZE_CREATED_BY (`created_by`),
  INDEX INDEX_TRAVEL_PRIZE_UPDATED_BY (`updated_by`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

CREATE TABLE `activity`.`user_travel_prize` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `prize_id`      BIGINT UNSIGNED NOT NULL,
  `prize`         VARCHAR(100)    NOT NULL,
  `login_name`    VARCHAR(25)     NOT NULL,
  `mobile`        VARCHAR(11)     NOT NULL,
  `invest_amount` BIGINT UNSIGNED NOT NULL,
  `created_time`  DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  INDEX INDEX_USER_TRAVEL_PRIZE_LOGIN_NAME (`login_name`),
  INDEX INDEX_USER_TRAVEL_PRIZE_MOBILE (`mobile`),
  CONSTRAINT FK_USER_TRAVEL_PRIZE_PRIZE_ID_REF_TRAVEL_PRIZE_ID FOREIGN KEY (`prize_id`) REFERENCES `activity`.`travel_prize` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;