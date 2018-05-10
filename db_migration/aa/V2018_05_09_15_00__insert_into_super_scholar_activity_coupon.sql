BEGIN;

INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('495', '0', '0.002', '0', '1', '2018-06-01 00:00:00', '2018-06-31 23:59:59', '30', '0', '99999999', '0', '1', '0',
                                                                                                               'sidneygao',
                                                                                                               '2018-05-09 00:00:00',
                                                                                                               '5000',
                                                                                                               '_90,_180,_360',
                                                                                                               'INTEREST_COUPON',
                                                                                                               'WINNER',
                                                                                                               '0',
                                                                                                               '6月答题赢加薪');

INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('496', '0', '0.005', '0', '1', '2018-06-01 00:00:00', '2018-06-31 23:59:59', '30', '0', '99999999', '0', '1', '0',
                                                                                                               'sidneygao',
                                                                                                               '2018-05-09 00:00:00',
                                                                                                               '5000',
                                                                                                               '_90,_180,_360',
                                                                                                               'INTEREST_COUPON',
                                                                                                               'WINNER',
                                                                                                               '0',
                                                                                                               '6月答题赢加薪');

COMMIT;