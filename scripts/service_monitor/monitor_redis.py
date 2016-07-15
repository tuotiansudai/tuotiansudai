#!/usr/bin/env python

import optparse
import redis
import sys
import time
import os
import traceback
from datetime import datetime, timedelta

redis_keys = [
    # 'pubsub_patterns',
    # 'connected_slaves',
    # 'uptime_in_days',
    # 'used_cpu_user',
    # 'used_cpu_user_children',
    # 'used_cpu_sys_children',
    # 'pubsub_channels',
    'connected_clients',
    # 'blocked_clients',
    # 'rejected_connections',
    # 'total_connections_received',
    # 'used_memory_rss',
    # 'used_cpu_sys',
    # 'used_memory_lua',
    # 'used_memory',
    # 'used_memory_human',
    # 'used_memory_peak',
    # 'mem_fragmentation_ratio',
    # 'expired_keys',
    # 'evicted_keys',
    # 'keyspace_hits',
    # 'keyspace_misses',
    # 'repl_backlog_size',
    # 'instantaneous_input_kbps',
    # 'instantaneous_output_kbps',
    # 'total_net_input_bytes',
    # 'total_net_output_bytes',
    # 'instantaneous_ops_per_sec',
    # 'total_commands_processed',
    # 'uptime_in_seconds',
    # legacy keys for 3.0.0 and below
    # 'bytes_received_per_sec',
    # 'bytes_sent_per_sec'
]


class MonitorRedis():
    def __init__(self, options):
        self.host = options.host
        self.conn = redis.StrictRedis(host=options.host, password=options.password, port=options.port, db=0)


    def __enter__(self):
	try:
          self.log = open("%s/redis-%s.log" % (options.folder_path, datetime.today().strftime("%Y%m%d")), "a")
        except Exception, e:
          with open('/var/log/monitor_redis.log','a') as f:
            f.write('%s' % traceback.format_exc())
            f.close()
            sys.exit()
        return self


    def __exit__(self, *args):
        self.log.close()


    def logOut(self):
        res = self.conn.info()
        log_str = '{"host":%s,[' % (self.host)
        for p in redis_keys:
            if p in res:
                log_str += '{%(name)s:%(val)s},' % {'name': p, 'val': res[p]}
        log_str = log_str[0:-1]
        self.log.write(log_str + '],"date":%s}\n' % (time.strftime("%Y-%m-%d %H:%M:%S")))
        self.log.flush()

# main app
#
if __name__ == "__main__":
    now = datetime.now()

    p = optparse.OptionParser(conflict_handler="resolve",
                              description="This Zabbix plugin checks the health of redis instance.")
    p.add_option('-h', '--host', action='store', type='string', dest='host', default="127.0.0.1",
                 help='The hostname you want to connect to')
    p.add_option('-P', '--port', action='store', type='string', dest='port', default=6379,
                 help='The port to connect on')
    p.add_option('-p', '--passwd', action='store', type='string', dest='password', default=None,
                 help='The password to authenticate with')
    p.add_option('-f', '--folder_path', action='store', type='string', dest='folder_path',
                 default="/var/log/redis-monitor", help='The out file path to authenticate with')
    options, arguments = p.parse_args()
    if not os.path.exists(options.folder_path):
        try:
            os.mkdir(options.folder_path)
        except Exception, e:
            with open('/var/log/monitor_redis.log', 'a') as f:
                f.write('%s' % traceback.format_exc())
                f.close()
            exit(1)

    with MonitorRedis(options) as redis_monitor:
        for i in xrange(60 * 60 * 24):
            redis_monitor.logOut()
            time.sleep(1)
