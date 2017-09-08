BEGIN;
INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `period`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('463', '0', '0.068', '0', '1', '1', '2017-09-25 00:00:00', '2017-10-15 23:59:59', '0', '0', '99999999', '0', '1', '0',
                                                                                                               'sidneygao',
                                                                                                               '2017-09-07 00:00:00',
                                                                                                               '0',
                                                                                                               '_180',
                                                                                                               'INTEREST_COUPON',
                                                                                                               'ALL_USER',
                                                                                                               '0',
                                                                                                               '国庆遇中秋活动奖励');
COMMIT;