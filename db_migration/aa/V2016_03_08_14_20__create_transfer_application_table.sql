CREATE TABLE `aa`.`transfer_application` (
  `id`                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`               VARCHAR(100)    NOT NULL,
  `loan_id`            BIGINT UNSIGNED NOT NULL,
  `transfer_invest_id` BIGINT UNSIGNED NOT NULL,
  `invest_id`          BIGINT UNSIGNED,
  `period`             INT             NOT NULL,
  `login_name`         VARCHAR(25)     NOT NULL,
  `invest_amount`      BIGINT UNSIGNED NOT NULL,
  `transfer_amount`    BIGINT UNSIGNED NOT NULL,
  `transfer_fee`       BIGINT UNSIGNED NOT NULL,
  `status`             VARCHAR(100)    NOT NULL,
  `deadline`           DATETIME        NOT NULL,
  `transfer_time`      DATETIME,
  `application_time`   DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_TRANSFER_APPLICATION_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES `loan` (`id`),
  CONSTRAINT `FK_TRANSFER_APPLICATION_TRANSFER_INVEST_ID_REF_INVEST_ID` FOREIGN KEY (`transfer_invest_id`) REFERENCES `invest` (`id`),
  CONSTRAINT `FK_TRANSFER_APPLICATION_INVEST_ID_REF_INVEST_ID` FOREIGN KEY (`invest_id`) REFERENCES `invest` (`id`),
  CONSTRAINT `FK_TRANSFER_APPLICATION_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

ALTER TABLE `invest` ADD COLUMN `transfer_invest_id` BIGINT UNSIGNED
AFTER `id`;

ALTER TABLE `invest` ADD CONSTRAINT `FK_INVEST_TRANSFER_INVEST_ID_REF_INVEST_ID` FOREIGN KEY (`transfer_invest_id`) REFERENCES `invest` (`id`);

ALTER TABLE `invest` ADD COLUMN `transfer_status` VARCHAR(100) NOT NULL DEFAULT 'TRANSFERABLE'
AFTER `status`;

ALTER TABLE `invest_repay` ADD COLUMN `is_transferred` BOOLEAN NOT NULL DEFAULT FALSE
AFTER `status`;

