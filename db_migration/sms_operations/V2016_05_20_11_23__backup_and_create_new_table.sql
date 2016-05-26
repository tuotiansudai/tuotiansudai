RENAME TABLE coupon_notify TO zucp_coupon_notify;
RENAME TABLE fatal_notify TO zucp_fatal_notify;
RENAME TABLE invest_notify TO zucp_invest_notify;
RENAME TABLE loan_repay_notify TO zucp_loan_repay_notify;
RENAME TABLE no_password_invest_captcha TO zucp_no_password_invest_captcha;
RENAME TABLE register_captcha TO zucp_register_captcha;
RENAME TABLE retrieve_password_captcha TO zucp_retrieve_password_captcha;
RENAME TABLE user_password_changed_notify TO zucp_user_password_changed_notify;

CREATE TABLE coupon_notify LIKE zucp_coupon_notify;
CREATE TABLE fatal_notify LIKE zucp_fatal_notify;
CREATE TABLE invest_notify LIKE zucp_invest_notify;
CREATE TABLE loan_repay_notify LIKE zucp_loan_repay_notify;
CREATE TABLE no_password_invest_captcha LIKE zucp_no_password_invest_captcha;
CREATE TABLE register_captcha LIKE zucp_register_captcha;
CREATE TABLE retrieve_password_captcha LIKE zucp_retrieve_password_captcha;
CREATE TABLE user_password_changed_notify LIKE zucp_user_password_changed_notify;

ALTER TABLE `sms_operations`.`coupon_notify` DROP COLUMN `ext`, DROP COLUMN `stime`, DROP COLUMN `rrid`;
ALTER TABLE `sms_operations`.`fatal_notify` DROP COLUMN `ext`, DROP COLUMN `stime`, DROP COLUMN `rrid`;
ALTER TABLE `sms_operations`.`invest_notify` DROP COLUMN `ext`, DROP COLUMN `stime`, DROP COLUMN `rrid`;
ALTER TABLE `sms_operations`.`loan_repay_notify` DROP COLUMN `ext`, DROP COLUMN `stime`, DROP COLUMN `rrid`;
ALTER TABLE `sms_operations`.`no_password_invest_captcha` DROP COLUMN `ext`, DROP COLUMN `stime`, DROP COLUMN `rrid`;
ALTER TABLE `sms_operations`.`register_captcha` DROP COLUMN `ext`, DROP COLUMN `stime`, DROP COLUMN `rrid`;
ALTER TABLE `sms_operations`.`retrieve_password_captcha` DROP COLUMN `ext`, DROP COLUMN `stime`, DROP COLUMN `rrid`;
ALTER TABLE `sms_operations`.`user_password_changed_notify` DROP COLUMN `ext`, DROP COLUMN `stime`, DROP COLUMN `rrid`;
