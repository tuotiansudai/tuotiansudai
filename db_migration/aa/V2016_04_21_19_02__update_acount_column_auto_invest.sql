BEGIN ;
update account set auto_invest = 0 where login_name in ('djcrp','huangmin11','liangjinhua','zhoujinmeng','lt5566');
COMMIT ;