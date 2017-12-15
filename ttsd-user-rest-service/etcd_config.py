import os
import etcd3
import yaml

BASE_DIR = os.path.dirname(os.path.abspath(__file__))

ETCD_ENDPOINT_CONFIG_FILE = '/app/etcd-endpoints.yml'
ETCD_ENDPOINT_CONFIG_FILE_DEV = os.path.join(BASE_DIR, '../ttsd-etcd/src/main/resources/etcd-endpoints.yml')


class EtcdConfig(object):
    def __init__(self, env):
        self.env = (env or 'dev').lower()

        etcd_endpoint_config_file = None

        if os.path.exists(ETCD_ENDPOINT_CONFIG_FILE):
            etcd_endpoint_config_file = ETCD_ENDPOINT_CONFIG_FILE
        elif os.path.exists(ETCD_ENDPOINT_CONFIG_FILE_DEV):
            etcd_endpoint_config_file = ETCD_ENDPOINT_CONFIG_FILE_DEV

        if etcd_endpoint_config_file:
            with open(etcd_endpoint_config_file, 'r') as stream:
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
        if default_value is not None:
            if type(default_value) == int:
                return int(value)

            if type(default_value) == bool:
                return value == 'True'
        return value
