BEGIN;
CREATE TABLE `loan_application_materials` (
  `id`                    BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
  `loan_application_id`   BIGINT(20) UNSIGNED NOT NULL,
  `identity_prove_urls`   TEXT                NOT NULL,
  `income_prove_urls`     TEXT                NOT NULL,
  `credit_prove_urls`    TEXT                NOT NULL,
  `marriage_prove_urls`   TEXT,
  `property_prove_urls`   TEXT                NOT NULL,
  `together_prove_urls`   TEXT,
  `drivers_license`       TEXT,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_MATERIALS_LOAN_APPLICATION_ID_REF_LOAN_APPLICATION_ID` FOREIGN KEY (`loan_application_id`) REFERENCES `aa`.`loan_application` (`id`)

)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '借款申请材料记录表';


ALTER TABLE `loan_application`
  ADD COLUMN (
  together_loaner             VARCHAR(25),
  together_loaner_identity    VARCHAR(25),
  loan_id                     BIGINT UNSIGNED,
  status                      VARCHAR(10) DEFAULT 'WAITING' NOT NULL
  );

ALTER TABLE loan_application
  CHANGE is_married marriage VARCHAR(20);
UPDATE loan_application
SET marriage = 'UNMARRIED'
WHERE marriage = '0';
UPDATE loan_application
SET marriage = 'MARRIED'
WHERE marriage = '1';

ALTER TABLE loan_application
  ADD CONSTRAINT FK_LOAN_ID_REF_LOAN_ID FOREIGN KEY (loan_id) REFERENCES loan (id);

COMMIT;