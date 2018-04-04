BEGIN ;

CREATE TABLE `we_chat_user_info` (
  `id`                BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT,
  `openId`            VARCHAR(100)     NOT NULL,
  `nick_name`         VARCHAR (100) CHARACTER SET utf8mb4,
  `head_img_url`      varchar(256),
  `created_time`      datetime NOT NULL,
  `updated_time`      datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET utf8 collate utf8_general_ci;

CREATE TABLE `we_chat_help` (
  `id`                BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT,
  `loan_id`           BIGINT UNSIGNED,
  `invest_id`         BIGINT UNSIGNED,
  `invest_amount`     BIGINT(20)  DEFAULT 0,
  `annualized_amount` BIGINT(20)  DEFAULT 0,
  `login_name`        VARCHAR(25),
  `openId`            VARCHAR(100),
  `help_type`         VARCHAR(25) NOT NULL,
  `help_user_count`   int(10)     DEFAULT 0,
  `reward`            BIGINT(20)  DEFAULT 0,
  `start_time`        datetime    NOT NULL,
  `end_time`          datetime    NOT NULL,
  `is_send`           TINYINT(1)  NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `we_chat_help_info` (
  `id`                    BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT,
  `we_chat_user_info_id`  BIGINT UNSIGNED  NOT NULL,
  `we_chat_help_id`       BIGINT UNSIGNED  NOT NULL,
  `created_time`          datetime NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_WE_CHAT_USER_INFO_ID FOREIGN KEY (`we_chat_user_info_id`) REFERENCES `we_chat_user_info` (`id`),
  CONSTRAINT FK_WE_CHAT_HELP_ID FOREIGN KEY (`we_chat_help_id`) REFERENCES `we_chat_help` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


COMMIT ;