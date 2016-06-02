BEGIN;
  insert into `aa`.`membership` value(1, 0, 0, '0.1');
  insert into `aa`.`membership` value(2, 1, 5000, '0.1');
  insert into `aa`.`membership` value(3, 2, 50000, '0.09');
  insert into `aa`.`membership` value(4, 3, 300000, '0.08');
  insert into `aa`.`membership` value(5, 4, 1500000, '0.08');
  insert into `aa`.`membership` value(6, 5, 5000000, '0.07');
COMMIT;
