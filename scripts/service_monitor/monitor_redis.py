#!/usr/bin/env python

import optparse
import redis
import sys
import time

redis_keys = [
#'pubsub_patterns',
#'connected_slaves',
#'uptime_in_days',
#'used_cpu_user',
#'used_cpu_user_children',
#'used_cpu_sys_children',
#'pubsub_channels',
'connected_clients',
#'blocked_clients',
#'rejected_connections',
#'total_connections_received',
#'used_memory_rss',
#'used_cpu_sys',
#'used_memory_lua',
#'used_memory',
#'used_memory_human',
#'used_memory_peak',
#'mem_fragmentation_ratio',
#'expired_keys',
#'evicted_keys',
#'keyspace_hits',
#'keyspace_misses',
#'repl_backlog_size',
#'instantaneous_input_kbps',
#'instantaneous_output_kbps',
#'total_net_input_bytes',
#'total_net_output_bytes',
#'instantaneous_ops_per_sec',
#'total_commands_processed',
#'uptime_in_seconds',
# legacy keys for 3.0.0 and below
#'bytes_received_per_sec',
#'bytes_sent_per_sec'
]

def get_redis(options):
  return redis.StrictRedis(host=options.host, password=options.password, port=options.port, db=0)

def main(argv):
  p = optparse.OptionParser(conflict_handler="resolve", description="This Zabbix plugin checks the health of redis instance.")

  p.add_option('-h', '--host', action='store', type='string', dest='host', default="127.0.0.1", help='The hostname you want to connect to')
  p.add_option('-P', '--port', action='store', type='string', dest='port', default=6379, help='The port to connect on')
  p.add_option('-p', '--passwd', action='store', type='string', dest='password', default=None, help='The password to authenticate with')

  options, arguments = p.parse_args()

  if options.host is None :
     return p.print_help()

  r = get_redis(options)

  return print_keys(r, options.host)

def print_keys(r, host):
  res = r.info()

  for p in redis_keys:
    if p in res:
        print '{"host":%(host)s,"key":redis.%(name)s,"value":%(val)s,"date":%(local_time)s}' % { 'host': host, 'name': p, 'val': res[p] ,'local_time':time.strftime("%Y-%m-%d %H:%M:%S")}

#
# main app
#
if __name__ == "__main__":
  sys.exit(main(sys.argv[1:]))
