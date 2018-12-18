ALTER TABLE `user`
  ADD staff_referrer_mobile VARCHAR(11);
ALTER TABLE `user`
  ADD INDEX INDEX_USER_STAFF_REFERRER_MOBILE (`staff_referrer_mobile`);

UPDATE user
  INNER JOIN (SELECT
                login_name,
                (SELECT user.mobile
                 FROM user
                 WHERE user.login_name = temp1.referrer_login_name) AS staff_mobile
              FROM referrer_relation temp1
              WHERE level = (SELECT max(level)
                             FROM referrer_relation temp2
                             WHERE temp1.login_name = temp2.login_name)
                    AND exists(SELECT 1
                                  FROM user_role
                                  WHERE user_role.login_name = temp1.referrer_login_name AND
                                        user_role.role IN ('SD_STAFF', 'ZC_STAFF'))) referrer_relation
    ON user.login_name = referrer_relation.login_name
SET user.staff_referrer_mobile = referrer_relation.staff_mobile