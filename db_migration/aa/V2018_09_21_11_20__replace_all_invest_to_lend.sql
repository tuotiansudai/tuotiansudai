update experience_bill set note = replace(note, '投资', '出借');
update membership_experience_bill set description = replace(description, '投资', '出借');
update system_bill set detail = replace(detail, '投资', '出借');
update coupon set coupon_source = replace(coupon_source, '投资', '出借');