BEGIN;
CREATE TABLE `activity` (
  `id`               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `title`            VARCHAR(30)     NOT NULL
  COMMENT '活动名称',
  `web_activity_url` VARCHAR(150)    NOT NULL
  COMMENT '活动目标地址',
  `app_activity_url` VARCHAR(150)    NOT NULL
  COMMENT 'APP活动备用地址',
  `description`      VARCHAR(15)     NOT NULL
  COMMENT '活动介绍',
  `web_picture_url`  VARCHAR(150)    NOT NULL
  COMMENT 'PC端活动图',
  `app_picture_url`  VARCHAR(150)    NOT NULL
  COMMENT '移动端活动图',
  `activated_time`   DATETIME
  COMMENT '活动发布时间',
  `expired_time`     DATETIME        NOT NULL
  COMMENT '活动结束时间',
  `source`           VARCHAR(10)     NOT NULL
  COMMENT '活动来源渠道',
  `status`           VARCHAR(20)     NOT NULL
  COMMENT '活动状态',
  `created_by`       VARCHAR(25)     NOT NULL
  COMMENT '活动创建人',
  `created_time`     DATETIME        NOT NULL
  COMMENT '活动创建时间',
  `updated_by`       VARCHAR(25)     NOT NULL
  COMMENT '活动更新人',
  `updated_time`     DATETIME        NOT NULL
  COMMENT '活动更新时间',
  `activated_by`     VARCHAR(25)
  COMMENT '活动审核人',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_ACTIVITY_ACTIVATED_BY_REF_USER_LOGIN_NAME` FOREIGN KEY (`activated_by`) REFERENCES `user` (`login_name`),
  CONSTRAINT `FK_ACTIVITY_CREATED_BY_REF_USER_LOGIN_NAME` FOREIGN KEY (`created_by`) REFERENCES `user` (`login_name`),
  CONSTRAINT `FK_ACTIVITY_UPDATED_BY_REF_USER_LOGIN_NAME` FOREIGN KEY (`updated_by`) REFERENCES `user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '活动中心活动';
COMMIT;