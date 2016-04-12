CREATE TABLE `referrer_relation` (
  `referrer_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `level` INT NOT NULL,
  PRIMARY KEY (`referrer_id`, `user_id`),
  CONSTRAINT `referrer_relation_fk1` FOREIGN KEY (`referrer_id`) REFERENCES `user` (`id`),
  CONSTRAINT `referrer_relation_fk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;