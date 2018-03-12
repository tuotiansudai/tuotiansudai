begin;
update loaner_details set region = '河北省' where `region` in (' 河北省','河北','石家庄');
update loaner_details set region = '黑龙江省' where `region` in ('120000','黑龙江');
update loaner_details set region = '内蒙古自治区' where `region` in ('内蒙古','内蒙古省');
update loaner_details set region = '北京市' where `region` in ('北京');
update loaner_details set region = '四川省' where `region` in ('成都市');
update loaner_details set region = '天津市' where `region` in ('天津');
update loaner_details set region = '宁夏回族自治区' where `region` in ('宁夏');
update loaner_details set region = '山东省' where `region` in ('山东');
update loaner_details set region = '浙江省' where `region` in ('杭州');
update loaner_details set region = '福建省' where `region` in ('福建');
update loaner_details set region = '辽宁省' where `region` in ('辽宁');
update loaner_details set region = '湖南省' where `region` in ('长沙');
update loaner_details set region = '重庆市' where `region` in ('重庆');

COMMIT ;