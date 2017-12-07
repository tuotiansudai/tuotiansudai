import os
import etcd3
import yaml

ETCD_ENDPOINT_CONFIG_FILE = '/app/etcd-endpoints.yml'


class EtcdConfig(object):
    def __init__(self, env):
        self.env = (env or 'dev').lower()

        if os.path.exists(ETCD_ENDPOINT_CONFIG_FILE):
            with open(ETCD_ENDPOINT_CONFIG_FILE, 'r') as stream:
                yaml_data = yaml.load(stream)

            endpoints = yaml_data.get(self.env)
            for endpoint in endpoints:
                host, port = endpoint.split(':')
                self.client = etcd3.client(host=host, port=port)
                try:
                    self.client.status()
                    print 'endpoint is {}:{}'.format(host, port)
                    return
                except Exception:
                    pass

    def get(self, key, default_value=None):
        value, _ = self.client.get('/{}/{}'.format(self.env, key))
        if value is None:
            return default_value
        if default_value is not None and type(default_value) == int:
            return int(value)
        return value
