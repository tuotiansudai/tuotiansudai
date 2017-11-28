import os
import random

import etcd3
import yaml

ETCD_ENDPOINT_CONFIG_FILE = '/app/etcd-endpoints.yml'
DEFAULT_ETCD_HOST = '127.0.0.1'
DEFAULT_ETCD_PORT = 2379


class EtcdConfig(object):
    def __init__(self, env):
        self.env = (env or 'dev').lower()

        host = DEFAULT_ETCD_HOST
        port = DEFAULT_ETCD_PORT
        if os.path.exists(ETCD_ENDPOINT_CONFIG_FILE):
            with open(ETCD_ENDPOINT_CONFIG_FILE, 'r') as stream:
                yaml_data = yaml.load(stream)

            endpoints = yaml_data.get(self.env)
            hosts = endpoints.get('host')
            ports = endpoints.get('port')
            host = hosts[random.randint(0, len(hosts) - 1)]
            port = ports[random.randint(0, len(ports) - 1)]
        self.client = etcd3.client(host=host, port=port)

    def get(self, key, default_value=None):
        value, _ = self.client.get('/{}/{}'.format(self.env, key))
        if value is None:
            return default_value
        if default_value is not None and type(default_value) == int:
            return int(value)
        return value
