BEGIN ;
set foreign_key_checks=0;
update user set mobile = CONCAT('13800',LPAD(CONVERT(id,char),6,'0')), password='dd06471f173b76a85c8cae2519f9c0e85f042160', salt='5e0a84cdd76f4a89b085a0a842fe370c';
update prepare_user set referrer_mobile = '13800000001';
update account set user_name='王拓天', identity_number='111111111111111111', pay_user_id='UB201601010000000000000000000000', pay_account_id='02000155700000';
update bank_card set card_number = '6222020000000000000';
COMMIT ;
