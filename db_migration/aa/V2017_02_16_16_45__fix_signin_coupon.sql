BEGIN;

UPDATE `aa`.`coupon` SET `multiple` = 1 WHERE `id` IN ('376', '377', '378', '379', '380', '381') AND `coupon_source` = '签到红包';

COMMIT;