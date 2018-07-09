ALTER TABLE `aa`.`loan`
  ADD COLUMN `fund_platform` VARCHAR(20);
update loan set fund_platform='UMP' where fund_platform is null;

