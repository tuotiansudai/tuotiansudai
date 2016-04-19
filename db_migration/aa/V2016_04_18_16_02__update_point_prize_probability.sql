BEGIN ;

UPDATE `point_prize` SET `probability` = 44 WHERE `name` = 'ThankYou';

UPDATE `point_prize` SET `probability` = 50 WHERE `name` = 'InterestCoupon2';

COMMIT ;