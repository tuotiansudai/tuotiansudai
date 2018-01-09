BEGIN;

INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('481', '0', '0.005', '0', '1', '2018-02-10 00:00:00', '2018-02-28 23:59:59', '30', '0', '99999999', '0', '1', '0',
                                                                                                               'sidneygao',
                                                                                                               '2018-01-10 00:00:00',
                                                                                                               '5000',
                                                                                                               '_90,_180,_360',
                                                                                                               'INTEREST_COUPON',
                                                                                                               'ALL_USER',
                                                                                                               '0',
                                                                                                               '微信公众号回复我要更有钱');

INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('482', '0', '0.002', '0', '1', '2018-02-10 00:00:00', '2018-02-28 23:59:59', '30', '0', '99999999', '0', '1', '0',
                                                                                                               'sidneygao',
                                                                                                               '2018-01-10 00:00:00',
                                                                                                               '5000',
                                                                                                               '_90,_180,_360',
                                                                                                               'INTEREST_COUPON',
                                                                                                               'ALL_USER',
                                                                                                               '0',
                                                                                                               '微信公众号回复我要更有钱');

COMMIT;