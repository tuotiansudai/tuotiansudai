BEGIN;
  DELETE FROM user_message
  WHERE
    login_name IN (SELECT
        um.*
    FROM
        account a
            LEFT JOIN
        (SELECT
            m.login_name login_name
        FROM
            user_message m

        WHERE
            title = '实名认证成功，您的支付密码已经由联动优势发送至注册手机号码，请牢记。'
            AND DATE(created_time) = '2016-08-16') um ON um.login_name = a.login_name

    WHERE
        um.login_name IS NOT NULL
        and (a.register_time) = '2016-08-16');


   DELETE FROM user_message
    WHERE
    login_name IN (SELECT
        um.*
    FROM
        user u
            LEFT JOIN
        (SELECT
            m.login_name login_name
        FROM
            user_message m

        WHERE
            title = '终于等到你，欢迎来到拓天速贷平台。'
            AND DATE(created_time) = '2016-08-16') um ON um.login_name = u.login_name

    WHERE
        um.login_name IS NOT NULL
        and  (u.register_time) = '2016-08-16');

    DELETE FROM user_message
    WHERE
    DATE(created_time) = '2016-08-16'
    AND title REGEXP '%[{0}|{1}|{2}]%';

COMMIT;