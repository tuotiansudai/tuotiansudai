BEGIN;
-- BEGIN MIGRATE HOUSE DETAILS
UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150608000013;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150608000013, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150608000013);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150608000013, 'machenliang', '马晨亮', 'MALE', 34, '110105198207051155', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150608000013);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150608000013, '北京', '', '50000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150608000013);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150610000014;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150610000014, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150610000014);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150610000014, 'machenliang', '马晨亮', 'MALE', 34, '110105198207051155', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150610000014);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150610000014, '北京', '', '180000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150610000014);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150630000016;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150630000016, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150630000016);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150630000016, 'machenliang', '马晨亮', 'MALE', 34, '110105198207051155', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150630000016);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150630000016, '北京', '', '50000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150630000016);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150702000017;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150702000017, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150702000017);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150702000017, 'machenliang', '马晨亮', 'MALE', 34, '110105198207051155', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150702000017);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150702000017, '北京', '', '50000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150702000017);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150703000018;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150703000018, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000018);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150703000018, 'songran', '宋冉', 'MALE', 32, '110108198403204218', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000018);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150703000018, '北京', '', '50000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000018);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150703000019;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150703000019, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000019);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150703000019, 'songran', '宋冉', 'MALE', 32, '110108198403204218', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000019);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150703000019, '北京', '', '50000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000019);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150703000020;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150703000020, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000020);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150703000020, 'songran', '宋冉', 'MALE', 32, '110108198403204218', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000020);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150703000020, '北京', '', '50000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000020);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150703000021;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150703000021, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000021);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150703000021, 'songran', '宋冉', 'MALE', 32, '110108198403204218', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000021);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150703000021, '北京', '', '50000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000021);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150703000022;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150703000022, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000022);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150703000022, 'machenliang', '马晨亮', 'MALE', 34, '110105198207051155', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000022);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150703000022, '北京', '', '50000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150703000022);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150704000023;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150704000023, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150704000023);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150704000023, 'machenliang', '马晨亮', 'MALE', 34, '110105198207051155', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150704000023);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150704000023, '北京', '', '50000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150704000023);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150704000024;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150704000024, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150704000024);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150704000024, 'machenliang', '马晨亮', 'MALE', 34, '110105198207051155', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150704000024);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150704000024, '北京', '', '50000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150704000024);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150704000025;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150704000025, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150704000025);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150704000025, 'machenliang', '马晨亮', 'MALE', 34, '110105198207051155', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150704000025);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150704000025, '北京', '', '100000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150704000025);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150704000026;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150704000026, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150704000026);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150704000026, 'machenliang', '马晨亮', 'MALE', 34, '110105198207051155', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150704000026);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150704000026, '北京', '', '100000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150704000026);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150805000043;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150805000043, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150805000043);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150805000043, 'wangdacheng', '王大成', 'MALE', 59, '110104195703011611', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150805000043);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150805000043, '北京', '', '700000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150805000043);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150807000045;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150807000045, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150807000045);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150807000045, 'banhuiying', '班慧颖', 'FEMALE', 31, '152525198409140064', 'NONE', '内蒙古自治区', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150807000045);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150807000045, '北京', '', '1100000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150807000045);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150809000046;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150809000046, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150809000046);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150809000046, 'luxueliang01', '刘雪亮', 'MALE', 33, '110226198304230013', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150809000046);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150809000046, '北京', '', '500000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150809000046);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150814000048;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150814000048, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150814000048);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150814000048, 'liujiming', '刘继明', 'MALE', 35, '110224198012260015', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150814000048);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150814000048, '北京', '', '900000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150814000048);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150819000053;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150819000053, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150819000053);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150819000053, 'jinwei', '金伟', 'MALE', 48, '110105196805271511', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150819000053);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150819000053, '北京', '', '1050000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150819000053);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150820000055;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150820000055, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150820000055);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150820000055, 'yanglihua01', '杨丽华', 'FEMALE', 45, '110105197101016525', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150820000055);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150820000055, '北京', '', '1000000', '', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150820000055);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150824000056;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150824000056, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150824000056);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150824000056, 'uyanxi', '鲁宴希', 'MALE', 54, '110106196202100090', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150824000056);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150824000056, '北京', '3000000', '1200000', '101.7平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150824000056);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150825000057;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150825000057, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150825000057);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150825000057, 'wangchunxiang', '王春祥', 'MALE', 72, '110104194310073016', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150825000057);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150825000057, '北京', '1700000', '800000', '68.64平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150825000057);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150826000058;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150826000058, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150826000058);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150826000058, 'tanxiuying', '谭秀英', 'FEMALE', 64, '110225195204171028', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150826000058);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150826000058, '北京', '800000', '450000', '72.56平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150826000058);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150827000059;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150827000059, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150827000059);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150827000059, 'wanghongli', '王红莉', 'FEMALE', 41, '410305197408315345', 'NONE', '河南省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150827000059);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150827000059, '北京', '1700000', '1000000', '43.86平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150827000059);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150828000060;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150828000060, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150828000060);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150828000060, 'wangjing01', '王静', 'FEMALE', 35, '370125198012175346', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150828000060);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150828000060, '北京', '1900000', '1400000', '85.53平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150828000060);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150831000061;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150831000061, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150831000061);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150831000061, 'nieyajuan', '聂亚娟', 'FEMALE', 50, '232101196511030847', 'NONE', '黑龙江省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150831000061);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150831000061, '北京', '1200000', '700000', '74.94平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150831000061);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150902000062;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150902000062, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150902000062);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150902000062, 'bogedan', '博格丹', 'MALE', 32, '110108198308136050', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150902000062);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150902000062, '北京', '2000000', '1200000', '39.34平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150902000062);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150908000063;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150908000063, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150908000063);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150908000063, 'doulaibao', '窦来宝', 'MALE', 58, '110105195802144512', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150908000063);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150908000063, '北京', '1600000', '1000000', '47.02平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150908000063);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150914000064;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150914000064, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150914000064);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150914000064, 'liuaihua01', '刘爱花', 'FEMALE', 58, '110107195805030628', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150914000064);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150914000064, '北京', '2100000', '500000', '80.01平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150914000064);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150916000065;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150916000065, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150916000065);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150916000065, 'zhaorui', '赵蕊', 'FEMALE', 35, '110106198008010023', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150916000065);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150916000065, '北京', '3900000', '1600000', '142.60平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150916000065);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150922000066;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150922000066, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150922000066);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150922000066, 'zhangying01', '张英', 'MALE', 63, '110222195211113310', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150922000066);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150922000066, '北京', '1900000', '1100000', '93.23平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150922000066);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150925000067;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150925000067, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150925000067);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150925000067, 'wangchanghai', '王长海', 'MALE', 52, '110106196405243972', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150925000067);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150925000067, '北京', '1450000', '800000', '50.15平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150925000067);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150930000068;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150930000068, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150930000068);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150930000068, 'mawenli', '马文利', 'FEMALE', 47, '11010819680819344X', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150930000068);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150930000068, '北京', '4300000', '3500000', '106.77平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150930000068);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151010000069;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151010000069, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151010000069);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151010000069, 'yuzhihuan', '余志欢', 'FEMALE', 38, '132903197805129428', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151010000069);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151010000069, '北京', '1200000', '780000', '65.53平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151010000069);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151015000070;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151015000070, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151015000070);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151015000070, 'wangchunrong', '王春荣', 'FEMALE', 59, '110108195707072222', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151015000070);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151015000070, '北京', '2500000', '1800000', '61.06平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151015000070);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151021000071;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151021000071, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151021000071);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151021000071, 'qiaoyongge', '乔永阁', 'MALE', 37, '110109197904104612', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151021000071);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151021000071, '北京', '2800000', '1200000', '119.22平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151021000071);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151023000072;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151023000072, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151023000072);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151023000072, 'chenweiyan', '陈巍岩', 'MALE', 46, '110103196910131518', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151023000072);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151023000072, '北京', '3000000', '1200000', '82.89平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151023000072);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151026000073;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151026000073, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151026000073);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151026000073, 'suxinjun', '苏鑫俊', 'MALE', 29, '130726198608070013', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151026000073);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151026000073, '北京', '1400000', '600000', '53.74平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151026000073);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151028000074;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151028000074, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151028000074);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151028000074, 'lihongtao', '李洪涛', 'MALE', 52, '110105196310256512', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151028000074);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151028000074, '北京', '1700000', '900000', '55.96平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151028000074);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151029000075;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151029000075, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151029000075);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151029000075, 'shenbaozhen', '沈宝珍', 'FEMALE', 52, '352231196403120045', 'NONE', '福建省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151029000075);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151029000075, '北京', '1100000', '700000', '26.29平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151029000075);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151105000076;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151105000076, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151105000076);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151105000076, 'wujunjiao', '吴俊姣', 'FEMALE', 34, '210423198207283629', 'NONE', '辽宁省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151105000076);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151105000076, '北京', '1500000', '900000', '47.67平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151105000076);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151110000077;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151110000077, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151110000077);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151110000077, 'yangjinlong', '杨金龙', 'MALE', 33, '11022119821202005X', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151110000077);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151110000077, '北京', '1700000', '800000', '82.52平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151110000077);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151111000078;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151111000078, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151111000078);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151111000078, 'zhangjitao', '张纪涛', 'MALE', 46, '370727197002163078', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151111000078);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151111000078, '北京', '1200000', '500000', '71.81平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151111000078);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151112000079;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151112000079, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151112000079);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151112000079, 'yangdongyu', '杨冬宇', 'FEMALE', 46, '110104196910130861', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151112000079);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151112000079, '北京', '3100000', '1750000', '56.8平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151112000079);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151117000080;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151117000080, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151117000080);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151117000080, 'liuyuling', '刘玉玲', 'FEMALE', 55, '110105196105144124', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151117000080);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151117000080, '北京', '1500000', '500000', '49.08平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151117000080);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151117000081;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151117000081, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151117000081);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151117000081, 'liguilan', '李桂兰', 'FEMALE', 39, '110106197703155721', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151117000081);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151117000081, '北京', '1100000', '600000', '88.93平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151117000081);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151119000082;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151119000082, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151119000082);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151119000082, 'zanghongyan', '藏红雁', 'FEMALE', 46, '110104197004280440', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151119000082);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151119000082, '北京', '2400000', '1400000', '72.80平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151119000082);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151120000083;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151120000083, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151120000083);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151120000083, 'zhaoguoqing', '赵国庆', 'MALE', 38, '372431197708154413', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151120000083);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151120000083, '北京', '1800000', '500000', '84.57平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151120000083);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151123000084;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151123000084, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151123000084);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151123000084, 'sunjianli', '孙建立', 'MALE', 50, '110108196604182757', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151123000084);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151123000084, '北京', '1600000', '300000', '64.03平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151123000084);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151125000085;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151125000085, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151125000085);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151125000085, 'zhangaijing', '张爱晶', 'MALE', 47, '110108196810112274', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151125000085);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151125000085, '北京', '1900000', '700000', '42.70平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151125000085);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151126000086;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151126000086, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151126000086);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151126000086, 'zhaoheping', '赵和平', 'FEMALE', 59, '110223195705290022', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151126000086);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151126000086, '北京', '2600000', '1200000', '113.41平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151126000086);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151127000087;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151127000087, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151127000087);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151127000087, 'zhaoguangxue', '赵光学', 'MALE', 57, '132442195902117611', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151127000087);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151127000087, '北京', '4100000', '2000000', '169.57平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151127000087);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151204000088;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151204000088, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151204000088);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151204000088, 'yangshujun01', '杨树军', 'MALE', 53, '110105196211204813', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151204000088);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151204000088, '北京', '3350000', '1400000', '118.77平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151204000088);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151208000089;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151208000089, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151208000089);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151208000089, 'shenshuhong', '沈树红', 'FEMALE', 45, '110221197009195343', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151208000089);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151208000089, '北京', '2000000', '600000', '99.05平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151208000089);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151209000090;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151209000090, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151209000090);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151209000090, 'gaopeifu', '高佩福', 'MALE', 76, '110101193911291533', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151209000090);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151209000090, '北京', '1800000', '300000', '69平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151209000090);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151209000091;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151209000091, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151209000091);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151209000091, 'jintieliang', '金铁亮', 'MALE', 54, '110108196206114935', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151209000091);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151209000091, '北京', '3600000', '1700000', '58.40平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151209000091);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151216000092;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151216000092, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151216000092);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151216000092, 'hanhui', '韩晖', 'MALE', 42, '410603197407211011', 'NONE', '河南省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151216000092);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151216000092, '北京', '2800000', '900000', '154.50平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151216000092);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20151217000093;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20151217000093, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151217000093);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20151217000093, 'licheng', '李程', 'MALE', 35, '110101198102094516', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151217000093);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20151217000093, '北京', '2900000', '1900000', '64.54平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20151217000093);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 22600165059552;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 22600165059552, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 22600165059552);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 22600165059552, 'xibin01', '席斌', 'MALE', 38, '110102197710071133', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 22600165059552);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 22600165059552, '北京', '2000000', '1200000', '59.81平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 22600165059552);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 23240187038416;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 23240187038416, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 23240187038416);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 23240187038416, 'liran01', '李然', 'FEMALE', 43, '130322197307070026', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 23240187038416);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 23240187038416, '北京', '3000000', '1100000', '150.35平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 23240187038416);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 23864826932704;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 23864826932704, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 23864826932704);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 23864826932704, 'xuzengjiang', '徐增江', 'MALE', 40, '110224197604100010', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 23864826932704);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 23864826932704, '北京', '2500000', '1100000', '90.65平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 23864826932704);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 24478357146640;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 24478357146640, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 24478357146640);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 24478357146640, 'xuanlingrong', '宣苓荣', 'FEMALE', 56, '370822195912133243', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 24478357146640);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 24478357146640, '山东', '500000', '220000', '102.02平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 24478357146640);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 25007441321408;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 25007441321408, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 25007441321408);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 25007441321408, 'zhaijianjun', '翟建军', 'MALE', 47, '130684196811253332', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 25007441321408);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 25007441321408, '北京', '4200000', '1850000', '182.06平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 25007441321408);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 26164380646400;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 26164380646400, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 26164380646400);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 26164380646400, 'zhangdaochang', '张道常', 'MALE', 44, '372522197206194617', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 26164380646400);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 26164380646400, '山东', '300000', '110000', '42.92平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 26164380646400);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 26165070781440;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 26165070781440, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 26165070781440);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 26165070781440, 'yaoyunbo', '姚运波', 'MALE', 47, '370823196812175877', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 26165070781440);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 26165070781440, '山东', '500000', '350000', '64.53平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 26165070781440);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 26327124588688;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 26327124588688, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 26327124588688);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 26327124588688, 'zhaixinchun', '翟新春', 'MALE', 44, '370802197201012115', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 26327124588688);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 26327124588688, '山东', '500000', '290000', '72.01平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 26327124588688);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 26328228277392;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 26328228277392, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 26328228277392);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 26328228277392, 'tangchunlei', '唐春雷', 'MALE', 26, '370811199001103018', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 26328228277392);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 26328228277392, '山东', '500000', '350000', '94.53平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 26328228277392);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 27555306596864;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 27555306596864, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 27555306596864);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 27555306596864, 'yaoyunbo', '姚运波', 'MALE', 47, '370823196812175877', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 27555306596864);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 27555306596864, '山东', '1000000', '700000', '64.53平方米、64.53平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 27555306596864);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 27578838421008;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 27578838421008, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 27578838421008);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 27578838421008, 'jiangyongtao', '姜永涛', 'MALE', 38, '370811197801100836', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 27578838421008);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 27578838421008, '山东', '1000000', '600000', '136.24平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 27578838421008);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 28104640080624;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 28104640080624, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 28104640080624);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 28104640080624, 'changhongbing', '常红兵', 'MALE', 42, '110111197312160313', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 28104640080624);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 28104640080624, '北京', '1700000', '600000', '87.11平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 28104640080624);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 28643614667104;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 28643614667104, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 28643614667104);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 28643614667104, 'yuanshuyun', '袁淑云', 'FEMALE', 39, '370822197707260824', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 28643614667104);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 28643614667104, '山东', '334000', '170000', '83.97平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 28643614667104);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 28644515293536;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 28644515293536, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 28644515293536);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 28644515293536, 'liteng', '李腾', 'MALE', 28, '370811198805155038', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 28644515293536);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 28644515293536, '山东', '1000000', '600000', '83.41平方米、83.41平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 28644515293536);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 30051311678464;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 30051311678464, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 30051311678464);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 30051311678464, 'hehai', '何海', 'MALE', 29, '370830198706154717', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 30051311678464);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 30051311678464, '山东', '340000', '100000', '85.87平方米', '编号20006398-2', '编号160674', '济诚信证经字第192号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 30051311678464);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 30259442598912;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 30259442598912, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 30259442598912);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 30259442598912, 'renxudong', '何旭东', 'MALE', 57, '342623195906230014', 'MARRIED', '安徽省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 30259442598912);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 30259442598912, '北京', '6600000', '3800000', '157.39平方米', '编号04347427', '编号02249748', '京中信内民证字13603号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 30259442598912);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 30494782250672;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 30494782250672, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 30494782250672);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 30494782250672, 'shiyuefeng', '史月凤', 'FEMALE', 48, '370802196709020023', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 30494782250672);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 30494782250672, '山东', '400000', '150000', '58.14平方米', '编号37272', '编号2016002585', '济诚信证经字第197号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 30494782250672);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 31183385238128;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 31183385238128, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31183385238128);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 31183385238128, 'yanmeng', '闫猛', 'MALE', 35, '370883198010080919', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31183385238128);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 31183385238128, '山东', '170000', '70000', '74.69平方米', '编号201000047', '编号2016002865', '济诚信证经字第234号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31183385238128);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 31204246249072;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 31204246249072, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31204246249072);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 31204246249072, 'lideyuan', '李德元', 'MALE', 61, '110109195503224016', 'MARRIED', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31204246249072);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 31204246249072, '北京', '1650000', '700000', '82.53平方米', '编号080989', '编号0012715', '京中信内民证字第107235号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31204246249072);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 31819850060304;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 31819850060304, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31819850060304);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 31819850060304, 'wangyongli', '王永利', 'FEMALE', 40, '370811197604060521', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31819850060304);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 31819850060304, '山东', '500000', '200000', '111.38平方米', '编号2015004226', '编号016003179', '济诚信证经字第284号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31819850060304);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 32264815568304;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 32264815568304, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 32264815568304);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 32264815568304, 'zhouchangqing', '周长清', 'MALE', 43, '370826197303016812', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 32264815568304);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 32264815568304, '山东', '400000', '230000', '77.63平方米', '编号2008008902', '编号2016003381', '济诚信证经字第326号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 32264815568304);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 32877300321760;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 32877300321760, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 32877300321760);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 32877300321760, 'maxiangju', '马祥菊', 'FEMALE', 40, '370822197607192422', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 32877300321760);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 32877300321760, '山东', '320000', '150000', '86.42平方米', '编号201103115', '编号20160559', '济诚信证经字第369号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 32877300321760);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 33064893975664;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 33064893975664, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33064893975664);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 33064893975664, 'zhaixuehong', '翟雪洪', 'FEMALE', 41, '370825197412105527', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33064893975664);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 33064893975664, '山东', '460000', '180000', '86平方米', '编号00095635', '编号00205628', '济诚信证经字第381号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33064893975664);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 33154684581136;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 33154684581136, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33154684581136);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 33154684581136, 'liuwufeng', '刘武丰', 'MALE', 46, '110106196911210030', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33154684581136);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 33154684581136, '北京', '2800000', '300000', '83.67平方米', '编号092074', '编号188557', '京中信内证字91041号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33154684581136);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 33226752436656;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 33226752436656, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33226752436656);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 33226752436656, 'songdianfang', '宋殿芳', 'FEMALE', 50, '110224196604090022', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33226752436656);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 33226752436656, '北京', '2300000', '400000', '69.77平方米', '编号072188', '编号0015419', '京中信内证字108089号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33226752436656);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 33479234568256;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 33479234568256, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33479234568256);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 33479234568256, 'wanglin', '王林', 'MALE', 36, '120101197910253017', 'NONE', '天津市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33479234568256);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 33479234568256, '北京', '2500000', '600000', '70.61平方米', '编号1476719', '编号545532', '京中信内证字65614号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33479234568256);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 33484048683072;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 33484048683072, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33484048683072);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 33484048683072, 'renjiangwei', '任江伟', 'MALE', 39, '411222197707152517', 'MARRIED', '河南省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33484048683072);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 33484048683072, '北京', '2000000', '400000', '57.62平方米', '编号759467', '编号548557', '京中信内证字70251号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33484048683072);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 33748144266112;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 33748144266112, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33748144266112);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 33748144266112, 'liuwengang', '刘文刚', 'FEMALE', 31, '110105198505043000', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33748144266112);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 33748144266112, '北京', '1950000', '400000', '62.84平方米', '编号1023174', '编号0026888', '京中信内经证字17670号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33748144266112);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 33859803589504;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 33859803589504, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33859803589504);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 33859803589504, 'weihesheng', '魏河胜', 'MALE', 52, '37082219640320321X', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33859803589504);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 33859803589504, '山东', '360000', '150000', '87.61平方米', '编号000257', '编号20160628', '济诚信证经字第425号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33859803589504);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 35531159660832;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 35531159660832, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35531159660832);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 35531159660832, 'suncheng', '孙成', 'MALE', 34, '370883198111020413', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35531159660832);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 35531159660832, '山东', '300000', '100000', '63.14平方米', '编号01108029', '编号161270', '济诚信证经字第449号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35531159660832);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 35531620086048;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 35531620086048, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35531620086048);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 35531620086048, 'haoyingchun', '郝迎春', 'FEMALE', 43, '370828197302280323', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35531620086048);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 35531620086048, '山东', '450000', '200000', '70.54平方米', '编号2013008128', '编号016004943', '济诚信证经字第493号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35531620086048);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 36155581416592;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 36155581416592, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 36155581416592);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 36155581416592, 'xueziya', '薛子亚', 'FEMALE', 25, '110224199009202847', 'MARRIED', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 36155581416592);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 36155581416592, '北京', '1220000', '630000', '111.52平方米', '编号144257', '编号070393', '京中信内民证字第90495号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 36155581416592);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 36255287314528;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 36255287314528, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 36255287314528);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 36255287314528, 'tanchaohong', '覃超宏', 'FEMALE', 50, '370828196606012323', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 36255287314528);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 36255287314528, '山东', '250000', '170000', '114.58平方米', '编号00017861', '编号00017577', '济诚信证经字第547号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 36255287314528);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 36309774563424;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 36309774563424, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 36309774563424);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 36309774563424, 'liqin', '李钦', 'MALE', 31, '370831198503010752', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 36309774563424);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 36309774563424, '山东', '1000000', '360000', '153.71平方米', '编号2016006521', '编号2016004764', '济诚信证经字第447号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 36309774563424);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 37217446260560;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 37217446260560, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 37217446260560);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 37217446260560, 'zhuxiuzhi', '朱秀芝', 'FEMALE', 38, '370302197801160325', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 37217446260560);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 37217446260560, '北京', '4600000', '900000', '127.44平方米', '编号0704316', '编号0017421', '京中信内经证字第24733号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 37217446260560);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 37836882713216;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 37836882713216, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 37836882713216);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 37836882713216, 'liyuqiu', '李玉球', 'MALE', 39, '370825197703295535', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 37836882713216);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 37836882713216, '山东', '150000', '70000', '59.17平方米', '编号20006290', '编号161475', '济诚信证经字第567号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 37836882713216);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 38105428496272;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 38105428496272, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38105428496272);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 38105428496272, 'wangshaohua', '王少华', 'MALE', 42, '411302197311276038', 'NONE', '河南省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38105428496272);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 38105428496272, '北京', '2400000', '1120000', '85.07平方米', '编号359440', '编号0018619', '京中信内经证字第20710号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38105428496272);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 38727329898032;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 38727329898032, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38727329898032);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 38727329898032, 'zhongxiongfei', '钟雄飞', 'MALE', 56, '110106196007040934', 'MARRIED', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38727329898032);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 38727329898032, '北京', '2100000', '1000000', '92.83平方米', '编号038828', '编号0016326', '京中信内经证字第22229号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38727329898032);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 38794651564592;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 38794651564592, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38794651564592);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 38794651564592, 'liliang', '李亮', 'MALE', 35, '370882198105070418', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38794651564592);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 38794651564592, '山东', '300000', '200000', '104.09平方米', '编号201106254', '编号20160912', '济诚信证经字第663号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38794651564592);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 39071524289312;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39071524289312, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39071524289312);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39071524289312, 'lujun', '路军', 'MALE', 36, '370882197911012015', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39071524289312);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 39071524289312, '山东', '300000', '230000', '119.62平方米', '编号201405271', '编号20160840', '济诚信证经字第606号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39071524289312);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 38793272489520;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 38793272489520, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38793272489520);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 38793272489520, 'shenhua', '沈华', 'MALE', 53, '370883196212127616', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38793272489520);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 38793272489520, '山东', '450000', '300000', '202.98平方米', '编号01101214', '编号161750', '济诚信证经字第669号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38793272489520);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 39436998318128;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39436998318128, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39436998318128);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39436998318128, 'wulin', '吴林', 'MALE', 28, '37080219871007241X', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39436998318128);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 39436998318128, '山东', '400000', '200000', '93.44平方米', '建房注册号37029', '编号00199319', '济诚信证民字第2522号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39436998318128);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 39437681526832;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39437681526832, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39437681526832);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39437681526832, 'haohaijun', '郝海军', 'MALE', 44, '370828197108060036', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39437681526832);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 39437681526832, '山东', '300000', '100000', '142.81平方米', '建房注册号37039', '编号0013071', '济诚信证经字第664号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39437681526832);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 39496724994256;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39496724994256, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39496724994256);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39496724994256, 'zhengzebao', '郑则宝', 'MALE', 23, '370830199210173513', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39496724994256);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 39496724994256, '山东', '400000', '240000', '140.45平方米', '编号00338318', '编号00203943', '济诚信证民字第2516号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39496724994256);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 39497131912400;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39497131912400, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39497131912400);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39497131912400, 'zhangyong', '张勇', 'MALE', 29, '370802198706022436', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39497131912400);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 39497131912400, '山东', '400000', '150000', '48.57平方米', '编号00274893', '编号00203746', '济诚信证经字第696号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39497131912400);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 39497605573840;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39497605573840, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39497605573840);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39497605573840, 'baimeng', '白萌', 'MALE', 33, '370802198210261812', 'MARRIED', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39497605573840);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 39497605573840, '山东', '600000', '90000', '59.8平方米', '编号00288074', '编号00201915', '济诚信证民字第1671号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39497605573840);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 39684256358608;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39684256358608, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39684256358608);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39684256358608, 'wangyan', '王晏', 'FEMALE', 25, '340802199104160224', 'NONE', '安徽省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39684256358608);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 39684256358608, '安徽', '450000', '220000', '91.85平方米', '编号00018087', '', '皖枞公证字第667号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39684256358608);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 39688100259024;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39688100259024, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39688100259024);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39688100259024, 'dusimei', '杜四妹', 'FEMALE', 44, '370105197109302524', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39688100259024);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 39688100259024, '山东', '350000', '190000', '43.70平方米', '编号37000279563', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39688100259024);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 39859616274144;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39859616274144, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39859616274144);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39859616274144, 'wangzonghai', '汪宗海', 'MALE', 50, '370811196602222035', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39859616274144);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 39859616274144, '山东', '700000', '450000', '315.66平方米', '编号00278867', '编号00199448', '济诚信证经字第717号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39859616274144);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 39860080765664;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39860080765664, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39860080765664);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39860080765664, 'majunqiu', '马俊秋', 'FEMALE', 38, '370802197801315128', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39860080765664);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 39860080765664, '山东', '200000', '120000', '40.15平方米', '编号00174300', '编号00204435', '济诚信证民字第731号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39860080765664);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 39936211260304;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39936211260304, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39936211260304);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39936211260304, 'zengfanrong', '曾凡荣', 'MALE', 50, '370829196603253232', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39936211260304);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 39936211260304, '山东', '450000', '220000', '135.94平方米', '第0001927号', '第001428号', '济诚信证经字第720号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39936211260304);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 39963386690640;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39963386690640, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39963386690640);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39963386690640, 'litongxin', '李峒昕', 'MALE', 28, '370830198803280010', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39963386690640);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 39963386690640, '山东', '380000', '150000', '94.25平方米', '编号0036468', '编号00013443', '济诚信证经字第492号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39963386690640);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 40404498510192;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 40404498510192, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 40404498510192);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 40404498510192, 'liying', '李莹', 'MALE', 28, '370828198708170000', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 40404498510192);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 40404498510192, '山东', '100000', '70000', '106.56平方米', '建房注册号37039', '编号00017469', '济诚信证经字第602号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 40404498510192);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 40058932676864;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 40058932676864, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 40058932676864);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 40058932676864, 'liyang', '李洋', 'MALE', 32, '130123198402136000', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 40058932676864);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 40058932676864, '河北省', '1500000', '900000', '144.53平方米', '第730011623号', '第033019903号', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 40058932676864);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 40653972481744;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 40653972481744, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 40653972481744);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 40653972481744, 'sunzengxiang', '孙增祥', 'MALE', 38, '110101197801122000', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 40653972481744);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 40653972481744, '北京市', '1300000', '6000000', '43.45平方米', '编号00680449', '第143925号', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 40653972481744);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 41026939926192;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 41026939926192, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41026939926192);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 41026939926192, 'suilong', '隋龙', 'MALE', 27, '370882198905171000', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41026939926192);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 41026939926192, '山东省', '500000', '260000', '120.64平方米', '建房注册号37031', '编号00197022', '济诚信证经字第758号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41026939926192);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 40996846038512;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 40996846038512, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 40996846038512);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 40996846038512, 'linshaohui', '林绍辉', 'MALE', 51, '132232196506222000', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 40996846038512);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 40996846038512, '河北省', '900000', '500000', '91.17平方米', '编号01091280', '第033020011号', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 40996846038512);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 41295092067952;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 41295092067952, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41295092067952);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 41295092067952, 'wangdong', '王董', 'MALE', 25, '370883199009110000', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41295092067952);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 41295092067952, '山东省', '300000', '150000', '84平方米', '建房注册号37032', '编号00031933', '济诚信证经字第754号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41295092067952);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 41107901194928;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 41107901194928, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41107901194928);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 41107901194928, 'yejiuqi', '叶九其', 'MALE', 36, '370828197909063000', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41107901194928);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 41107901194928, '山东省', '220000', '150000', '106.32平方米', '建房注册号37039', '编号00012896', '济诚信证经字第752号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41107901194928);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 41624561501648;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 41624561501648, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41624561501648);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 41624561501648, 'mabiao', '马彪', 'MALE', 33, '110105198212172000', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41624561501648);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 41624561501648, '北京市', '2100000', '1000000', '68.84平方米', '编号04744422', '第0012343号', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41624561501648);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 41818877884144;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 41818877884144, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41818877884144);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 41818877884144, 'hanjian', '韩健', 'MALE', 33, '110111198201258814', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41818877884144);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 41818877884144, '北京市', '1650000', '550000', '116.09平方米', '编号03686587', '第0013985号', '京中信内民证字第109808号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41818877884144);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 41294010146416;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 41294010146416, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41294010146416);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 41294010146416, 'liuquan', '刘全', 'MALE', 32, '130103198306020312', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41294010146416);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 41294010146416, '河北省', '1600000', '400000', '230.79平方米', '编号01144001', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41294010146416);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 38794014144048;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 38794014144048, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38794014144048);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 38794014144048, 'lujun', '路军', 'MALE', 37, '370882197911012015', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38794014144048);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 38794014144048, '山东省', '300000', '230000', '119.62平方米', '编号201405271', '编号20160840  ', '济诚信证经字第606号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38794014144048);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 32605526181184;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 32605526181184, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 32605526181184);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 32605526181184, 'renhaixiao', '任海啸', 'MALE', 32, '110111198412155217', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 32605526181184);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 32605526181184, '北京市', '2800000', '1100000', '109.94平方米', '编号0600372', '编号069270', '京中信内民证字76020号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 32605526181184);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150610000015;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150610000015, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150610000015);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150610000015, 'machenliang', '马晨亮', 'MALE', 34, '110105198207051155', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150610000015);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150610000015, '北京市', '1350000', '680000', '94平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150610000015);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 20150610000015;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150610000015, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150610000015);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150610000015, 'machenliang', '马晨亮', 'MALE', 34, '110105198207051155', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150610000015);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 20150610000015, '北京市', '1350000', '680000', '94平方米', '', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150610000015);

