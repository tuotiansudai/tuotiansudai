CREATE TABLE `channel_point` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `serial_no`     VARCHAR(50)     NOT NULL,
  `total_point`   BIGINT UNSIGNED NOT NULL DEFAULT 0,
  `head_count`    BIGINT UNSIGNED NOT NULL DEFAULT 0,
  `created_by`    VARCHAR(25) NOT NULL,
  `created_time`  DATETIME NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;


CREATE TABLE `channel_point_detail` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `channel_point_id`    BIGINT UNSIGNED NOT NULL,
  `login_name`    VARCHAR(50)     ,
  `user_name`     VARCHAR(50)     NOT NULL,
  `mobile`        VARCHAR(11)     NOT NULL,
  `channel`       VARCHAR(50)     NOT NULL,
  `point`         BIGINT UNSIGNED NOT NULL,
  `success`       TINYINT(1)      NOT NULL DEFAULT FALSE ,
  `created_time`  DATETIME        NOT NULL,
  `remark`  VARCHAR(255)        ,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_CHANNEL_POINT_DETAIL_REF_CHANNEL_POINT_ID FOREIGN KEY (`channel_point_id`) REFERENCES `channel_point` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;