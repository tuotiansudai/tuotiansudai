BEGIN;

UPDATE loan
SET loan_amount = 68880000
WHERE id = 1;

INSERT INTO coupon (`id`,
                    `amount`,
                    `rate`,
                    `birthday_benefit`,
                    `multiple`,
                    `start_time`,
                    `end_time`,
                    `deadline`,
                    `used_count`,
                    `total_count`,
                    `issued_count`,
                    `active`,
                    `created_by`,
                    `created_time`,
                    `activated_by`,
                    `activated_time`,
                    `updated_by`,
                    `updated_time`,
                    `invest_lower_limit`,
                    `product_types`,
                    `coupon_type`,
                    `user_group`,
                    `coupon_source`)
  SELECT
    382,
    688800,
    0,
    0,
    0,
    '2017-01-01',
    '9999-12-31',
    30,
    0,
    1000000,
    0,
    TRUE,
    'sidneygao',
    now(),
    'sidneygao',
    now(),
    'sidneygao',
    now(),
    0,
    'EXPERIENCE',
    'NEWBIE_COUPON',
    'NEW_REGISTERED_USER',
    '注册奖励'
  FROM dual
  WHERE EXISTS(SELECT *
               FROM `user`
               WHERE login_name = 'sidneygao');

UPDATE coupon
SET deleted = '1'
WHERE amount = 588800 AND `product_types` = 'EXPERIENCE' AND `coupon_type` = 'NEWBIE_COUPON';

UPDATE coupon
SET deleted = '1'
WHERE id IN (315, 316, 317, 318, 319, 100033);

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (383, 22200, 0, 0, 0, '2017-01-01 00:00:00', '2017-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                              '2017-01-01 00:00:00',
                                                                                              'sidneygao',
                                                                                              '2017-01-01 00:00:00',
                                                                                              NULL,
                                                                                              '2017-01-01 00:00:00',
                                                                                              6000000, '_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (384, 17800, 0, 0, 0, '2017-01-01 00:00:00', '2017-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                              '2017-01-01 00:00:00',
                                                                                              'sidneygao',
                                                                                              '2017-01-01 00:00:00',
                                                                                              NULL,
                                                                                              '2017-01-01 00:00:00',
                                                                                              5000000, '_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (385, 9800, 0, 0, 0, '2017-01-01 00:00:00', '2017-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                             '2017-01-01 00:00:00',
                                                                                             'sidneygao',
                                                                                             '2017-01-01 00:00:00',
                                                                                             NULL,
                                                                                             '2017-01-01 00:00:00',
                                                                                             3000000, '_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (386, 5800, 0, 0, 0, '2017-01-01 00:00:00', '2017-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                             '2017-01-01 00:00:00',
                                                                                             'sidneygao',
                                                                                             '2017-01-01 00:00:00',
                                                                                             NULL,
                                                                                             '2017-01-01 00:00:00',
                                                                                             600000, '_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (387, 4800, 0, 0, 0, '2017-01-01 00:00:00', '2017-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                             '2017-01-01 00:00:00',
                                                                                             'sidneygao',
                                                                                             '2017-01-01 00:00:00',
                                                                                             NULL,
                                                                                             '2017-01-01 00:00:00',
                                                                                             3000000, '_90,_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (388, 3800, 0, 0, 0, '2017-01-01 00:00:00', '2017-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                             '2017-01-01 00:00:00',
                                                                                             'sidneygao',
                                                                                             '2017-01-01 00:00:00',
                                                                                             NULL,
                                                                                             '2017-01-01 00:00:00',
                                                                                             2000000, '_90,_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (389, 1800, 0, 0, 0, '2017-01-01 00:00:00', '2017-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                             '2017-01-01 00:00:00',
                                                                                             'sidneygao',
                                                                                             '2017-01-01 00:00:00',
                                                                                             NULL,
                                                                                             '2017-01-01 00:00:00',
                                                                                             900000, '_90,_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `comment`, `coupon_source`)
VALUES (390, 800, 0, 0, 0, '2017-01-01 00:00:00', '2017-12-31 23:59:59', 30, 0, 9999999, 0, 1, 0, 'sidneygao',
                                                                                            '2017-01-01 00:00:00',
                                                                                            'sidneygao',
                                                                                            '2017-01-01 00:00:00', NULL,
                                                                                            '2017-01-01 00:00:00',
                                                                                            100000, '_30,_90,_180,_360',
        'RED_ENVELOPE', 'NEW_REGISTERED_USER', 0, 0, '', '注册奖励');

INSERT INTO `coupon` (`id`, `amount`, `rate`, `birthday_benefit`, `period`, `multiple`, `start_time`, `end_time`, `deadline`, `used_count`, `total_count`, `issued_count`, `active`, `shared`, `created_by`, `created_time`, `activated_by`, `activated_time`, `updated_by`, `updated_time`, `invest_lower_limit`, `product_types`, `coupon_type`, `user_group`, `sms_alert`, `deleted`, `coupon_source`, `comment`)
VALUES (391, 0, 0.03, 0, 1, 0, '2017-01-01 00:00:00', '2017-12-31 23:59:59', '30', '0', '999999', '0', '1', '0',
                                                                                                  'sidneygao',
                                                                                                  '2017-01-01 00:00:00',
                                                                                                  'sidneygao',
                                                                                                  '2017-01-01 00:00:00',
                                                                                                  NULL,
                                                                                                  '2017-01-01 00:00:00',
                                                                                                  '5000',
        '_30,_90,_180,_360',
        'INTEREST_COUPON', 'WINNER', 0, 0, '拓天速贷平台赠送', '');

UPDATE coupon
SET period = 1
WHERE coupon_type = 'BIRTHDAY_COUPON';

COMMIT;