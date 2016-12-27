CREATE TABLE `audit_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `auditor_login_name` varchar(25) DEFAULT NULL,
  `auditor_mobile` varchar(18) DEFAULT NULL,
  `operator_login_name` varchar(25) NOT NULL,
  `operator_mobile` varchar(18) NOT NULL,
  `target_id` varchar(25) DEFAULT NULL,
  `operation_type` varchar(20) NOT NULL,
  `ip` varchar(15) NOT NULL,
  `operation_time` datetime NOT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8


insert into audit_log (auditor_login_name, auditor_mobile,operator_login_name, operator_mobile,target_id,operation_type,ip,operation_time, description)
select l.auditor_login_name, auditor.mobile as auditor_mobile, l.operator_login_name, operator.mobile as operator_mobile, l.target_id,l.operation_type,
l.ip,l.operation_time,l.description
from aa.audit_log l
left join aa.user auditor on l.auditor_login_name = auditor.login_name
join aa.user operator on l.operator_login_name=operator.login_name;
