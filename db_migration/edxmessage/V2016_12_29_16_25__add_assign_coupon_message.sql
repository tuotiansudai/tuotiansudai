BEGIN;
INSERT INTO `message` (`title`, `template`, `template_txt`, `type`, `event_type`, `user_group`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `push_id`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('恭喜您获得了一张{0}！', '尊敬的用户，恭喜您获得了一张{0}，有效期至{1}！',
                        '尊敬的用户，恭喜您获得了一张{0}，有效期至{1}！', 'EVENT', 'ASSIGN_COUPON_SUCCESS', 'ALL_USER',
                        'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/my-treasure', 'MY_TREASURE_UNUSED',
                        'APPROVED', 0,
                                    NULL,
                                    'sidneygao',
                                    '2016-11-01 00:00:00',
                                    '9999-12-31 23:59:59',
                                    'sidneygao',
                                    '2016-11-01 00:00:00',
                                    'sidneygao',
                                    '2016-11-01 00:00:00',
                                    0);
COMMIT;