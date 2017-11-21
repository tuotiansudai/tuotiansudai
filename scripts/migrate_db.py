import os
import sys

from paver.shell import sh

sys.path.insert(1, os.path.dirname(os.path.abspath(__file__)))


def migrate(etcd):
    common_environment, _ = etcd.get('common.environment')
    common_jdbc_host, _ = etcd.get('common.jdbc.host')
    common_jdbc_port, _ = etcd.get('common.jdbc.port')

    common_jdbc_username, _ = etcd.get('common.jdbc.username')
    common_jdbc_password, _ = etcd.get('common.jdbc.password')

    ask_jdbc_username, _ = etcd.get('ask.jdbc.username')
    ask_jdbc_password, _ = etcd.get('ask.jdbc.password')

    activity_jdbc_username, _ = etcd.get('activity.jdbc.username')
    activity_jdbc_password, _ = etcd.get('activity.jdbc.password')

    point_jdbc_username, _ = etcd.get("point.jdbc.username")
    point_jdbc_password, _ = etcd.get("point.jdbc.password")

    log_jdbc_username, _ = etcd.get("log.jdbc.username")
    log_jdbc_password, _ = etcd.get("log.jdbc.password")

    anxin_jdbc_username, _ = etcd.get("anxin.jdbc.username")
    anxin_jdbc_password, _ = etcd.get("anxin.jdbc.password")

    message_jdbc_username, _ = etcd.get("message.jdbc.username")
    message_jdbc_password, _ = etcd.get("message.jdbc.password")

    sh('gradle ttsd-config:flywayMigrate -Penv={} -Pdatabase={} -Phost={} -Pport={} -Pusername={} -Ppwd={}'.format(common_environment, 'aa', common_jdbc_host, common_jdbc_port, common_jdbc_username, common_jdbc_password))
    sh('gradle ttsd-config:flywayMigrate -Penv={} -Pdatabase={} -Phost={} -Pport={} -Pusername={} -Ppwd={}'.format(common_environment, 'ump_operations', common_jdbc_host, common_jdbc_port, common_jdbc_username, common_jdbc_password))
    sh('gradle ttsd-config:flywayMigrate -Penv={} -Pdatabase={} -Phost={} -Pport={} -Pusername={} -Ppwd={}'.format(common_environment, 'sms_operations', common_jdbc_host, common_jdbc_port, common_jdbc_username, common_jdbc_password))
    sh('gradle ttsd-config:flywayMigrate -Penv={} -Pdatabase={} -Phost={} -Pport={} -Pusername={} -Ppwd={}'.format(common_environment, 'job_worker', common_jdbc_host, common_jdbc_port, common_jdbc_username, common_jdbc_password))
    sh('gradle ttsd-config:flywayMigrate -Penv={} -Pdatabase={} -Phost={} -Pport={} -Pusername={} -Ppwd={}'.format(common_environment, 'edxask', common_jdbc_host, common_jdbc_port, ask_jdbc_username, ask_jdbc_password))
    sh('gradle ttsd-config:flywayMigrate -Penv={} -Pdatabase={} -Phost={} -Pport={} -Pusername={} -Ppwd={}'.format(common_environment, 'edxactivity', common_jdbc_host, common_jdbc_port, activity_jdbc_username, activity_jdbc_password))
    sh('gradle ttsd-config:flywayMigrate -Penv={} -Pdatabase={} -Phost={} -Pport={} -Pusername={} -Ppwd={}'.format(common_environment, 'edxpoint', common_jdbc_host, common_jdbc_port, point_jdbc_username, point_jdbc_password))
    sh('gradle ttsd-config:flywayMigrate -Penv={} -Pdatabase={} -Phost={} -Pport={} -Pusername={} -Ppwd={}'.format(common_environment, 'edxlog', common_jdbc_host, common_jdbc_port, log_jdbc_username, log_jdbc_password))
    sh('gradle ttsd-config:flywayMigrate -Penv={} -Pdatabase={} -Phost={} -Pport={} -Pusername={} -Ppwd={}'.format(common_environment, 'edxmessage', common_jdbc_host, common_jdbc_port, message_jdbc_username, message_jdbc_password))
