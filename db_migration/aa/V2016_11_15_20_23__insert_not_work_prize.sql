BEGIN;

INSERT INTO `aa`.`coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `coupon_source`, `comment`)
VALUES ('322', '2000', '0', '0', '0', '2016-12-01 00:00:00', '2016-12-23 00:00:00', '7', '0', '999999', '0', '1', '0',
                                                                                                             'sidneygao',
                                                                                                             '2016-11-20 00:00:00',
                                                                                                             'sidneygao',
                                                                                                             '2016-11-20 00:00:00',
                                                                                                             'sidneygao',
                                                                                                             '2016-11-20 00:00:00',
                                                                                                             '100000',
                                                                                                             '_90,_180,_360',
        'RED_ENVELOPE', 'ALL_USER', '0', '0', '拓天速贷平台赠送', '');

COMMIT;