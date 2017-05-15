CREATE table `dragon_boat_festival` (
  `id`                      INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`              VARCHAR (25) NOT NULL,
  `user_name`               VARCHAR (25),
  `mobile`                  VARCHAR (11),
  `invest_amount`           INTEGER (10),
  `experience_amount`       INT(20) UNSIGNED NOT NULL DEFAULT 0,
  `invite_new_user_count`   INT(8) UNSIGNED NOT NULL DEFAULT 0,
  `invite_old_user_count`   INT(8) UNSIGNED NOT NULL DEFAULT 0,
  `created_time`            DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`login_name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

select user_name, mobile, invest_amount, experience_amount, invite_old_user_count, invite_new_user_count from dragon_boat_activity order by created_time asc;

