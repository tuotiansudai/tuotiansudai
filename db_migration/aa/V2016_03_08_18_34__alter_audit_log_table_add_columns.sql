ALTER TABLE audit_log DROP FOREIGN KEY FK_AUDIT_LOG_OPERATOR_OPERATOR_REF_USER_LOGIN_NAME;
ALTER TABLE audit_log DROP KEY FK_AUDIT_LOG_OPERATOR_OPERATOR_REF_USER_LOGIN_NAME;

ALTER TABLE audit_log DROP FOREIGN KEY FK_AUDIT_LOG_OPERATOR_LOGIN_NAME_REF_USER_LOGIN_NAME;
ALTER TABLE audit_log DROP KEY FK_AUDIT_LOG_OPERATOR_LOGIN_NAME_REF_USER_LOGIN_NAME;

ALTER TABLE audit_log ADD COLUMN auditor_login_name VARCHAR(25) AFTER id;
ALTER TABLE audit_log CHANGE login_name target_id VARCHAR(25) AFTER operator_login_name;
ALTER TABLE audit_log ADD COLUMN operation_type VARCHAR(20) NOT NULL AFTER target_id;

ALTER TABLE audit_log ADD CONSTRAINT FK_AUDIT_LOG_AUDITOR_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`auditor_login_name`) REFERENCES `aa`.`user` (`login_name`);
ALTER TABLE audit_log ADD CONSTRAINT FK_AUDIT_LOG_OPERATOR_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`operator_login_name`) REFERENCES `aa`.`user` (`login_name`);

BEGIN;
UPDATE audit_log SET auditor_login_name = operator_login_name, operator_login_name = target_id, target_id = null where operation_time > '2016-03-04 11:00:00';
UPDATE audit_log SET operation_type = 'USER';
COMMIT;