CREATE TABLE ${ump_operations}.`mer_register_request` (
  `id`            INT(32)      NOT NULL AUTO_INCREMENT,
  `mer_cust_id`   VARCHAR(32)  NOT NULL,
  `mer_cust_name` VARCHAR(256) NOT NULL,
  `identity_type` VARCHAR(256) NOT NULL,
  `identity_code` VARCHAR(256) NOT NULL,
  `mobile_id`     VARCHAR(11)  NOT NULL,
  `email`         VARCHAR(64),
  `request_time`  DATETIME     NOT NULL,
  `request_url`   TEXT,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `${ump_operations}`.`mer_register_response` (
  `id`            INT(32)    NOT NULL AUTO_INCREMENT,
  `reg_date`      DATETIME,
  `user_id`       VARCHAR(32),
  `account_id`    VARCHAR(15),
  `ret_code`      VARCHAR(8) NOT NULL,
  `ret_msg`       VARCHAR(128),
  `response_time` DATETIME   NOT NULL,
  `request_id`    INT(32)    NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_REGISTER_RES_FOR_REQ FOREIGN KEY (`request_id`) REFERENCES ${ump_operations}.`mer_register_request` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;