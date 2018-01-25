
CREATE TABLE `activity_invest_annualized` (
  `id`                          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`                  VARCHAR(25) NOT NULL,
  `user_name`                   VARCHAR(50) NOT NULL,
  `mobile`                      VARCHAR(11) NOT NULL,
  `sum_invest_amount`           BIGINT(20) DEFAULT '0',
  `sum_annualized_amount`       BIGINT(20) DEFAULT '0',
  `activity_name`               VARCHAR(100) NOT NULL,
  `activity_loan_desc`          VARCHAR(11) DEFAULT NULL,
  `created_time`                DATETIME DEFAULT NULL,
  `updated_time`                DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX INDEX_NAME_AUUNALIZED_ACTIVITY (`activity_name`),
  INDEX INDEX_MOBILE_AUUNALIZED_ACTIVITY (`mobile`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
