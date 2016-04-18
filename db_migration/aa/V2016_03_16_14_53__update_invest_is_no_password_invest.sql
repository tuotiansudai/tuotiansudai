BEGIN ;

UPDATE invest SET is_no_password_invest = is_auto_invest;

COMMIT ;