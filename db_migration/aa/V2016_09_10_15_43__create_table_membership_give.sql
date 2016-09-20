CREATE TABLE `membership_give` (
  `id`                 BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `membership_id`      BIGINT(20) UNSIGNED NOT NULL
  COMMENT '对应membership表中的ID',
  `valid_period`       INT(10) UNSIGNED    NOT NULL
  COMMENT '有效天数',
  `receive_start_time` DATETIME DEFAULT NULL
  COMMENT '领取活动开始时间',
  `receive_end_time`   DATETIME DEFAULT NULL
  COMMENT '领取活动结束时间',
  `user_group`         VARCHAR(200)        NOT NULL
  COMMENT '发放用户组',
  `sms_notify`         TINYINT(1)          NOT NULL
  COMMENT '是否短信通知, 0-不通知， 1-通知',
  `valid`              TINYINT(1)          NOT NULL
  COMMENT '是否生效，0-不生效，1-生效',
  `valid_login_name`   VARCHAR(25)                  DEFAULT NULL
  COMMENT '生效批准人',
  `created_time`       DATETIME                     DEFAULT NULL
  COMMENT '创建人',
  `created_login_name` VARCHAR(25)                  DEFAULT NULL
  COMMENT '创建时间',
  `updated_time`       DATETIME                     DEFAULT NULL
  COMMENT '更新人',
  `updated_login_name` VARCHAR(25)                  DEFAULT NULL
  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_MEMB_GIVE_MEMB_ID_REF_MEMB_ID` FOREIGN KEY (`membership_id`) REFERENCES `membership` (`id`),
  CONSTRAINT `FK_MEMB_GIVE_CREATED_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`created_login_name`) REFERENCES `user` (`login_name`),
  CONSTRAINT `FK_MEMB_GIVE_UPDATED_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`updated_login_name`) REFERENCES `user` (`login_name`),
  CONSTRAINT `FK_MEMB_GIVE_VAILD_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`valid_login_name`) REFERENCES `user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '会员赠送记录表';