grant select,insert,update,delete,create,drop on aa.* to tuotiansd@'%' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop on job_worker.* to tuotiansd@'%' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop on sms_operations.* to tuotiansd@'%' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop on ump_operations.* to tuotiansd@'%' identified by 'tuotiansd';

grant select,insert,update,delete,create,drop on edxactivity.* to sdactivity@'%' identified by 'edxactivity';

grant select,insert,update,delete,create,drop on edxpoint.* to sdpoint@'%' identified by 'edxpoint';

grant select,insert,update,delete,create,drop on edxask.* to sdask@'%' identified by 'edxask';

update user set password=password('root') where user='root';

flush privileges;
