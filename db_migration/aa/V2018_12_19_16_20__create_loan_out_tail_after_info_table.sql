BEGIN;
CREATE TABLE `loan_out_tail_after` (
  `id`                       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `loan_id`                  BIGINT UNSIGNED,
  `finance_state`            VARCHAR(50)     NOT NULL,
  `repay_power`              VARCHAR(50)     NOT NULL,
  `is_overdue`               TINYINT(1)      NOT NULL,
  `is_administrative_penalty` TINYINT(1)     NOT NULL,
  `amount_usage`             VARCHAR(50)     NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_TAIL_AFTER_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES `aa`.`loan` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '放款后跟踪信息';

COMMIT;