BEGIN;

INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('消费借款','_90',1,100000,1000000,0.001);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('消费借款','_90',2,1000000,5000000,0.003);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('消费借款','_90',3,5000000,0,0.005);

INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('消费借款','_180',1,100000,1000000,0.002);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('消费借款','_180',2,1000000,5000000,0.004);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('消费借款','_180',3,5000000,0,0.008);

INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('消费借款','_360',1,100000,1000000,0.003);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('消费借款','_360',2,1000000,5000000,0.006);
INSERT INTO `extra_loan_rate_rule` (`name`,`product_type`,`level`,`min_invest_amount`,`max_invest_amount`,`rate`) VALUES ('消费借款','_360',3,5000000,0,0.012);

COMMIT;