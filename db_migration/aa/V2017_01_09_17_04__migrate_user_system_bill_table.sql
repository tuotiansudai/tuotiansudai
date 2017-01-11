begin;
UPDATE user_bill set business_type = 'MEMBERSHIP_PRIVILEGE_PURCHASE' where `business_type` ='MEMBERSHIP_PURCHASE';
UPDATE system_bill set business_type = 'MEMBERSHIP_PRIVILEGE_PURCHASE' where `business_type` ='MEMBERSHIP_PURCHASE';
COMMIT ;