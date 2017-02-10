BEGIN;

UPDATE user_bill
SET business_type = 'MEMBERSHIP_PRIVILEGE_PURCHASE'
WHERE `business_type` = 'MEMBERSHIP_PURCHASE';

UPDATE system_bill
SET business_type = 'MEMBERSHIP_PRIVILEGE_PURCHASE'
WHERE `business_type` = 'MEMBERSHIP_PURCHASE';


DELETE FROM user_membership
WHERE type = 'PURCHASED';

COMMIT;