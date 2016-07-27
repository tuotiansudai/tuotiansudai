SET @telfirst := '134 135 136 137 138 139 150 151 152 157 158 159 130 131 132 155 156 133 153 186 188';

BEGIN ;

UPDATE
  user_point_prize m
  JOIN
    (SELECT
      t.*,
      CONCAT(
        TRIM(
          SUBSTRING(@telfirst, 1+ FLOOR(RAND() * 21) * 4, 3)
        ),
        '****',
        FLOOR(RAND() * 10),
        FLOOR(RAND() * 10),
        FLOOR(RAND() * 10),
        FLOOR(RAND() * 10)
      ) AS mobile
    FROM
      user_point_prize t
    WHERE t.`reality` IS FALSE) temp
    ON m.`point_prize_id` = temp.point_prize_id
    AND m.`login_name` = temp.login_name
    AND m.`reality` IS FALSE SET m.`login_name` = temp.mobile ;

COMMIT ;