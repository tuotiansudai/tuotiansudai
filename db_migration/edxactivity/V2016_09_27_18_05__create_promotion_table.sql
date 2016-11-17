CREATE TABLE `edxactivity`.`promotion` (
  `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`                VARCHAR(100) NOT NULL,
  `image_url`     			VARCHAR(200) NOT NULL,
  `link_url`            VARCHAR(100) NOT NULL,
  `start_time`          DATETIME,
  `end_time`            DATETIME,
  `seq`           			BIGINT UNSIGNED NOT NULL,
  `status`              VARCHAR(20) NOT NULL,
  `created_by`        	VARCHAR(25) NOT NULL,
  `created_time`      	DATETIME NOT NULL,
  `updated_by`      		VARCHAR(25),
  `updated_time`    		DATETIME,
  `deactivated_by`    	VARCHAR(25),
  `deactivated_time`  	DATETIME,
  `deleted`         		TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8;