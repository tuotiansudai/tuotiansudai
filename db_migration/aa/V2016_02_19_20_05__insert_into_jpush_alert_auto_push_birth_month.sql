BEGIN;
update jpush_alert set content = '亲爱的天宝生日快乐！生日月投资收益翻倍，最高26%，不要错过哟！',jump_to_link='/activity/birth-month?source=app' where push_type = 'BIRTHDAY_ALERT_MONTH' and is_automatic = true;
COMMIT ;