ALTER TABLE experience_account ADD UNIQUE (`login_name`);
ALTER TABLE user_sign_in ADD UNIQUE (`login_name`);
