begin;
INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `period`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`,
                      `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`,
                      `user_group`, `deleted`, `coupon_source`, `comment`)
VALUES (439, 5000, 0, 0, NULL, 1, '2017-07-01 00:00:00', '2017-07-16 23:59:59', '7', '0', '999999', '0',
                                                                                                    '1', '0',
                                                                                                    'sidneygao', now(),
                                                                                                    'sidneygao', now(),
                                                                                                    NULL, NULL,
                                                                                                    '1500000',
        '_90,_180,_360', 'RED_ENVELOPE',
        'WINNER', 0, '标王争霸场第二名', '');


INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `period`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`,
                      `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`,
                      `user_group`, `deleted`, `coupon_source`, `comment`)
VALUES (440, 2000, 0, 0, NULL, 1, '2017-07-01 00:00:00', '2017-07-16 23:59:59', '7', '0', '999999', '0',
                                                                                                    '1', '0',
                                                                                                    'sidneygao', now(),
                                                                                                    'sidneygao', now(),
                                                                                                    NULL, NULL,
                                                                                                    '800000',
        '_90,_180,_360', 'RED_ENVELOPE',
        'WINNER', 0, '标王争霸场第三名', '');

COMMIT ;