ALTER TABLE `aa`.`user_membership`
ADD COLUMN `membership_give_id` BIGINT(20) UNSIGNED NULL
AFTER `type`,
ADD INDEX `FK_USER_MEMB_GIVE_ID_REF_MEMB_GIVE_ID_idx` (`membership_give_id` ASC);
ALTER TABLE `aa`.`user_membership`
ADD CONSTRAINT `FK_USER_MEMB_GIVE_ID_REF_MEMB_GIVE_ID`
FOREIGN KEY (`membership_give_id`)
REFERENCES `aa`.`membership_give` (`id`);