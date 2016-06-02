BEGIN;

UPDATE coupon
SET product_types = REPLACE(product_types, '_180,_360', '_180');

COMMIT;