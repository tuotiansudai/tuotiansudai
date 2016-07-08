BEGIN;
CREATE TABLE `loan_details` (
  `id`          BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `loan_id`     BIGINT(20) UNSIGNED NOT NULL
  COMMENT '标的ID',
  `declaration` VARCHAR(100)        NOT NULL
  COMMENT '标的声明',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_LOAN_DETAILS_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES `loan` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT '标的详情';

CREATE TABLE `loaner_details` (
  `id`                BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `loan_id`           BIGINT(20) UNSIGNED NOT NULL
  COMMENT '标的ID',
  `login_name`        VARCHAR(20)         NOT NULL
  COMMENT '借款人登录名',
  `user_name`         VARCHAR(40)                  DEFAULT NULL
  COMMENT '借款人真实姓名',
  `gender`            VARCHAR(10)                  DEFAULT NULL
  COMMENT '借款人性别',
  `age`               INT(11)                      DEFAULT NULL
  COMMENT '借款人年龄',
  `identify_number`   VARCHAR(18)                  DEFAULT NULL
  COMMENT '借款人身份证号',
  `marriage`          VARCHAR(30)                  DEFAULT NULL
  COMMENT '借款人婚姻情况',
  `region`            VARCHAR(40)                  DEFAULT NULL
  COMMENT '借款人所在地区',
  `income`            VARCHAR(50)                  DEFAULT NULL
  COMMENT '借款人收入情况',
  `employment_status` VARCHAR(20)                  DEFAULT NULL
  COMMENT '借款人就业情况',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_LOANER_DETAILS_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES `loan` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT '标的借款人详情';

CREATE TABLE `pledge_house` (
  `id`                 BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `loan_id`            BIGINT(20) UNSIGNED NOT NULL
  COMMENT '标的ID',
  `pledge_location`    VARCHAR(30)                  DEFAULT NULL
  COMMENT '抵押物所在地',
  `estimate_amount`    VARCHAR(30)                  DEFAULT NULL
  COMMENT '抵押物估值',
  `square`             VARCHAR(30)                  DEFAULT NULL
  COMMENT '房屋面积',
  `property_card_id`   VARCHAR(20)                  DEFAULT NULL
  COMMENT '房产证编号',
  `estate_register_id` VARCHAR(30)                  DEFAULT NULL
  COMMENT '不动产登记z证明',
  `authentic_act`      VARCHAR(50)                  DEFAULT NULL
  COMMENT '公证书编号',
  `loan_amount`        VARCHAR(30)                  DEFAULT NULL
  COMMENT '抵押物借款金额',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_PLEDGE_HOUSE_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES `loan` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT '房屋抵押物详情';

CREATE TABLE `pledge_vehicle` (
  `id`              BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `loan_id`         BIGINT(20) UNSIGNED NOT NULL
  COMMENT '标的ID',
  `pledge_location` VARCHAR(40)                  DEFAULT NULL
  COMMENT '抵押物所在地',
  `brand`           VARCHAR(30)                  DEFAULT NULL
  COMMENT '车辆品牌',
  `model`           VARCHAR(30)                  DEFAULT NULL
  COMMENT '车辆型号',
  `estimate_amount` VARCHAR(30)                  DEFAULT NULL
  COMMENT '抵押物估值',
  `loan_amount`     VARCHAR(30)                  DEFAULT NULL
  COMMENT '抵押物借款金额',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_PLEDGE_VEHICLE_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES `loan` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT '车辆抵押物详情';
COMMIT;