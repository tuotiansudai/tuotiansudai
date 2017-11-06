BEGIN;
INSERT INTO edxmessage.`message` (`title`
  , `template`, `template_txt`
  , `type`, `event_type`, `user_group`, `channels`, `message_category`
  , `web_url`, `app_url`
  , `status`, `read_count`, `push_id`
  , `activated_by`, `activated_time`, `valid_end_time`
  , `created_by`, `created_time`
  , `updated_by`, `updated_time`, `deleted`)
VALUES ('代发工资到账'
  , '{0}已发放到您的账户余额中，请注意查收。', '{0}已发放到您的账户余额中，请注意查收。'
  , 'EVENT', 'PAYROLL_HAS_BEEN_TRANSFERRED', 'ALL_USER', 'WEBSITE,APP_MESSAGE', 'SYSTEM'
  , 'https://tuotiansudai.com/account', 'MY_FUND_AVAILABLE'
  , 'APPROVED', 0, NULL
  , 'sidneygao', '2017-05-11 00:00:00', '9999-12-31 23:59:59'
  , 'sidneygao', '2017-05-11 00:00:00'
  , 'sidneygao', '2017-05-11 00:00:00', 0);
COMMIT;