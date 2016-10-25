grant select,insert,update,delete,create,drop,alter on aa.* to tuotiansd@'%' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop,alter on job_worker.* to tuotiansd@'%' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop,alter on sms_operations.* to tuotiansd@'%' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop,alter on ump_operations.* to tuotiansd@'%' identified by 'tuotiansd';

grant select,insert,update,delete,create,drop,alter on edxactivity.* to sdactivity@'%' identified by 'sdactivity';

grant select,insert,update,delete,create,drop,alter on edxpoint.* to sdpoint@'%' identified by 'sdpoint';

grant select,insert,update,delete,create,drop,alter on edxask.* to sdask@'%' identified by 'sdask';

update mysql.user set password=password('root') where user='root';

flush privileges;
