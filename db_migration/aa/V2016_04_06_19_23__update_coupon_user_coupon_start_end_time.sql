alter table coupon drop column deadline;
alter table coupon drop column invest_upper_limit;

BEGIN ;

UPDATE
  coupon c
  JOIN
    (SELECT
      t.`id`,
      MIN(u.`start_time`) AS start_time,
      MAX(u.`end_time`) AS end_time
    FROM
      coupon t
      JOIN user_coupon u
        ON t.`id` = u.`coupon_id`
    WHERE t.`start_time` IS NULL
      AND t.`end_time` IS NULL
    GROUP BY t.`id`) temp
    ON c.`id` = temp.id SET c.`start_time` = temp.start_time,
  c.`end_time` = temp.end_time ;

UPDATE
    user_coupon t
    JOIN
    (SELECT
       uc.`coupon_id`,
       MAX(uc.`end_time`) AS end_time
     FROM
       user_coupon uc
     WHERE uc.`status` != 'SUCCESS'
           OR uc.`status` IS NULL
     GROUP BY uc.`coupon_id`) temp
      ON t.`coupon_id` = temp.coupon_id SET t.`end_time` = temp.end_time
WHERE t.`status` != 'SUCCESS'
      OR t.`status` IS NULL ;

COMMIT ;