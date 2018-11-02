update loan set `name` = replace(`name`, '抵押', '消费') where `name` like '%车辆抵押借款%';
