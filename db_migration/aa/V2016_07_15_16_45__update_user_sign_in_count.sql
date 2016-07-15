begin;
SET @temp_count := 1;
SET @temp_login := '';
SET @temp_time := '';
SET @temp_count1 := 1;
SET @temp_login1 := '';
SET @temp_time1 := '';
UPDATE `user`
  u
  JOIN
    (SELECT
      temp1.login_name,
      temp1.sign_count
    FROM
      (SELECT
        t.id,
        CASE
          WHEN @temp_login = t.login_name
          AND DATE(
            DATE_SUB(t.created_time, INTERVAL 1 DAY)
          ) = DATE(@temp_time)
          THEN @temp_count := @temp_count + 1
          WHEN @temp_login = t.login_name
          AND DATE(t.created_time) = DATE(@temp_time)
          THEN @temp_count
          ELSE @temp_count := 1
        END AS sign_count,
        @temp_login := t.login_name AS login_name,
        @temp_time := t.created_time AS create_time
      FROM
        point_bill t
      WHERE t.`business_type` = 'SIGN_IN'
      ORDER BY t.`login_name`,
        t.`created_time`) temp1
      JOIN
        (SELECT
          temp.login_name,
          MAX(temp.id) AS id
        FROM
          (SELECT
            t.id,
            CASE
              WHEN @temp_login1 = t.login_name
              AND DATE(
                DATE_SUB(t.created_time, INTERVAL 1 DAY)
              ) = DATE(@temp_time1)
              THEN @temp_count1 := @temp_count1 + 1
              WHEN @temp_login1 = t.login_name
              AND DATE(t.created_time) = DATE(@temp_time1)
              THEN @temp_count1
              ELSE @temp_count1 := 1
            END AS sign_count,
            @temp_login1 := t.login_name AS login_name,
            @temp_time1 := t.created_time AS create_time
          FROM
            point_bill t
          WHERE t.`business_type` = 'SIGN_IN'
          ORDER BY t.`login_name`,
            t.`created_time`) temp
        GROUP BY temp.login_name) temp2
        ON temp1.login_name = temp2.login_name
        AND temp1.id = temp2.id) temp4
    ON u.`login_name` = temp4.login_name SET u.`sign_in_count` = temp4.sign_count;

  COMMIT ;