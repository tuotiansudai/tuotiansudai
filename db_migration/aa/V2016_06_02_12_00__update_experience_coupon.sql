BEGIN;

UPDATE
  coupon
SET
  user_group = 'NOT_ACCOUNT_NOT_INVESTED_USER'
WHERE user_group = 'NEW_REGISTERED_USER'
  AND product_types = 'EXPERIENCE';

COMMIT;