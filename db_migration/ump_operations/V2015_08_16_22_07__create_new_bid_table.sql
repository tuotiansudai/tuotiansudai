CREATE TABLE `mer_bind_project_request` (
  `id` varchar(32) NOT NULL,
  `mark_id` varchar(50) NOT NULL,
  `operator` varchar(500) DEFAULT NULL,
  `request_data` text,
  `request_time` datetime DEFAULT NULL,
  `response_data` text,
  `response_time` datetime DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `trusteeship` varchar(200) DEFAULT NULL,
  `type` varchar(200) DEFAULT NULL,
  `request_url` varchar(4096) DEFAULT NULL,
  `charset` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;

CREATE TABLE `mer_bind_project_request` (
  `id` varchar(32) NOT NULL,
  `mark_id` varchar(50) NOT NULL,
  `operator` varchar(500) DEFAULT NULL,
  `request_data` text,
  `request_time` datetime DEFAULT NULL,
  `response_data` text,
  `response_time` datetime DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `trusteeship` varchar(200) DEFAULT NULL,
  `type` varchar(200) DEFAULT NULL,
  `request_url` varchar(4096) DEFAULT NULL,
  `charset` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;