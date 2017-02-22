ALTER TABLE `product_order` ADD COLUMN `actual_points` INT UNSIGNED AFTER `points`;

BEGIN;
UPDATE `product_order` SET `actual_points` = `points` * `num`;
COMMIT;