BEGIN;
 DELETE FROM user_message
  WHERE
    NOT EXISTS( SELECT
        *
    FROM
        account

    WHERE
        DATE(register_time) = '2016-08-16'
        AND login_name = user_message.login_name)
    AND title = '实名认证成功，您的支付密码已经由联动优势发送至注册手机号码，请牢记。'
    AND DATE(created_time) = '2016-08-16';



   DELETE FROM user_message
    WHERE
    NOT EXISTS( SELECT
        *
    FROM
        user

    WHERE
        DATE(register_time) = '2016-08-16'
        AND login_name = user_message.login_name)
    AND title = '终于等到你，欢迎来到拓天速贷平台。'
    AND DATE(created_time) = '2016-08-16';


    DELETE FROM user_message
    WHERE
    DATE(created_time) = '2016-08-16'
    AND title REGEXP '\{[012]\}';

COMMIT;