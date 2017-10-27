BEGIN;

UPDATE message SET app_url='INVEST_NORMAL_EXPERIENCE' WHERE event_type='REGISTER_USER_SUCCESS';
UPDATE message SET app_url='MY_ACCOUNT' WHERE event_type='REGISTER_ACCOUNT_SUCCESS';
UPDATE message SET app_url='FUND_DETAIL_SPENDING' WHERE event_type='WITHDRAW_SUCCESS';
UPDATE message SET app_url='MY_INVEST_DETAIL' WHERE event_type='INVEST_SUCCESS';
UPDATE message SET app_url='MY_INVEST_DETAIL' WHERE event_type='TRANSFER_SUCCESS';
UPDATE message SET app_url='MY_INVEST_DETAIL' WHERE event_type='TRANSFER_FAIL';
UPDATE message SET app_url='MY_INVEST_DETAIL' WHERE event_type='LOAN_OUT_SUCCESS';
UPDATE message SET app_url='MY_INVEST_DETAIL' WHERE event_type='REPAY_SUCCESS';
UPDATE message SET app_url='RECOMMEND_LIST' WHERE event_type='RECOMMEND_SUCCESS';
UPDATE message SET app_url='CASH_WITHDRAWAL_LIST' WHERE event_type='WITHDRAW_APPLICATION_SUCCESS';
UPDATE message SET app_url='MY_INVEST_DETAIL' WHERE event_type='ADVANCED_REPAY';
UPDATE message SET app_url='INVEST_NORMAL_EXPERIENCE' WHERE event_type='NEWMAN_TYRANT';
UPDATE message SET app_url='INVEST_NORMAL_EXPERIENCE' WHERE event_type='ASSIGN_EXPERIENCE_SUCCESS';

COMMIT;