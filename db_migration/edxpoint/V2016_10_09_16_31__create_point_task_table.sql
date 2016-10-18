CREATE TABLE `point_task` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `point` bigint(20) unsigned NOT NULL,
  `multiple` tinyint(1) DEFAULT '0',
  `active` tinyint(1) DEFAULT '1',
  `max_level` bigint(20) unsigned NOT NULL DEFAULT '1',
  `created_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;