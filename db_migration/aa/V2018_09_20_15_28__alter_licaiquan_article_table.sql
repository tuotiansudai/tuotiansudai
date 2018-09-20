ALTER TABLE `licaiquan_article` ADD COLUMN `sub_section` VARCHAR(30) AFTER `section`;
UPDATE `licaiquan_article`      SET `sub_section`='BASIC_KNOWLEDGE'     WHERE `section`='KNOWLEDGE';
