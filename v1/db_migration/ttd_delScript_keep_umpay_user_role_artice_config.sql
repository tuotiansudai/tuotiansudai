set foreign_key_checks=0;
delete from auth_info;
delete from auto_invest;
delete from withdraw_cash;
delete from banner_picture_loan;
delete from borrower_addition_info;
delete from borrower_additional_info;
delete from borrower_authentication;
delete from borrower_personal_info;
delete from borrower_info;

delete from autc_mtr_picture;
delete from authentication_materials;
delete from coupon;
delete from inbox;
delete from invest_repay;
delete from invest_transfer;
delete from transfer_apply;
delete from invest;
delete from level_for_user;
delete from loan_guarantee_pics;
delete from loan_info_pics;
delete from banner_picture where banner_id is null;
delete from loan_node_attr;
delete from loan_repay;
delete from loan;
delete from motion_tracking;
delete from open_auth;
delete from qrtz_blob_triggers;
delete from qrtz_cron_triggers;
delete from qrtz_fired_triggers;
delete from qrtz_simple_triggers;
delete from qrtz_triggers;
delete from qrtz_job_details;
delete from recharge;
delete from real_name_certification_validate_log;
delete from sendbox;
delete from system_bill;
delete from system_money_log;
delete from trusteeship_operation;
delete from user_bill;
delete from user_coupon;
delete from user_level_history;
delete from user_login_log;
delete from user_message;
delete from user_point;
delete from user_point_history;
delete from apply_enterprise_loan;
delete from credit_rating_log;
delete from watchdog;
delete from `comment`;
update node set last_modify_user = 'admin' , creator ='admin';
delete from raise_invest;
delete from raise_loan;
delete from raise_loan_comments;
delete from raise_loan_likers;
delete from raise_loan_payback;

delete from `user` where id not in ('admin', 'sidneygao')';
delete from user_role where user_id not in ('admin', 'sidneygao');
delete from bank_card where user_id not in ('admin', 'sidneygao');
delete from trusteeship_account where user_id not in ('admin', 'sidneygao');