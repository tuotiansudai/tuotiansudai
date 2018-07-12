BEGIN;
INSERT INTO edxmessage.`message` (`title`
  , `template`, `template_txt`
  , `type`, `event_type`, `user_group`, `channels`, `message_category`
  , `web_url`, `app_url`
  , `status`, `read_count`, `push_id`
  , `activated_by`, `activated_time`, `valid_end_time`
  , `created_by`, `created_time`
  , `updated_by`, `updated_time`, `deleted`)
VALUES ('恭喜您认证成功'
  , '尊敬的{0}女士/先生，您申请的出借人身份已经认证成功,马上开启赚钱之旅吧！', '尊敬的{0}女士/先生，您申请的出借人身份已经认证成功,马上开启赚钱之旅吧！'
  , 'EVENT', 'REGISTER_INVESTOR_ACCOUNT_SUCCESS', 'ALL_USER', 'WEBSITE,APP_MESSAGE', 'SYSTEM'
  , 'https://tuotiansudai.com/personal-info', 'MY_ACCOUNT'
  , 'APPROVED', 0, NULL
  , 'sidneygao', '2018-07-05 00:00:00', '9999-12-31 23:59:59'
  , 'sidneygao', '2018-07-05 00:00:00'
  , 'sidneygao', '2018-07-05 00:00:00', 0);

INSERT INTO edxmessage.`message` (`title`
  , `template`, `template_txt`
  , `type`, `event_type`, `user_group`, `channels`, `message_category`
  , `web_url`, `app_url`
  , `status`, `read_count`, `push_id`
  , `activated_by`, `activated_time`, `valid_end_time`
  , `created_by`, `created_time`
  , `updated_by`, `updated_time`, `deleted`)
VALUES ('恭喜您认证成功'
  , '尊敬的{0}女士/先生，您申请的借款人身份已经认证成功,马上去借款吧！', '尊敬的{0}女士/先生，您申请的借款人身份已经认证成功,马上去借款吧！'
  , 'EVENT', 'REGISTER_LOANER_ACCOUNT_SUCCESS', 'ALL_USER', 'WEBSITE,APP_MESSAGE', 'SYSTEM'
  , 'https://tuotiansudai.com/personal-info', 'MY_ACCOUNT'
  , 'APPROVED', 0, NULL
  , 'sidneygao', '2018-07-05 00:00:00', '9999-12-31 23:59:59'
  , 'sidneygao', '2018-07-05 00:00:00'
  , 'sidneygao', '2018-07-05 00:00:00', 0);
COMMIT;