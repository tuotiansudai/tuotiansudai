BEGIN;
INSERT INTO edxmessage.`message` (`title`
  , `template`, `template_txt`
  , `type`, `event_type`, `user_group`, `channels`, `message_category`
  , `web_url`, `app_url`
  , `status`, `read_count`, `push_id`
  , `activated_by`, `activated_time`, `valid_end_time`
  , `created_by`, `created_time`
  , `updated_by`, `updated_time`, `deleted`)
VALUES ('债券转让成功'
  , '您的借款项目有出借人进行了债权转让，请查看', '尊敬的用户，您的借款项目【{0}】的出借人【{1}】转让了{2}元的债权，接收人为【{3}】，此短信为系统自动发送，请勿回复。'
  , 'EVENT', 'TRANSFER_SUCCESS_LOANER', 'ALL_USER', 'WEBSITE,APP_MESSAGE', 'SYSTEM'
  , 'https://tuotiansudai.com/loan-list', 'INVEST_NORMAL'
  , 'APPROVED', 0, NULL
  , 'sidneygao', '2017-05-11 00:00:00', '9999-12-31 23:59:59'
  , 'sidneygao', '2017-05-11 00:00:00'
  , 'sidneygao', '2017-05-11 00:00:00', 0);
COMMIT;