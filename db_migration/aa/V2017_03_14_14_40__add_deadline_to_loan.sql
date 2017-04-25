ALTER TABLE loan
  ADD COLUMN `deadline` DATETIME
  AFTER `fundraising_end_time`;

ALTER TABLE loan
  ADD COLUMN `original_duration` INT UNSIGNED
  AFTER `periods`;