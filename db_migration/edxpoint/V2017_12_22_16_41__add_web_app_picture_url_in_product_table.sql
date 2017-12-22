ALTER TABLE `product` ADD COLUMN `web_picture_url` VARCHAR(200) DEFAULT NULL AFTER `total_count`;
ALTER TABLE `product` ADD COLUMN `app_picture_url` VARCHAR(200) DEFAULT NULL AFTER `total_count`;