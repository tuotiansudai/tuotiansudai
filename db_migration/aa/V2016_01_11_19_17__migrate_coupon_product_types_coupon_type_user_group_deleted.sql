BEGIN;
update coupon set product_types = 'SYL,WYX,JYF',coupon_type='NEWBIE_COUPON',user_group='NEW_REGISTERED_USER',deleted=false ;
COMMIT;