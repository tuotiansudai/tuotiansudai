BEGIN;

 update message set app_url = 'ASSESS_ADMINISTER' where app_url = 'FUND_DETAIL_LIST';
 update message set app_url = 'NOTIFY' where app_url = 'MESSAGE_CENTER_LIST';
 update message set app_url = 'PERSON_CENTER_HOME' where app_url = 'MY_ASSESS';
 update message set app_url = 'RECOMMEND_DETAIL' where app_url = 'INVITE_FRIEND';
 update message set app_url = 'TRANSFER_HISTORY' where app_url = 'MY_INVEST_FINISH';

COMMIT;

