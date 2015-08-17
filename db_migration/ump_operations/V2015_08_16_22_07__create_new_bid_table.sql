CREATE TABLE ${ump_operations}.`mer_bind_project_request` (
  `id`                  INT(32)         NOT NULL AUTO_INCREMENT,/******/
  `service`             VARCHAR(32)     NOT NULL,/***接口名称***/
  `sign_type`           VARCHAR(8)      NOT NULL,/***签名方式***/
  `sign`                VARCHAR(256)    NOT NULL,/***签名***/
  `charset`             VARCHAR(16)     NOT NULL,/***参数字符编码集***/
  `mer_id`              VARCHAR(8)      NOT NULL,/***商户编号***/
  `version`             VARCHAR(3)      NOT NULL,/***版本号***/
  `project_id`          VARCHAR(32)     NOT NULL,/***标的号***/
  `project_name`        VARCHAR(32)     NOT NULL,/***标的名称***/
  `project_amount`      VARCHAR(13)     NOT NULL,/***标的金额***/
  `loan_user_id`        VARCHAR(32)     NOT NULL,/***标的融资人资金账户托管平台用户号（个人）***/
  `request_url`         VARCHAR(2048)   NOT NULL,/***请求地址***/
  `request_data`        TEXT            NOT NULL,/***请求数据***/
  PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;

CREATE TABLE ${ump_operations}.`mer_bind_project_response` (
  `id`                            INT(32)         NOT NULL AUTO_INCREMENT,/******/
  `mer_bind_project_request_id`   INT(32)         NOT NULL AUTO_INCREMENT,/******/
  `sign_type`                     varchar(8)      NOT NULL,/***签名方式***/
  `sign`                          varchar(256)    DEFAULT NULL,/***签名***/
  `mer_id`                        varchar(8)      DEFAULT NULL,/***商户编号***/
  `version`                       varchar(3)      DEFAULT NULL,/***版本号***/
  `project_account_id`            varchar(15)     DEFAULT NULL,/***标的账户号***/
  `project_state`                 varchar(2)      DEFAULT NULL,/***标的账户状态***/
  `mer_check_date`                varchar(8)      DEFAULT NULL,/***商户对账日期***/
  `ret_code`                      varchar(8)      DEFAULT NULL,/***返回码***/
  `ret_msg`                       varchar(128)    DEFAULT NULL,/***返回信息***/
  PRIMARY KEY (`id`),
  CONSTRAINT FK_mer_bind_project_response_id_ref_mer_bind_project_request_id FOREIGN KEY (`mer_bind_project_request_id`) REFERENCES ${ump_operations}.`mer_bind_project_response` (`id`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;