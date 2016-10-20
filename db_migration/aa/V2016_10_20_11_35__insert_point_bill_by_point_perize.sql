BEGIN;

insert into aa.point_bill(login_name,point,business_type,note,created_time)
select login_name,
		case activity_category
			when 'POINT_DRAW_1000' then 1000
			when 'POINT_DRAW_10000' then 10000 end,
		'ACTIVITY',
		case prize
			when 'MASK' then '抽中防雾霾骑行口罩'
			when 'LIPSTICK' then '抽中屈臣氏润唇膏'
			when 'PORCELAIN_CUP_BY_1000' then '抽中青花瓷杯子'
			when 'PHONE_BRACKET' then '抽中懒人手机支架'
			when 'PHONE_CHARGE_10' then '抽中10元话费'
			when 'RED_ENVELOPE_10' then '抽中10元投资红包'
			when 'INTEREST_COUPON_2_POINT_DRAW' then '抽中0.2%加息券'
			when 'DELAYED_ACTION' then '抽中通用自拍杆'
			when 'U_DISH' then '抽中拓天速贷U盘'
			when 'PHONE_CHARGE_20' then '抽中20元话费'
			when 'HEADREST' then '抽中车家两用U型头枕'
			when 'IQIYI_MEMBERSHIP_30' then '抽中爱奇艺会员月卡'
			when 'RED_ENVELOPE_50_POINT_DRAW' then '抽中50元投资红包'
			when 'INTEREST_COUPON_5_POINT_DRAW' then '抽中0.5%加息券' end,
        lottery_time
 from edxactivity.user_lottery_prize where activity_category in ('POINT_DRAW_1000','POINT_DRAW_10000');


COMMIT;