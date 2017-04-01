BEGIN;

UPDATE invest_referrer_reward SET referrer_role = 'SD_STAFF'
WHERE referrer_role = 'STAFF' AND EXISTS(SELECT 1 FROM user_role WHERE user_role.login_name = invest_referrer_reward.referrer_login_name AND user_role.role = 'SD_STAFF');

UPDATE invest_referrer_reward SET referrer_role = 'ZC_STAFF'
WHERE referrer_role = 'STAFF' AND EXISTS(SELECT 1 FROM user_role WHERE user_role.login_name = invest_referrer_reward.referrer_login_name AND user_role.role = 'ZC_STAFF');

UPDATE invest_referrer_reward SET referrer_role = 'INVESTOR'
WHERE referrer_role = 'STAFF' AND EXISTS(SELECT 1 FROM user_role WHERE user_role.login_name = invest_referrer_reward.referrer_login_name AND user_role.role = 'INVESTOR');

UPDATE invest_referrer_reward SET referrer_role = 'USER'
WHERE referrer_role = 'STAFF' AND NOT EXISTS(SELECT 1 FROM user_role WHERE user_role.login_name = invest_referrer_reward.referrer_login_name AND user_role.role = 'INVESTOR');

COMMIT;
