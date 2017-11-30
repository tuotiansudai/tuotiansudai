BEGIN;

CREATE TABLE `experience_account` (
  `id`                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`         VARCHAR(50)     NOT NULL,
  `experience_balance` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;


INSERT INTO experience_account (login_name, experience_balance)
  SELECT
    b.login_name,
    u.experience_balance
  FROM (
         SELECT DISTINCT login_name
         FROM experience_bill
       ) b INNER JOIN user u ON b.login_name = u.login_name;


ALTER TABLE user
  DROP COLUMN experience_balance;

COMMIT;
