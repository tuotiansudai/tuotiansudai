ALTER TABLE invest ADD COLUMN trading_time DATETIME
AFTER created_time;

ALTER TABLE invest ADD COLUMN achievements VARCHAR(100)
AFTER trading_time;