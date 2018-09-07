-- 测试环境的密码，需要在后面追加环境ip 如 tuotiansd153, sdactivity153 ...
CREATE DATABASE `aa`;
CREATE DATABASE `job_worker`;
CREATE DATABASE `sms_operations`;
CREATE DATABASE `ump_operations`;
CREATE DATABASE `edxactivity`;
CREATE DATABASE `edxpoint`;
CREATE DATABASE `edxask`;
CREATE DATABASE `anxin_operations`;
CREATE DATABASE `edxlog`;
CREATE DATABASE `edxmessage`;
CREATE DATABASE `fudian`;

grant select,insert,update,delete,create,drop,alter,index on aa.* to tuotiansd@'%' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop,alter,index on aa.* to tuotiansd@'localhost' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop,alter,index on job_worker.* to tuotiansd@'%' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop,alter,index on job_worker.* to tuotiansd@'localhost' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop,alter,index on sms_operations.* to tuotiansd@'%' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop,alter,index on sms_operations.* to tuotiansd@'localhost' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop,alter,index on ump_operations.* to tuotiansd@'%' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop,alter,index on ump_operations.* to tuotiansd@'localhost' identified by 'tuotiansd';

grant select,insert,update,delete,create,drop,alter,index on edxactivity.* to sdactivity@'%' identified by 'sdactivity';
grant select,insert,update,delete,create,drop,alter,index on edxactivity.* to sdactivity@'localhost' identified by 'sdactivity';

grant select,insert,update,delete,create,drop,alter,index on edxpoint.* to sdpoint@'%' identified by 'sdpoint';
grant select,insert,update,delete,create,drop,alter,index on edxpoint.* to sdpoint@'localhost' identified by 'sdpoint';

grant select,insert,update,delete,create,drop,alter,index on edxask.* to sdask@'%' identified by 'sdask';
grant select,insert,update,delete,create,drop,alter,index on edxask.* to sdask@'localhost' identified by 'sdask';

grant select,insert,update,delete,create,drop,alter,index on anxin_operations.* to sdanxin@'%' identified by 'sdanxin';
grant select,insert,update,delete,create,drop,alter,index on anxin_operations.* to sdanxin@'localhost' identified by 'sdanxin';

grant select,insert,update,delete,create,drop,alter,index on edxlog.* to sdlog@'%' identified by 'sdlog';
grant select,insert,update,delete,create,drop,alter,index on edxlog.* to sdlog@'localhost' identified by 'sdlog';

grant select,insert,update,delete,create,drop,alter,index on edxmessage.* to sdmessage@'%' identified by 'sdmessage';
grant select,insert,update,delete,create,drop,alter,index on edxmessage.* to sdmessage@'localhost' identified by 'sdmessage';

grant select,insert,update,delete,create,drop,alter,index on fudian.* to tuotiansd@'%' identified by 'tuotiansd';
grant select,insert,update,delete,create,drop,alter,index on fudian.* to tuotiansd@'localhost' identified by 'tuotiansd';

update mysql.user set password=password('root') where user='root';

flush privileges;
