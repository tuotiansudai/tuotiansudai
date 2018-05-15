BEGIN ;

CREATE TABLE `super_scholar_reward` (
  `id`                BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT,
  `login_name`        VARCHAR(25) NOT NULL,
  `question_index`    VARCHAR(20) NOT NULL,
  `question_answer`   VARCHAR(20) NOT NULL,
  `user_answer`       VARCHAR(20) ,
  `user_right`        int(1)      NOT NULL DEFAULT 0,
  `is_share_home`     TINYINT(1)  NOT NULL DEFAULT 0,
  `is_share_account`  TINYINT(1)  NOT NULL DEFAULT 0,
  `is_share_invest`   TINYINT(1)  NOT NULL DEFAULT 0,
  `is_cashback`       TINYINT(1)  NOT NULL DEFAULT 0,
  `created_time`      datetime    NOT NULL,
  `answer_time`       datetime    ,
  `updated_time`      datetime    NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `edxactivity`.`activity_invest` ADD COLUMN `loan_id` VARCHAR(20) AFTER `id`;
ALTER TABLE `edxactivity`.`activity_invest` ADD COLUMN `annualized_amount` BIGINT(20) DEFAULT 0 AFTER `invest_amount`;

COMMIT ;