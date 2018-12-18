BEGIN;
CREATE TABLE `loan_application_materials` (
  `id`                    BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
  `loan_application_id`   BIGINT(20) UNSIGNED NOT NULL,
  `identity_prove_urls`   TEXT                NOT NULL,
  `income_prove_urls`     TEXT                NOT NULL,
  `credit_report_urls`    TEXT                NOT NULL,
  `marriage_prove_urls`   TEXT,
  `property_prove_urls`   TEXT                NOT NULL,
  `together_loaner`       VARCHAR(25),
  `together_prove_urls`   TEXT,
  `tdrivers_license_urls` TEXT,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_MATERIALS_LOAN_APPLICATION_ID_REF_LOAN_APPLICATION_ID` FOREIGN KEY (`loan_application_id`) REFERENCES `aa`.`loan_application` (`id`)

)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '借款申请材料记录表';


ALTER TABLE `loan_application`
  ADD COLUMN (
  loan_id     BIGINT UNSIGNED   COMMENT '生成的标的ID',
  is_divorced TINYINT(1)        NOT NULL COMMENT '是否离异',
  status      VARCHAR(10)       NOT NULL DEFAULT 'WAITING' COMMENT '审核状态'
  );

ALTER TABLE loan_application ADD CONSTRAINT FK_LOAN_ID_REF_LOAN_ID FOREIGN KEY(loan_id) REFERENCES loan(id);

COMMIT;