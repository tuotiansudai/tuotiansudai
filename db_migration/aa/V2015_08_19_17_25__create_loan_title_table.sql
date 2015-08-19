CREATE TABLE `${aa}`.`loan_title` (
  `id`                    INT(32)               NOT NULL AUTO_INCREMENT,
  `loan_id`               BIGINT            NOT NULL,/***借款标的***/
  `title_id`              INT               NOT NULL,/***申请材料标题***/
  `apply_metarial_url`    TEXT              NOT NULL,/***申请材料存放路径***/
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_LOAN_TITLE_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES `${aa}`.`loan` (`id`),
  CONSTRAINT `FK_LOAN_TITLE_TITLE_ID_REF_LOAN_ID` FOREIGN KEY (`title_id`) REFERENCES `${aa}`.`title` (`id`)
)
ENGINE=InnoDB
AUTO_INCREMENT = 100001
DEFAULT CHARSET=utf8;