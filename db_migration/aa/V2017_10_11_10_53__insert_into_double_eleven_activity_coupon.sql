BEGIN;

INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('469', '2000', '0', '0', '1', '2017-11-06 00:00:00', '2017-12-03 23:59:59', '0', '0', '99999999', '0', '1', '0',
                                                                                                               'sidneygao',
                                                                                                               '2017-11-06 00:00:00',
                                                                                                               '1000000',
                                                                                                               '_180,_360',
                                                                                                               'RED_ENVELOPE',
                                                                                                               'ALL_USER',
                                                                                                               '0',
                                                                                                               '双十一剁手抽奖活动奖励');

INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('470', '5000', '0', '0', '1', '2017-11-06 00:00:00', '2017-12-03 23:59:59', '0', '0', '99999999', '0', '1', '0',
                                                                                                               'sidneygao',
                                                                                                               '2017-11-06 00:00:00',
                                                                                                               '2500000',
                                                                                                               '_180,_360',
                                                                                                               'RED_ENVELOPE',
                                                                                                               'WINNER',
                                                                                                               '0',
                                                                                                               '双十一剁手抽奖活动奖励');

INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('471', '10000', '0', '0', '1', '2017-11-06 00:00:00', '2017-12-03 23:59:59', '0', '0', '99999999', '0', '1', '0',
                                                                                                               'sidneygao',
                                                                                                               '2017-11-06 00:00:00',
                                                                                                               '2500000',
                                                                                                               '_360',
                                                                                                               'RED_ENVELOPE',
                                                                                                               'WINNER',
                                                                                                               '0',
                                                                                                               '双十一剁手抽奖活动奖励');

INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('472', '20000', '0', '0', '1', '2017-11-06 00:00:00', '2017-12-03 23:59:59', '0', '0', '99999999', '0', '1', '0',
                                                                                                               'sidneygao',
                                                                                                               '2017-11-06 00:00:00',
                                                                                                               '5000000',
                                                                                                               '_360',
                                                                                                               'RED_ENVELOPE',
                                                                                                               'WINNER',
                                                                                                               '0',
                                                                                                               '双十一剁手抽奖活动奖励');

INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('473', '0', '0.002', '0', '1', '2017-11-06 00:00:00', '2017-12-03 23:59:59', '0', '0', '999999', '0', '1', '0',
                                                                                                               'sidneygao',
                                                                                                               '2017-11-06 00:00:00',
                                                                                                               '5000',
                                                                                                               '_90,_180,_360',
                                                                                                               'INTEREST_COUPON',
                                                                                                               'WINNER',
                                                                                                               '0',
                                                                                                               '双十一剁手抽奖活动奖励');
INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `deleted`, `coupon_source`)
VALUES ('474', '0', '0.005', '0', '1', '2017-11-06 00:00:00', '2017-12-03 23:59:59', '0', '0', '999999', '0', '1', '0',
                                                                                                               'sidneygao',
                                                                                                               '2017-11-06 00:00:00',
                                                                                                               '5000',
                                                                                                               '_90,_180,_360',
                                                                                                               'INTEREST_COUPON',
                                                                                                               'WINNER',
                                                                                                               '0',
                                                                                                               '双十一剁手抽奖活动奖励');

COMMIT;