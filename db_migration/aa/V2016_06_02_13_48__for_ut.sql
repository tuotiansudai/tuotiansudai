BEGIN ;
UPDATE coupon
SET product_types = REPLACE(product_types, 'SYL', '_30');

UPDATE coupon
SET product_types = REPLACE(product_types, 'WYX', '_90');

UPDATE coupon
SET product_types = REPLACE(product_types, 'JYF', '_180,_360');
COMMIT ;