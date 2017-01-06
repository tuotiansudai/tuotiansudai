BEGIN;

 update message set app_url = 'FUND_DETAIL_LIST' where app_url = 'ASSESS_ADMINISTER';
 update message set app_url = 'MESSAGE_CENTER_LIST' where app_url = 'NOTIFY';
 update message set app_url = 'MY_ASSESS' where app_url = 'PERSON_CENTER_HOME';
 update message set app_url = 'INVITE_FRIEND' where app_url = 'RECOMMEND_DETAIL';
 update message set app_url = 'MY_INVEST_FINISH' where app_url = 'TRANSFER_HISTORY';

COMMIT;

