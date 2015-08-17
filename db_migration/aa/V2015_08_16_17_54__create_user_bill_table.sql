CREATE TABLE `user_bill` (
  `id` varchar(32) NOT NULL,
  `detail` varchar(200) DEFAULT NULL,
  `money` double NOT NULL,
  `seq_num` int(11) NOT NULL,
  `time` datetime NOT NULL,
  `type_info` varchar(320) DEFAULT NULL,
  `user_id` varchar(32) NOT NULL,
  `type` varchar(200) DEFAULT NULL,
  `balance` double DEFAULT NULL,
  `frozen_money` double DEFAULT NULL,
  `operator` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `FK143497FB2A334343` (`user_id`) USING BTREE,
  KEY `user_bill_operator` (`operator`) USING BTREE,
  CONSTRAINT `user_bill_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;