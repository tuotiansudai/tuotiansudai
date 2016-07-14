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

def main(host, passwd, port, log):
  r = redis.StrictRedis(host=host, password=passwd, port=port, db=0)

  res = r.info()

  for p in redis_keys:
    if p in res:
      log.write('{"host":%(host)s,"key":redis.%(name)s,"value":%(val)s,"date":%(local_time)s}\n' % { 'host': host, 'name': p, 'val': res[p] ,'local_time':time.strftime("%Y-%m-%d %H:%M:%S")})
  if not log.isatty():      # tty is line buffer 
      log.flush()

#
# main app
#
if __name__ == "__main__":
  p = optparse.OptionParser(conflict_handler="resolve", description="This Zabbix plugin checks the health of redis instance.")
  p.add_option('-h', '--host', action='store', type='string', dest='host', default="127.0.0.1", help='The hostname you want to connect to')
  p.add_option('-P', '--port', action='store', type='string', dest='port', default=6379, help='The port to connect on')
  p.add_option('-p', '--passwd', action='store', type='string', dest='password', default=None, help='The password to authenticate with')
  p.add_option('-f', '--file_path', action='store', type='string', dest='file_path', default=None, help='The out file path to authenticate with')
  options, arguments = p.parse_args()

  if options.host is None :
     p.print_help()
     sys.exit(1)

  log_out = sys.stdout 
  if options.file_path is not None:
    f_handler = open(options.file_path, "a")
    log_out = f_handler
  start_time = time.strftime("%Y%m%d")
  while start_time == time.strftime("%Y%m%d"):
    main(options.host, options.password, options.port, log_out)
    time.sleep(1)
