DELETE FROM luxury_prize
WHERE id IN (1, 2);
DELETE FROM travel_prize
WHERE id IN (1, 2, 3);
INSERT INTO `luxury_prize` (`id`, `brand`, `name`, `price`, `image`, `invest_amount`, `ten_percent_off_invest_amount`, `twenty_percent_off_invest_amount`, `thirty_percent_off_invest_amount`, `introduce`, `created_by`, `created_time`)
VALUES ('1', '天梭（TISSOT）', '经典系列皮带日历石英表', '1500', 'image', '38000000', '4000000', '8000000', '12000000', 'introduce', 'hourglasskoala', '2016-08-31 00:00:00');
INSERT INTO `luxury_prize` (`id`, `brand`, `name`, `price`, `image`, `invest_amount`, `ten_percent_off_invest_amount`, `twenty_percent_off_invest_amount`, `thirty_percent_off_invest_amount`, `introduce`, `created_by`, `created_time`)
VALUES ('2', '天梭（TISSOT）', '经典系列时尚石英男表', '1750', 'image', '44000000', '5000000', '9000000', '14000000', 'introduce', 'hourglasskoala', '2016-08-31 00:00:00');
INSERT INTO `travel_prize` (`id`, `name`, `price`, `image`, `invest_amount`, `introduce`, `created_by`, `created_time`)
VALUES ('1', '韩国游轮七日游', '1680', 'image', '56000000', 'introduce', 'hourglasskoala', '2016-08-31 00:00:00');
INSERT INTO `travel_prize` (`id`, `name`, `price`, `image`, `invest_amount`, `introduce`, `created_by`, `created_time`)
VALUES ('2', '壶口瀑布三日游', '1398', 'image', '44000000', 'introduce', 'hourglasskoala', '2016-08-31 00:00:00');
INSERT INTO `travel_prize` (`id`, `name`, `price`, `image`, `invest_amount`, `introduce`, `created_by`, `created_time`)
VALUES ('3', '五台山三日游', '818', 'image', '21000000', 'introduce', 'hourglasskoala', '2016-08-31 00:00:00');