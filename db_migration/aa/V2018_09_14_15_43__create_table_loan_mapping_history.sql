CREATE TABLE `loan_mapping_history` (
  `id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `login_name`    VARCHAR(25)        NOT NULL
  COMMENT '身份证号',
  `loan_id`    BIGINT(20) UNSIGNED NOT NULL
  COMMENT '标的id',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_LOAN_MAPPING_HISTORY_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `user` (`login_name`),
  CONSTRAINT `FK_LOAN_MAPPING_HISTORY_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES `loan` (`id`),
  INDEX INDEX_LOAN_MAPPING_HISTORY_LOGIN_NAME(`login_name`),
  INDEX INDEX_LOAN_MAPPING_HISTORY_LOAN_ID(`loan_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '标的loginName对应表';