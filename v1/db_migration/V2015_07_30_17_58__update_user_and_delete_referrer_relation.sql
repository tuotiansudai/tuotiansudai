BEGIN ;
    UPDATE
    `user` t
    JOIN
    (SELECT
       m.`user_id`
     FROM
       user_role m
     WHERE m.`role_id` = 'ROLE_MERCHANDISER') temp
      ON t.`id` = temp.`user_id` SET t.`referrer` = NULL WHERE t.`referrer` IS NOT NULL;

    DELETE
    FROM
      referrer_relation
    WHERE EXISTS
    (SELECT
      '1'
    FROM
      user_role m
    WHERE m.`role_id` = 'ROLE_MERCHANDISER'
          AND referrer_relation.`user_id` = m.`user_id`) ;
COMMIT ;