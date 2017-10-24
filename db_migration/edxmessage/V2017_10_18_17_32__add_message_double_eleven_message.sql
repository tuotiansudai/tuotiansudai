BEGIN;
INSERT INTO `message` (`title`, `template`, `template_txt`, `type`, `event_type`, `user_group`, `channels`, `message_category`, `status`, `read_count`, `push_id`, `activated_by`, `activated_time`, `valid_end_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('双十一活动投资奖励通知', '双十一活动投资奇数奖励',
                        '亲爱的用户，您投资的{0}项目的顺序为奇数，当项目满额放款后，我们将发送给您投资金额1.1倍的体验金奖励。', 'EVENT', 'DOUBLE_ELEVEN_ACTIVITY_EVEN', 'ALL_USER',
                        'WEBSITE,APP_MESSAGE', 'SYSTEM',
                        'APPROVED', 0,
                                    NULL,
                                    'sidneygao',
                                    '2017-10-18 00:00:00',
                                    '9999-12-31 23:59:59',
                                    'sidneygao',
                                    '2017-10-18 00:00:00',
                                    'sidneygao',
                                    '2017-10-18 00:00:00',
                                    0);

INSERT INTO `message` (`title`, `template`, `template_txt`, `type`, `event_type`, `user_group`, `channels`, `message_category`, `status`, `read_count`, `push_id`, `activated_by`, `activated_time`, `valid_end_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('双十一活动投资奖励通知', '双十一活动投资偶数奖励',
                        '亲爱的用户，您投资的{0}项目的顺序为偶数，获得了一次抽奖机会，您可以前往活动页面进行抽奖。', 'EVENT', 'DOUBLE_ELEVEN_ACTIVITY_ODD', 'ALL_USER',
                        'WEBSITE,APP_MESSAGE', 'SYSTEM',
                        'APPROVED', 0,
                                    NULL,
                                    'sidneygao',
                                    '2017-10-18 00:00:00',
                                    '9999-12-31 23:59:59',
                                    'sidneygao',
                                    '2017-10-18 00:00:00',
                                    'sidneygao',
                                    '2017-10-18 00:00:00',
                                    0);
COMMIT;