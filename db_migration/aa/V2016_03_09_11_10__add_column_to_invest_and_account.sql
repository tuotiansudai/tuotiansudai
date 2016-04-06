ALTER TABLE invest ADD COLUMN is_no_password_invest BOOLEAN default FALSE AFTER channel;

ALTER TABLE account ADD COLUMN no_password_invest BOOLEAN default FALSE AFTER auto_invest;
