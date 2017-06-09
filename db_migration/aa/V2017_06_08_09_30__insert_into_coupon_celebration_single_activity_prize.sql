BEGIN;
INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('441', '500', '0', '0', '1', '2017-07-17 00:00:00', '2017-07-31 23:59:59', '15', '0', '999999', '0', '1', '0',
                                                                                                             'sidneygao',
                                                                                                             '2017-06-08 00:00:00',
                                                                                                             '400000',
                                                                                                             '_90,_180,_360',
                                                                                                             'RED_ENVELOPE',
                                                                                                             'WINNER',
                                                                                                             '0',
                                                                                                             '周年庆幸运大抽奖获得');
INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('442', '1000', '0', '0', '1', '2017-07-17 00:00:00', '2017-07-31 23:59:59', '15', '0', '999999', '0', '1', '0',
                                                                                                              'sidneygao',
                                                                                                              '2017-06-08 00:00:00',
                                                                                                              '800000',
                                                                                                              '_90,_180,_360',
                                                                                                              'RED_ENVELOPE',
                                                                                                              'WINNER',
                                                                                                              '0',
                                                                                                              '周年庆幸运大抽奖获得');
INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('443', '3000', '0', '0', '1', '2017-07-17 00:00:00', '2017-07-31 23:59:59', '15', '0', '999999', '0', '1', '0',
                                                                                                              'sidneygao',
                                                                                                              '2017-06-08 00:00:00',
                                                                                                              '2400000',
                                                                                                              '_90,_180,_360',
                                                                                                              'RED_ENVELOPE',
                                                                                                              'WINNER',
                                                                                                              '0',
                                                                                                              '周年庆幸运大抽奖获得');
INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('444', '0', '0.005', '0', '1', '2017-07-17 00:00:00', '2017-07-31 23:59:59', '15', '0', '999999', '0', '1', '0',
                                                                                                               'sidneygao',
                                                                                                               '2017-06-08 00:00:00',
                                                                                                               '5000',
                                                                                                               '_90,_180,_360',
                                                                                                               'INTEREST_COUPON',
                                                                                                               'WINNER',
                                                                                                               '0',
                                                                                                               '周年庆幸运大抽奖获得');
COMMIT;