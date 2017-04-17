CREATE TABLE `htracking_user` (
    `mobile` VARCHAR(18) NOT NULL,
    `device_id` DATETIME NOT NULL,
    `status` VARCHAR(18) NOT NULL,
    `created_time` DATETIME,
    PRIMARY KEY (`mobile`,`device_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;