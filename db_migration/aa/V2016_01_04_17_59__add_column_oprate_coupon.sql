ALTER TABLE coupon ADD column update_by VARCHAR(25) AFTER activated_time;
ALTER TABLE coupon ADD column update_time DATETIME AFTER operated_by;
ALTER TABLE coupon ADD column deleted BOOLEAN DEFAULT FALSE ;