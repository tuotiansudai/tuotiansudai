BEGIN;
UPDATE coupon
SET product_types = 'SYL,WYX,JYF', coupon_type = 'NEWBIE_COUPON', user_group = 'NEW_REGISTERED_USER', deleted = FALSE;
COMMIT;