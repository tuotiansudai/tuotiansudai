ALTER TABLE coupon ADD column updated_by VARCHAR(25) AFTER activated_time;
ALTER TABLE coupon ADD column updated_time DATETIME AFTER operated_by;
ALTER TABLE coupon ADD column deleted BOOLEAN DEFAULT FALSE ;