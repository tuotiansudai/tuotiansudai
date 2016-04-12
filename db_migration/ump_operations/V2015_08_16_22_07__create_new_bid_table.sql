CREATE TABLE ump_operations.`mer_bind_project_request` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT, /******/
  `service`        VARCHAR(32)     NOT NULL, /***接口名称***/
  `sign_type`      VARCHAR(8)      NOT NULL, /***签名方式***/
  `sign`           VARCHAR(256)    NOT NULL, /***签名***/
  `charset`        VARCHAR(16)     NOT NULL, /***参数字符编码集***/
  `mer_id`         VARCHAR(8)      NOT NULL, /***商户编号***/
  `version`        VARCHAR(3)      NOT NULL, /***版本号***/
  `project_id`     VARCHAR(32)     NOT NULL, /***标的号***/
  `project_name`   VARCHAR(32)     NOT NULL, /***标的名称***/
  `project_amount` VARCHAR(13)     NOT NULL, /***标的金额***/
  `loan_user_id`   VARCHAR(32)     NOT NULL, /***标的融资人资金账户托管平台用户号（个人）***/
  `request_url`    VARCHAR(2048)   NOT NULL, /***请求地址***/
  `request_data`   TEXT            NOT NULL, /***请求数据***/
  `request_time`   DATETIME        NOT NULL, /***请求时间***/
  `status`         VARCHAR(10)     NOT NULL, /***请求状态***/
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE ump_operations.`mer_bind_project_response` (
  `id`                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT, /******/
  `request_id`         BIGINT UNSIGNED NOT NULL, /***请求ID***/
  `sign_type`          VARCHAR(16), /***签名方式***/
  `sign`               VARCHAR(512)    NOT NULL, /***签名***/
  `mer_id`             VARCHAR(16), /***商户编号***/
  `version`            VARCHAR(6), /***版本号***/
  `project_account_id` VARCHAR(30), /***标的账户号***/
  `project_state`      VARCHAR(4), /***标的账户状态***/
  `mer_check_date`     VARCHAR(16), /***商户对账日期***/
  `ret_code`           VARCHAR(16)     NOT NULL, /***返回码***/
  `ret_msg`            VARCHAR(256), /***返回信息***/
  `response_data`      TEXT            NOT NULL, /***请求数据***/
  `response_time`      DATETIME        NOT NULL, /***请求时间***/
  PRIMARY KEY (`id`),
  CONSTRAINT FK_MER_BIND_PROJECT_RESPONSE_REQUEST_ID_REF_REQUEST_ID FOREIGN KEY (`request_id`) REFERENCES ump_operations.`mer_bind_project_request` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;