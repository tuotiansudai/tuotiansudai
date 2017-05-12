BEGIN;
INSERT INTO `message` (`title`, `template`, `template_txt`, `type`, `event_type`, `user_group`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `push_id`, `activated_by`, `activated_time`, `valid_end_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('{0}元活动奖励体验进已到账！', '亲爱的用户，您在母亲节活动期间投资{0}元，获得{1}元体验金请注意查收。',
                        '亲爱的用户，您在母亲节活动期间投资{0}元，获得{1}元体验金请注意查收。', 'EVENT', 'ASSIGN_EXPERIENCE_SUCCESS', 'ALL_USER',
                        'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/loan-list', 'INVEST_NORMAL',
                        'APPROVED', 0,
                                    NULL,
                                    'sidneygao',
                                    '2017-05-11 00:00:00',
                                    '9999-12-31 23:59:59',
                                    'sidneygao',
                                    '2017-05-11 00:00:00',
                                    'sidneygao',
                                    '2017-05-11 00:00:00',
                                    0);
COMMIT;