BEGIN;
CREATE TABLE `loan_risk_management_title` (
  `id`    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255)    NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '风控信息title';

INSERT INTO `aa`.`loan_risk_management_title` VALUES (1, '手机认证');
INSERT INTO `aa`.`loan_risk_management_title` VALUES (2, '收入认证');
INSERT INTO `aa`.`loan_risk_management_title` VALUES (3, '信用报告');
INSERT INTO `aa`.`loan_risk_management_title` VALUES (4, '资产认证');
INSERT INTO `aa`.`loan_risk_management_title` VALUES (5, '身份信息认证');
INSERT INTO `aa`.`loan_risk_management_title` VALUES (6, '婚姻状况认证');
INSERT INTO `aa`.`loan_risk_management_title` VALUES (7, '住址信息认证');
INSERT INTO `aa`.`loan_risk_management_title` VALUES (8, '工作信息认证');
INSERT INTO `aa`.`loan_risk_management_title` VALUES (9, '共同借款人');

CREATE TABLE `aa`.`loan_risk_management_title_relation` (
  `id`                  BIGINT UNSIGNED NOT NULL,
  `loan_id`             BIGINT UNSIGNED,
  `loan_application_id` BIGINT UNSIGNED NOT NULL,
  `title_id`            BIGINT UNSIGNED NOT NULL,
  `detail`              TEXT            NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_LOAN_RISK_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES `aa`.`loan` (`id`),
  CONSTRAINT `FK_LOAN_RISK_LOAN_APPLICATION_ID_REF_LOAN_ID` FOREIGN KEY (`title_id`) REFERENCES `aa`.`loan_application` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '风控信息详情';

COMMIT;