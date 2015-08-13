BEGIN ;

alter table `user` add index `USER_INDEX_REGISTER_TIME` (`register_time`) USING BTREE;

COMMIT ;