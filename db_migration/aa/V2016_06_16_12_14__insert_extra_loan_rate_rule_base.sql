BEGIN ;

INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('房产抵押借款','_90',1,100000,1000000,0.2);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('房产抵押借款','_90',2,1000000,5000000,0.4);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('房产抵押借款','_90',3,5000000,0,0.8);

INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('房产抵押借款','_180',1,100000,1000000,0.3);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('房产抵押借款','_180',2,1000000,5000000,0.5);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('房产抵押借款','_180',3,5000000,0,1.0);

INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('房产抵押借款','_360',1,100000,1000000,0.4);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('房产抵押借款','_360',2,1000000,5000000,0.8);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('房产抵押借款','_360',3,5000000,0,1.2);

INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('车辆抵押借款','_90',1,100000,1000000,0.1);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('车辆抵押借款','_90',2,1000000,5000000,0.3);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('车辆抵押借款','_90',3,5000000,0,0.5);

INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('车辆抵押借款','_180',1,100000,1000000,0.2);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('车辆抵押借款','_180',2,1000000,5000000,0.4);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('车辆抵押借款','_180',3,5000000,0,0.8);

INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('车辆抵押借款','_360',1,100000,1000000,0.3);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('车辆抵押借款','_360',2,1000000,5000000,0.6);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('车辆抵押借款','_360',3,5000000,0,1.2);

COMMIT ;