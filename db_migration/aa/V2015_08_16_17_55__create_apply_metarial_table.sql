CREATE TABLE ${aa}.`loan_title` (
  `loan_id` varchar(32) NOT NULL,/***借款标的***/
  `title_id` varchar(32) NOT NULL,/***申请材料标题***/
  `apply_metarial_url` varchar(32) NOT NULL,/***申请材料存放路径***/
  PRIMARY KEY (`loan_id`,`title_id`),
  CONSTRAINT `FK_LOAN_TITLE_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES ${aa}.`loan` (`id`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;