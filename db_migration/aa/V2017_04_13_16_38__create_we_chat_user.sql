CREATE TABLE `we_chat_user` (
  `id`                BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
  `login_name`        VARCHAR(25)           NOT NULL,
  `openid`            VARCHAR(100)          NOT NULL,
  `bound`             BOOLEAN DEFAULT FALSE NOT NULL,
  `latest_login_time` DATETIME              NOT NULL,
  `created_time`      DATETIME              NOT NULL,
  PRIMARY KEY (`id`),
  INDEX INDEX_WE_CHAT_USER_LOGIN_NAME(`login_name`),
  UNIQUE KEY UNIQUE_WE_CHAT_USER_OPENID(`openid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;