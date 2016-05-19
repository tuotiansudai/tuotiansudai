CREATE TABLE `aa`.`user_op_log` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`   VARCHAR(25)     NOT NULL,
  `op_type`      VARCHAR(50)     NOT NULL,
  `ip`           VARCHAR(25)     NOT NULL,
  `device_id`    VARCHAR(25)     NOT NULL,
  `source`       VARCHAR(25)     NOT NULL,
  `created_time` DATETIME        NOT NULL,
  `description`  TEXT,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_NAME_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/**
Can not run under Jenkins, need to be manually run under aa schema

BEGIN;

insert into `aa`.`user_op_log` (login_name, op_type, ip, device_id, source, description, created_time)
  SELECT a.login_name, 'BIND_CARD', '', '', '',
    case when LOCATE('ret_code=0000', m.response_data) > 0 then 'Success' else 'Fail' end,
    m.request_time
  from `ump_operations`.`mer_bind_card_notify_request` m
    join `aa`.`account` a on m.user_id = a.pay_user_id;

insert into `aa`.`user_op_log` (login_name, op_type, ip, device_id, source, description, created_time)
  SELECT a.login_name, 'NO_PASSWORD_AGREEMENT', '', '', '',
    case when LOCATE('ret_code=0000', m.response_data) > 0 then 'Success' else 'Fail' end,
    m.request_time
  from `ump_operations`.`mer_bind_agreement_notify_request` m
    join `aa`.`account` a on m.user_id = a.pay_user_id
  where m.user_bind_agreement_list = 'ZTBB0G00,0000,签约成功|';

insert into `aa`.`user_op_log` (login_name, op_type, ip, device_id, source, description, created_time)
  SELECT a.login_name, 'FAST_PAY_AGREEMENT', '', '', '',
    case when LOCATE('ret_code=0000', m.response_data) > 0 then 'Success' else 'Fail' end,
    m.request_time
  from `ump_operations`.`mer_bind_agreement_notify_request` m
    join `aa`.`account` a on m.user_id = a.pay_user_id
  where m.user_bind_agreement_list = 'ZKJP0700,0000,签约成功|';

COMMIT;

**/