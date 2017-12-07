import yaml
import etcd3


def client(env):
    with open('./ttsd-etcd/src/main/resources/etcd-endpoints.yml', 'r') as stream:
        yaml_data = yaml.load(stream)

    endpoints = yaml_data.get((env or 'dev').lower())

    return Etcd3Client(endpoints, env)


class Etcd3Client(object):
    def __init__(self, endpoints, env):
        self.env = env.lower()
        for endpoint in endpoints:
            host, port = endpoint.split(':')
            self.etcd_client = etcd3.client(host=host, port=port, timeout=5)
            try:
                self.etcd_client.status()
                print 'endpoint is {}:{}'.format(host, port)
                return
            except Exception:
                pass

    def get(self, key):
        if key:
            value, _ = self.etcd_client.get('/{}/{}'.format(self.env, key))
            return value

    def put(self, key, value):
        self.etcd_client.put('/{}/{}'.format(self.env, key), value)
