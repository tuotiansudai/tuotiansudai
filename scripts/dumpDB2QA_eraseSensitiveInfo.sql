BEGIN;
set foreign_key_checks=0;
update user set user_name=CONCAT('王拓天',CONVERT(id,char)), identity_number=LPAD(CONVERT(id,char),18,'0'), mobile = CONCAT('13800',LPAD(CONVERT(id,char),6,'0')), password='dd06471f173b76a85c8cae2519f9c0e85f042160', salt='5e0a84cdd76f4a89b085a0a842fe370c';
update prepare_user set referrer_mobile = '13800000001';
update account set _user_name=CONCAT('王拓天',CONVERT(id,char)), _identity_number=LPAD(CONVERT(id,char),18,'0'), pay_user_id=CONCAT('UB201601010000000000000000',LPAD(CONVERT(id,char),6,'0')), pay_account_id=CONCAT('02000155',LPAD(CONVERT(id,char),6,'0'));
update bank_card set card_number = '6222020000000000000';
COMMIT;