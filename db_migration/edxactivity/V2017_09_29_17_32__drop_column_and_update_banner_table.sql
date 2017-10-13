begin;
UPDATE `edxactivity`.`banner` SET deactivated_time = '2017-10-15 23:59:59' where id=186;
UPDATE `edxactivity`.`banner` SET deactivated_time = '2025-12-31 23:59:59' where id in(194,198,199,200,204,206);
commit;

ALTER TABLE `edxactivity`.`banner`
DROP COLUMN `active`;
