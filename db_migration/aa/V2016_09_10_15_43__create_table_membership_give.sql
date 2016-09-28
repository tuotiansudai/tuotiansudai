CREATE TABLE `membership_give` (
  `id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `membership_id` BIGINT(20) UNSIGNED NOT NULL
  COMMENT '对应membership表中的ID',
  `deadline`      INT(10) UNSIGNED    NOT NULL
  COMMENT '有效天数',
  `start_time`    DATETIME                     DEFAULT NULL
  COMMENT '领取活动开始时间',
  `end_time`      DATETIME                     DEFAULT NULL
  COMMENT '领取活动结束时间',
  `user_group`    VARCHAR(200)        NOT NULL
  COMMENT '发放用户组',
  `sms_notify`    TINYINT(1)          NOT NULL
  COMMENT '是否短信通知, 0-不通知， 1-通知',
  `active`        TINYINT(1)          NOT NULL
  COMMENT '是否生效，0-不生效，1-生效',
  `active_by`     VARCHAR(25)                  DEFAULT NULL
  COMMENT '生效批准人',
  `created_time`  DATETIME                     DEFAULT NULL
  COMMENT '创建人',
  `created_by`    VARCHAR(25)                  DEFAULT NULL
  COMMENT '创建时间',
  `updated_time`  DATETIME                     DEFAULT NULL
  COMMENT '更新人',
  `updated_by`    VARCHAR(25)                  DEFAULT NULL
  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_MEMB_GIVE_MEMB_ID_REF_MEMB_ID` FOREIGN KEY (`membership_id`) REFERENCES `membership` (`id`),
  CONSTRAINT `FK_MEMB_GIVE_CREATED_BY_REF_USER_LOGIN_NAME` FOREIGN KEY (`created_by`) REFERENCES `user` (`login_name`),
  CONSTRAINT `FK_MEMB_GIVE_UPDATED_BY_REF_USER_LOGIN_NAME` FOREIGN KEY (`updated_by`) REFERENCES `user` (`login_name`),
  CONSTRAINT `FK_MEMB_GIVE_ACTIVE_BY_REF_USER_LOGIN_NAME` FOREIGN KEY (`active_by`) REFERENCES `user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '会员赠送记录表';