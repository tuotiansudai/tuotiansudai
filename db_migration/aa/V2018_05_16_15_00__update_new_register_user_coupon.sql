BEGIN;

UPDATE coupon
SET deleted = 1
WHERE id IN (383, 384, 385, 386, 387, 389, 390);

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (497, 800, 0, 0, 0, '2018-01-01 00:00:00', '2020-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                            '2018-01-01 00:00:00',
                                                                                            'sidneygao',
                                                                                            '2018-01-01 00:00:00',
                                                                                            NULL,
                                                                                            '2018-01-01 00:00:00',
                                                                                            100000, '_90,_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (498, 2800, 0, 0, 0, '2018-01-01 00:00:00', '2020-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                             '2018-01-01 00:00:00',
                                                                                             'sidneygao',
                                                                                             '2018-01-01 00:00:00',
                                                                                             NULL,
                                                                                             '2018-01-01 00:00:00',
                                                                                             500000, '_90,_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (499, 6000, 0, 0, 0, '2018-01-01 00:00:00', '2020-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                             '2018-01-01 00:00:00',
                                                                                             'sidneygao',
                                                                                             '2018-01-01 00:00:00',
                                                                                             NULL,
                                                                                             '2018-01-01 00:00:00',
                                                                                             1000000, '_90,_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (500, 8000, 0, 0, 0, '2018-01-01 00:00:00', '2020-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                             '2018-01-01 00:00:00',
                                                                                             'sidneygao',
                                                                                             '2018-01-01 00:00:00',
                                                                                             NULL,
                                                                                             '2018-01-01 00:00:00',
                                                                                             1500000, '_90,_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (501, 10000, 0, 0, 0, '2018-01-01 00:00:00', '2020-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                              '2018-01-01 00:00:00',
                                                                                              'sidneygao',
                                                                                              '2018-01-01 00:00:00',
                                                                                              NULL,
                                                                                              '2018-01-01 00:00:00',
                                                                                              2000000, '_90,_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (502, 20000, 0, 0, 0, '2018-01-01 00:00:00', '2020-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                              '2018-01-01 00:00:00',
                                                                                              'sidneygao',
                                                                                              '2018-01-01 00:00:00',
                                                                                              NULL,
                                                                                              '2018-01-01 00:00:00',
                                                                                              5000000, '_90,_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (503, 5800, 0, 0, 0, '2018-01-01 00:00:00', '2020-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                             '2018-01-01 00:00:00',
                                                                                             'sidneygao',
                                                                                             '2018-01-01 00:00:00',
                                                                                             NULL,
                                                                                             '2018-01-01 00:00:00',
                                                                                             600000, '_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (504, 10800, 0, 0, 0, '2018-01-01 00:00:00', '2020-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                              '2018-01-01 00:00:00',
                                                                                              'sidneygao',
                                                                                              '2018-01-01 00:00:00',
                                                                                              NULL,
                                                                                              '2018-01-01 00:00:00',
                                                                                              1000000, '_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (505, 20000, 0, 0, 0, '2018-01-01 00:00:00', '2020-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                              '2018-01-01 00:00:00',
                                                                                              'sidneygao',
                                                                                              '2018-01-01 00:00:00',
                                                                                              NULL,
                                                                                              '2018-01-01 00:00:00',
                                                                                              2000000, '_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (506, 15800, 0, 0, 0, '2018-01-01 00:00:00', '2020-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                              '2018-01-01 00:00:00',
                                                                                              'sidneygao',
                                                                                              '2018-01-01 00:00:00',
                                                                                              NULL,
                                                                                              '2018-01-01 00:00:00',
                                                                                              1000000, '_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

COMMIT;