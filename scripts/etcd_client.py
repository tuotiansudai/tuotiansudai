import yaml
import random
import etcd3


def client(env):
    with open('./ttsd-etcd/src/main/resources/etcd-endpoints.yml', 'r') as stream:
        yaml_data = yaml.load(stream)

    endpoints = yaml_data.get((env or 'dev').lower())
    hosts = endpoints.get('host')
    ports = endpoints.get('port')
    host = hosts[random.randint(0, len(hosts) - 1)]
    port = ports[random.randint(0, len(ports) - 1)]

    return etcd3.client(host=host, port=port)