UPDATE `aa`.`loan` SET pledge_type = 'HOUSE' WHERE id = 41913485549520;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 41913485549520, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41913485549520);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 41913485549520, 'fuyang', '付杨', 'MALE', 35, '110109198108315210', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41913485549520);
INSERT INTO `aa`.`pledge_house` (loan_id, pledge_location, estimate_amount, loan_amount, square, property_card_id, estate_register_id, authentic_act) SELECT 41913485549520, '北京市', '1500000', '300000', '64.53平方米', '编号03432321', '编号01925258', '京中信内民证字第21650号' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 41913485549520);

-- BEGIN MIGRATE VEHICLE DETAILS
UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150717000027;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150717000027, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150717000027);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150717000027, 'pangyang', '庞洋', 'MALE', 34, '110101198201300012', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150717000027);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150717000027, '北京市', '', '60000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150717000027);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150720000028;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150720000028, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150720000028);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150720000028, 'guorui001', '郭瑞', 'MALE', 35, '131121198012100011', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150720000028);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150720000028, '北京市', '', '18000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150720000028);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150721000029;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150721000029, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150721000029);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150721000029, 'yujia001', '于佳', 'MALE', 35, '110103198103050331', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150721000029);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150721000029, '北京市', '', '27000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150721000029);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150722000030;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150722000030, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150722000030);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150722000030, 'liuqishun', '刘起顺', 'MALE', 71, '110105194501021514', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150722000030);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150722000030, '北京市', '', '50000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150722000030);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150727000031;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150727000031, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150727000031);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150727000031, 'mashuxu', '马淑旭', 'FEMALE', 27, '110111198902205221', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150727000031);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150727000031, '北京市', '', '80000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150727000031);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150727000032;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150727000032, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150727000032);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150727000032, 'dengzhe', '邓喆', 'MALE', 29, '110106198706293630', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150727000032);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150727000032, '北京市', '', '70000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150727000032);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150727000033;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150727000033, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150727000033);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150727000033, 'wangshuai', '王帅', 'FEMALE', 34, '110221198112035940', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150727000033);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150727000033, '北京市', '', '40000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150727000033);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150729000034;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150729000034, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150729000034);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150729000034, 'sunshuo', '孙硕', 'MALE', 27, '110223198812160016', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150729000034);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150729000034, '北京市', '', '120000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150729000034);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150729000035;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150729000035, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150729000035);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150729000035, 'wangguoqiang', '王国强', 'MALE', 28, '110223198806227878', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150729000035);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150729000035, '北京市', '', '40000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150729000035);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150729000036;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150729000036, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150729000036);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150729000036, 'xiabo', '夏博', 'MALE', 37, '110111197812021651', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150729000036);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150729000036, '北京市', '', '20000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150729000036);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150730000038;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150730000038, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000038);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150730000038, 'sunlili01', '孙立立', 'FEMALE', 31, '22080219850505544X', 'NONE', '吉林省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000038);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150730000038, '北京市', '', '80000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000038);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150730000040;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150730000040, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000040);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150730000040, 'xuebo', '薛波', 'MALE', 38, '610526197711194611', 'NONE', '陕西省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000040);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150730000040, '北京市', '', '60000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000040);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150730000041;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150730000041, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000041);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150730000041, 'zhangcheng01', '张程', 'MALE', 43, '110224197305170035', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000041);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150730000041, '北京市', '', '300000', '大众', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000041);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150731000042;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150731000042, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150731000042);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150731000042, 'dengxiaoqiang', '邓小强', 'MALE', 38, '130226197806054039', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150731000042);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150731000042, '北京市', '', '500000', '大众', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150731000042);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150812000047;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150812000047, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150812000047);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150812000047, 'zhangtiejun', '张铁军', 'MALE', 43, '220881197304215014', 'NONE', '吉林省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150812000047);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150812000047, '北京市', '140300', '48000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150812000047);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150814000049;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150814000049, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150814000049);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150814000049, 'yinhaiyong', '尹海勇', 'MALE', 37, '110108197809285412', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150814000049);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150814000049, '北京市', '', '120000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150814000049);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150814000050;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150814000050, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150814000050);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150814000050, 'yanghaizhong', '杨海忠', 'MALE', 47, '110222196902142916', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150814000050);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150814000050, '北京市', '', '60000', '北京现代', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150814000050);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150817000051;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150817000051, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150817000051);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150817000051, 'panruoxin', '潘若鑫', 'MALE', 50, '110105196509153633', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150817000051);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150817000051, '北京市', '238000', '116000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150817000051);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150817000052;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150817000052, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150817000052);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150817000052, 'duanlei', '段磊', 'MALE', 35, '110105198010281811', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150817000052);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150817000052, '北京市', '320000', '176000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150817000052);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150820000054;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150820000054, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150820000054);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150820000054, 'zhaojing01', '赵京', 'FEMALE', 22, '110108199310154922', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150820000054);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150820000054, '北京市', '', '150000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150820000054);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 22702147012736;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 22702147012736, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 22702147012736);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 22702147012736, 'liumaoliang', '刘茂亮', 'MALE', 29, '370830198608204717', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 22702147012736);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 22702147012736, '济宁市', '400000', '200000', '梅赛德斯-奔驰', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 22702147012736);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 23574347698064;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 23574347698064, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 23574347698064);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 23574347698064, 'linan001', '李喃', 'MALE', 31, '370811198503200032', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 23574347698064);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 23574347698064, '济宁市', '250000', '130000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 23574347698064);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 24560182700048;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 24560182700048, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 24560182700048);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 24560182700048, 'sunjianchun', '孙建春', 'FEMALE', 37, '370830197810165243', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 24560182700048);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 24560182700048, '济宁市', '350000', '200000', '奥迪', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 24560182700048);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 24561702486032;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 24561702486032, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 24561702486032);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 24561702486032, 'wanglaingliang', '王亮亮', 'MALE', 28, '370830198710013933', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 24561702486032);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 24561702486032, '山东省', '250000', '150000', '大众', 'LSVCH6A' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 24561702486032);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 24570181519376;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 24570181519376, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 24570181519376);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 24570181519376, 'luzeya', '路则亚', 'MALE', 24, '370830199205280015', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 24570181519376);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 24570181519376, '济南市', '750000', '400000', '梅赛德斯-奔驰', 'WDDNG8CB' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 24570181519376);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 25694155806528;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 25694155806528, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 25694155806528);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 25694155806528, 'shiyu', '石煜', 'MALE', 41, '370802197501294812', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 25694155806528);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 25694155806528, '济宁市', '750000', '430000', '梅赛德斯-奔驰', 'WDDNG8CB' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 25694155806528);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 29262603295456;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 29262603295456, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 29262603295456);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 29262603295456, 'zhaobingbing', '赵冰冰', 'MALE', 26, '370830198911163938', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 29262603295456);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 29262603295456, '山东省', '450000', '250000', '奥迪', '2013年 WAU9GD8T' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 29262603295456);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 29334315582336;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 29334315582336, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 29334315582336);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 29334315582336, 'zhaobingbing', '赵冰冰', 'MALE', 26, '370830198911163938', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 29334315582336);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 29334315582336, '山东省', '400000', '180000', '梅赛德斯-奔驰', 'BJ7182VXL' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 29334315582336);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 29335704587136;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 29335704587136, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 29335704587136);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 29335704587136, 'wangqinyin', '王钦印', 'MALE', 27, '371322198906280017', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 29335704587136);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 29335704587136, '山东省', '460000', '200000', '奥迪', 'WAUCGB8T' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 29335704587136);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 30497451731632;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 30497451731632, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 30497451731632);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 30497451731632, 'shanyong', '单勇', 'MALE', 37, '420620197902212538', 'NONE', '湖北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 30497451731632);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 30497451731632, '北京市', '650000（2辆车合计）', '500000（2辆车合计）', '帕萨特、雅阁', 'SVW7183SJD、HG7241AB' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 30497451731632);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 31199405149808;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 31199405149808, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31199405149808);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 31199405149808, 'houdongsheng', '侯东升', 'MALE', 30, '110108198602142718', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31199405149808);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 31199405149808, '北京市', '245900', '125000', '指南者', 'JEEP COMPASS' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31199405149808);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 31738238626864;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 31738238626864, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31738238626864);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 31738238626864, 'houdongsheng', '侯东升', 'MALE', 30, '110108198602142718', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31738238626864);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 31738238626864, '北京市', '1475400', '1000000', '指南者', 'JEEP COMPASS（共计6辆）' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31738238626864);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 31828346211856;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 31828346211856, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31828346211856);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 31828346211856, 'houdongsheng', '侯东升', 'MALE', 30, '110108198602142718', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31828346211856);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 31828346211856, '北京市', '1577300', '1000000', '指南者', 'JEEP COMPASS（共计7辆）' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31828346211856);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 31910596389712;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 31910596389712, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31910596389712);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 31910596389712, 'houdongsheng', '侯东升', 'MALE', 30, '110108198602142718', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31910596389712);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 31910596389712, '北京市', '1451400', '1000000', '指南者', 'JEEP COMPASS（共计6辆）' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 31910596389712);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 33660180696768;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 33660180696768, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33660180696768);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 33660180696768, 'jianyun', '简运', 'MALE', 44, '413024197204280035', 'NONE', '河南省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33660180696768);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 33660180696768, '北京市', '440000', '250000', '梅赛德斯-奔驰', 'BI6453A3F' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33660180696768);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 33662646298304;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 33662646298304, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33662646298304);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 33662646298304, 'jianghu', '江虎', 'MALE', 30, '340881198605302711', 'NONE', '安徽省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33662646298304);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 33662646298304, '北京市', '220000', '70000', '奥迪', 'FV7145LCDBG' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33662646298304);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 33663143023296;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 33663143023296, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33663143023296);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 33663143023296, 'chenhongfei', '陈宏飞', 'MALE', 23, '371423199210310034', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33663143023296);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 33663143023296, '河北省', '250000', '110000', '奥迪', 'FV7203TFCVTG' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33663143023296);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 33663942776512;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 33663942776512, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33663942776512);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 33663942776512, 'liuchanghui', '刘长会', 'MALE', 56, '110225195912244013', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33663942776512);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 33663942776512, '北京市', '200000', '100000', '思威', 'DHW6456B（CR-V 2.0）' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 33663942776512);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 35336607496320;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 35336607496320, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35336607496320);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 35336607496320, 'chenyuanqiao', '陈袁桥', 'MALE', 37, '340825197809220413', 'NONE', '安徽省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35336607496320);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 35336607496320, '天津市', '185000', '130000', '奥迪', 'FV7148BADBG' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35336607496320);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 35337795442816;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 35337795442816, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35337795442816);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 35337795442816, 'lizhanying', '李占营', 'MALE', 53, '132232196211096517', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35337795442816);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 35337795442816, '北京市', '2650000', '400000', '奥迪', 'WAUR4B4H' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35337795442816);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 35601490036480;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 35601490036480, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35601490036480);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 35601490036480, 'liushuang', '刘爽', 'MALE', 35, '110223198105110618', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35601490036480);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 35601490036480, '北京市', '450000', '270000', '丰田', 'SCT6482E5A' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35601490036480);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 35602309452544;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 35602309452544, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35602309452544);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 35602309452544, 'wangbing', '王兵', 'MALE', 42, '140104197309264519', 'NONE', '陕西省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35602309452544);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 35602309452544, '北京市', '400000', '160000', '奥迪', 'FV7201TFCVTG' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35602309452544);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 35602522193664;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 35602522193664, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35602522193664);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 35602522193664, 'guoyincai', '郭银才', 'MALE', 53, '410802196304051537', 'NONE', '河南省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35602522193664);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 35602522193664, '北京市', '120000', '70000', '宝骏', 'LZW7150AB5' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35602522193664);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 35954940133632;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 35954940133632, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35954940133632);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 35954940133632, 'wangcuimin', '王翠敏', 'FEMALE', 34, '110111198202221423', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35954940133632);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 35954940133632, '北京市', '500000', '200000', '奥迪', 'FV7251BBCWG' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 35954940133632);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 38550067570928;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 38550067570928, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38550067570928);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 38550067570928, 'durongjian', '杜荣建', 'MALE', 29, '131022198702234675', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38550067570928);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 38550067570928, '北京市', '95000', '56000', '北京现代', 'BH7161HMZ' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38550067570928);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 38551493002480;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 38551493002480, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38551493002480);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 38551493002480, 'lichong', '李冲', 'MALE', 26, '130984198912015718', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38551493002480);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 38551493002480, '河北省', '135000', '75000', '北京现代', 'BH7181PAZ' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38551493002480);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 38551802224880;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 38551802224880, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38551802224880);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 38551802224880, 'linxiaoyu', '林晓雨', 'MALE', 28, '110101198806015011', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38551802224880);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 38551802224880, '北京市', '200000', '95000', '东风日产', 'DFL7201AC' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 38551802224880);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 39345397442400;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39345397442400, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39345397442400);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39345397442400, 'yixiangqing', '伊祥青', 'MALE', 46, '370811197006200053', 'NONE', '山东省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39345397442400);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 39345397442400, '山东省', '190000', '120000', '大众', 'FA7207RBDWG' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39345397442400);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 39677243049168;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39677243049168, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39677243049168);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39677243049168, 'liushaojun', '刘少君', 'MALE', 44, '232321197108283917', 'NONE', '黑龙江省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39677243049168);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 39677243049168, '北京市', '700000', '400000', '奥迪', 'WAUS8B4H' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39677243049168);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 39677971956944;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39677971956944, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39677971956944);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39677971956944, 'wanglei', '王雷', 'MALE', 32, '110223198309240596', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39677971956944);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 39677971956944, '北京市', '250000', '60000', '克莱斯勒', 'BJ7270CX' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39677971956944);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 39678917788880;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39678917788880, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39678917788880);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39678917788880, 'wanggang', '王刚', 'MALE', 29, '232321198704174316', 'NONE', '黑龙江省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39678917788880);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 39678917788880, '黑龙江省', '1750000', '500000', '丰田', '5700' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39678917788880);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 39679606721744;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39679606721744, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39679606721744);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39679606721744, 'wupenghai', '吴鹏海', 'MALE', 39, '330323197609210614', 'NONE', '浙江省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39679606721744);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 39679606721744, '北京市', '700000', '380000', '奥迪', 'WAUAGD4L（Q7）' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39679606721744);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 39685425910992;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39685425910992, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39685425910992);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39685425910992, 'wangliang', '王良', 'MALE', 45, '230827197104030430', 'NONE', '黑龙江省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39685425910992);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 39685425910992, '黑龙江省', '120000', '40000', '华泰特拉卡', 'SDH6470FM4' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39685425910992);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 39686666614992;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39686666614992, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39686666614992);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39686666614992, 'daiweiling', '戴崴玲', 'FEMALE', 44, '110107197108150025', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39686666614992);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 39686666614992, '北京市', '200000', '110000', '别克', 'SGM7247ATA' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39686666614992);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 39848906664672;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39848906664672, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39848906664672);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39848906664672, 'zhangchanghong', '张长红', 'MALE', 30, '131121198510202272', 'NONE', '河北省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39848906664672);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 39848906664672, '北京市', '550000', '270000', '梅赛德斯—奔驰', 'WDCCB5EE' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39848906664672);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 39849489149664;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39849489149664, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39849489149664);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39849489149664, 'rendawei', '任大伟', 'MALE', 34, '220382198205020215', 'NONE', '吉林省', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39849489149664);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 39849489149664, '北京市', '440000', '240000', '宝马', 'BMW525Li' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39849489149664);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 39847091576544;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39847091576544, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39847091576544);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39847091576544, 'wangbaoshun', '王保顺', 'MALE', 35, '429001197009224997', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39847091576544);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 39847091576544, '北京市', '820000', '370000', '奥迪', 'WAUAGD4L' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39847091576544);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 39849861000928;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 39849861000928, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39849861000928);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 39849861000928, 'chenchonglin', '陈崇林', 'MALE', 36, '110106195703132416', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39849861000928);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 39849861000928, '北京市', '120000', '63000', '凌派', 'HG7180GAA5' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 39849861000928);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150730000039;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150730000039, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000039);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150730000039, 'xuebo', '薛波', 'MALE', 38, '610526197711194611', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000039);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150730000039, '北京市', '', '60000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000039);

UPDATE `aa`.`loan` SET pledge_type = 'VEHICLE' WHERE id = 20150730000039;
INSERT INTO `aa`.`loan_details` (loan_id, declaration) SELECT 20150730000039, '提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。'FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000039);
INSERT INTO `aa`.`loaner_details` (loan_id, login_name, user_name, gender, age, identity_number, marriage, region, income, employment_status) SELECT 20150730000039, 'xuebo', '薛波', 'MALE', 38, '610526197711194611', 'NONE', '北京市', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000039);
INSERT INTO `aa`.`pledge_vehicle` (loan_id, pledge_location, estimate_amount, loan_amount, brand, model) SELECT 20150730000039, '北京市', '', '60000', '', '' FROM DUAL WHERE EXISTS (SELECT 1 FROM `aa`.`loan` WHERE id = 20150730000039);

COMMIT;