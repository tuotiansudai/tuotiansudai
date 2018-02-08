BEGIN;
update user set city = province where `province` in ('北京','上海','重庆','天津','香港','澳门') and `city` = '未知';
COMMIT;