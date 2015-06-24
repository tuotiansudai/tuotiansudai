BEGIN;
UPDATE
  `user` a
  JOIN
    (SELECT
      t.`id`,
      m.`username`
    FROM
      `user` t
      JOIN user_history m
        ON t.`login_name` = m.`referrer`
        AND m.`referrer` IS NOT NULL
        AND m.`referrer` != '') b
    ON a.`login_name` = b.`username` SET a.`referrer` = b.`id` ;
COMMIT;