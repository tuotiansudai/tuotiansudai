BEGIN;
UPDATE
  `aa`.`loan`
SET
  product_type =
  CASE
  WHEN periods = 1
    THEN 'SYL'
  WHEN periods = 3
    THEN 'WYX'
  WHEN periods = 6
    THEN 'JYF'
  ELSE NULL
  END;

COMMIT;