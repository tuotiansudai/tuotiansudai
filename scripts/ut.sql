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

grant select,insert,update,delete,create,drop,alter,index on aa.* to root@'%' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on aa.* to root@'localhost' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on job_worker.* to root@'%' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on job_worker.* to root@'localhost' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on sms_operations.* to root@'%' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on sms_operations.* to root@'localhost' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on ump_operations.* to root@'%' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on ump_operations.* to root@'localhost' identified by 'root';

grant select,insert,update,delete,create,drop,alter,index on edxactivity.* to root@'%' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on edxactivity.* to root@'localhost' identified by 'root';

grant select,insert,update,delete,create,drop,alter,index on edxpoint.* to root@'%' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on edxpoint.* to root@'localhost' identified by 'root';

grant select,insert,update,delete,create,drop,alter,index on edxask.* to root@'%' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on edxask.* to root@'localhost' identified by 'root';

grant select,insert,update,delete,create,drop,alter,index on anxin_operations.* to root@'%' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on anxin_operations.* to root@'localhost' identified by 'root';

grant select,insert,update,delete,create,drop,alter,index on edxlog.* to root@'%' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on edxlog.* to root@'localhost' identified by 'root';

grant select,insert,update,delete,create,drop,alter,index on edxmessage.* to root@'%' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on edxmessage.* to root@'localhost' identified by 'root';

grant select,insert,update,delete,create,drop,alter,index on fudian.* to root@'%' identified by 'root';
grant select,insert,update,delete,create,drop,alter,index on fudian.* to root@'localhost' identified by 'root';

update mysql.user set password=password('root') where user='root';

flush privileges;
