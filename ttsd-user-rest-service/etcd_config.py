import random

import etcd3
import yaml


class EtcdConfig(object):
    def __init__(self, env):
        with open('/app/etcd-endpoints.yml', 'r') as stream:
            yaml_data = yaml.load(stream)

        self.env = (env or 'dev').lower()
        endpoints = yaml_data.get(self.env)
        hosts = endpoints.get('host')
        ports = endpoints.get('port')
        host = hosts[random.randint(0, len(hosts) - 1)]
        port = ports[random.randint(0, len(ports) - 1)]
        self.client = etcd3.client(host=host, port=port)

    def get(self, key, default_value=None):
        value, _ = self.client.get('/{}/{}'.format(self.env, key))
        return value if value else default_value

    def get_int(self, key, default_value=None):
        str_value = self.get(key)
        if str_value is None:
            return default_value
        else:
            return int(str_value)

    def get_bool(self, key):
        str_value = self.get(key)
        return str_value and 'true' == str_value.lower()
