CREATE TABLE `aa`.`jpush_alert` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `push_type` varchar(100) NOT NULL,
  `push_objects` varchar(200) DEFAULT NULL ,
  `push_source` varchar(500) NOT NULL,
  `status` varchar(100) DEFAULT NULL,
  `content` text NOT NULL,
  `jump_to` varchar(30) DEFAULT NULL,
  `jump_to_link` varchar(100) DEFAULT NULL,
   `created_time` datetime NOT NULL,
   `created_by` varchar(25) NOT NULL,
   `updated_time` datetime DEFAULT NULL,
   `updated_by` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_JPUSH_ALERT_CREATED_BY_REF_USER_LOGIN_NAME` FOREIGN KEY (`created_by`) REFERENCES `user` (`login_name`),
  CONSTRAINT `FK_JPUSH_ALERT_UPDATED_BY_REF_USER_LOGIN_NAME` FOREIGN KEY (`updated_by`) REFERENCES `user` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;