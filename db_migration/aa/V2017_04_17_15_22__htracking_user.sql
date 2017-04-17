CREATE TABLE `htracking_user` (
    `mobile` VARCHAR(18) NOT NULL,
    `device_id` VARCHAR(50) NOT NULL,
    `created_time` DATETIME,
    PRIMARY KEY (`mobile`,`device_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;