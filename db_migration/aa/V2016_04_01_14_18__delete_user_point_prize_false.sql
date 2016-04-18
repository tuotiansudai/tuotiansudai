BEGIN ;
delete from user_point_prize where reality is false;
COMMIT ;