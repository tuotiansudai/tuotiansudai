CREATE TABLE `aa`.`anxin_sign_property` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(50) NOT NULL,
  `anxin_user_id` varchar(32) COMMENT '安心签用户ID',
  `skip_auth` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开通免验',
  `project_code` varchar(32) COMMENT '上次授权时使用的projectCode',
  `auth_ip` VARCHAR (15) COMMENT '授权时用户的IP',
  `auth_time` datetime COMMENT '上一次授权的时间',
  `created_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_CFCA_PROPERTY_LOGIN_NAME` (`login_name`),
  CONSTRAINT `FK_CFCA_PROPERTY_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `user` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


OR:

alter TABLE account add COLUMN anxin_user_id varchar(32) COMMENT '安心签用户ID';
alter TABLE account add COLUMN skip_auth tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开通免验';
alter TABLE account add COLUMN project_code varchar(32) COMMENT '上次授权时使用的projectCode';

