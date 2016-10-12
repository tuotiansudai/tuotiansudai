CREATE TABLE `aa`.`loan_application` (
  `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`   VARCHAR(25)         NOT NULL
  COMMENT '借款申请人登录名',
  `mobile`    VARCHAR(11) NOT NULL
  COMMENT '借款申请人电话',
  `user_name` VARCHAR(50) NOT NULL
  COMMENT '借款申请人用户名',
  `region`       VARCHAR(15)         NOT NULL
  COMMENT '借款申请人所在地区',
  `amount`       INT UNSIGNED        NOT NULL
  COMMENT '申请借款数额，单位万',
  `period`       INT UNSIGNED        NOT NULL
  COMMENT '申请借款期限，单位月',
  `pledge_type`  VARCHAR(30)         NOT NULL
  COMMENT '抵押物类型',
  `pledge_info`  VARCHAR(200)        NOT NULL
  COMMENT '抵押物信息',
  `comment`      VARCHAR(200)        NULL
  COMMENT '备注',
  `created_time` DATETIME            NOT NULL,
  `updated_by`   VARCHAR(25)         NOT NULL,
  `updated_time` DATETIME            NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`login_name`)
  REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT `FK_UPDATED_BY_REF_USER_LOGIN_NAME` FOREIGN KEY (`updated_by`)
  REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = INNODB
  DEFAULT CHARACTER SET = UTF8
  COMMENT = '借款申请记录表';