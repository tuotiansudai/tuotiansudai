BEGIN ;
alter table feedback drop foreign key FK_FEEDBACK_LOGIN_NAME_REF_USER_LOGIN_NAME;
COMMIT ;