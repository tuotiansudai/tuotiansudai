ALTER TABLE invest ADD COLUMN is_no_password_invest BOOLEAN default FALSE AFTER channel;

ALTER TABLE account ADD COLUMN no_password_invest BOOLEAN AFTER auto_invest;

UPDATE account SET no_password_invest = false WHERE auto_invest = 1;
