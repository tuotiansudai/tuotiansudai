BEGIN ;
UPDATE
  `user_message_template`
SET
  `template` = '您的找回密码验证码是：#{authCode}，请勿告诉他人，如有疑问请联系客服：#{site_phone}【拓天速贷】'
WHERE `id` = 'find_login_password_by_mobile_sms' ;
COMMIT ;