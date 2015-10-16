CREATE TABLE `aa`.`loan_title_relation` (
  `id`                 BIGINT UNSIGNED NOT NULL,
  `loan_id`            BIGINT UNSIGNED NOT NULL, /***借款标的***/
  `title_id`           BIGINT UNSIGNED NOT NULL, /***申请材料标题***/
  `apply_metarial_url` TEXT            NOT NULL, /***申请材料存放路径***/
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_LOAN_TITLE_RELATION_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES `aa`.`loan` (`id`),
  CONSTRAINT `FK_LOAN_TITLE_RELATION_TITLE_ID_REF_LOAN_ID` FOREIGN KEY (`title_id`) REFERENCES `aa`.`loan_title` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;