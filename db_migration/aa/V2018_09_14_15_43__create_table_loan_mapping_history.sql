CREATE TABLE `loan_mapping_history` (
  `id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `identity_number`    VARCHAR(18)        NOT NULL
  COMMENT '身份证号',
  `loan_id`    BIGINT(20) UNSIGNED NOT NULL
  COMMENT '标的id',
  PRIMARY KEY (`id`),
  INDEX INDEX_LOAN_MAPPING_HISTORY_IDENTITY_NUMBER(`identity_number`),
  INDEX INDEX_LOAN_MAPPING_HISTORY_LOAN_ID(`loan_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '标的身份证号对应表';