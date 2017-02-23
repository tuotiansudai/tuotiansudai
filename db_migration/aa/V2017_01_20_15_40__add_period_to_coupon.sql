ALTER TABLE coupon
  ADD COLUMN `period` INT DEFAULT NULL
  AFTER birthday_benefit;