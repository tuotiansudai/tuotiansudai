BEGIN ;

alter table `loan` add index `INDEX_LOAN_STATUS` (`status`) USING BTREE;

COMMIT ;