BEGIN ;
alter table `system_bill` add index `SYSTEM_BILL_SEQ_NUM` (`seq_num`) USING BTREE;
COMMIT ;
