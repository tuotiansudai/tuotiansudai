BEGIN;

ALTER TABLE `aa`.`loan`
ADD COLUMN `pledge_type` VARCHAR(30) NOT NULL
AFTER `description_html`;

UPDATE `aa`.`loan`
SET pledge_type = 'NONE'
WHERE product_type = 'EXPERIENCE';

COMMIT;
