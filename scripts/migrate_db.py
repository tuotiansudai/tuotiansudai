import os
import sys

sys.path.insert(1, os.path.dirname(os.path.abspath(__file__)))


def migrate(gradle, etcd, exec_fun):
    db_host = etcd.get('common.jdbc.host')
    db_port = etcd.get('common.jdbc.port')
    schema_username_password = {
        'aa':
            {'username': etcd.get('common.jdbc.username'),
             'password': etcd.get('common.jdbc.password')},
        'ump_operations':
            {'username': etcd.get('common.jdbc.username'),
             'password': etcd.get('common.jdbc.password')},
        'sms_operations':
            {'username': etcd.get('common.jdbc.username'),
             'password': etcd.get('common.jdbc.password')},
        'anxin_operations':
            {'username': etcd.get('anxin.jdbc.username'),
             'password': etcd.get('anxin.jdbc.password')},
        'job_worker':
            {'username': etcd.get('common.jdbc.username'),
             'password': etcd.get('common.jdbc.password')},
        'edxask':
            {'username': etcd.get('ask.jdbc.username'),
             'password': etcd.get('ask.jdbc.password')},
        'edxactivity':
            {'username': etcd.get('activity.jdbc.username'),
             'password': etcd.get('activity.jdbc.password')},
        'edxpoint':
            {'username': etcd.get('point.jdbc.username'),
             'password': etcd.get('point.jdbc.password')},
        'edxlog':
            {'username': etcd.get('log.jdbc.username'),
             'password': etcd.get('log.jdbc.password')},
        'edxmessage':
            {'username': etcd.get('message.jdbc.username'),
             'password': etcd.get('message.jdbc.password')},
    }

    for schema, username_password in schema_username_password.items():
        username = username_password.get('username')
        password = username_password.get('password')
        exec_fun('{} ttsd-config:flywayMigrate -Pdatabase={} -Phost={} -Pport={} -Pusername={} -Ppwd={}'.format(gradle,
                                                                                                          schema,
                                                                                                          db_host,
                                                                                                          db_port,
                                                                                                          username,
                                                                                                          password))
