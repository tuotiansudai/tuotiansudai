alter table `aa`.`activity` add seq BIGINT NOT NULL;
alter table `aa`.`activity` add long_term tinyint(1) NOT NULL;
alter table `aa`.`activity` MODIFY `expired_time` DATETIME DEFAULT NULL;
