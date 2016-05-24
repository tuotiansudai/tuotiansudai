BEGIN;

UPDATE loan
SET product_type = '_30'
WHERE periods = 1;

UPDATE loan
SET product_type = '_90'
WHERE periods = 3;

UPDATE loan
SET product_type = '_180'
WHERE periods = 6;

UPDATE loan
SET product_type = '_360'
WHERE periods = 12;

UPDATE coupon
SET product_types = REPLACE(product_types, 'SYL', '_30');

UPDATE coupon
SET product_types = REPLACE(product_types, 'WYX', '_90');

UPDATE coupon
SET product_types = REPLACE(product_types, 'JYF', '_180,_360');

COMMIT;