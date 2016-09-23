BEGIN;

INSERT INTO `message` (`title`, `app_title`, `template`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '会员到期提醒',
    '会员到期提醒',
    '您购买的V5会员已到期，V5会员可享受服务费7折优惠，记得及时购买哦！',
    'EVENT',
    'MEMBERSHIP_EXPIRED',
    'ALL_USER',
    'WEBSITE,APP_MESSAGE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');
COMMIT;