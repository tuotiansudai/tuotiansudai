import os
import sys
import etcd3
from paver.tasks import task, cmdopts

sys.path.insert(1, os.path.dirname(os.path.abspath(__file__)))

@task
@cmdopts([
    ('host=', '', 'default is 127.0.0.1'),
    ('port=', '', 'default is 2379')
])
def flush_etcd(options):
    """
    Import dev config into etcd
    e.g. paver flush_etcd.host=127.0.0.1 flush_etcd.port=2379 flush_etcd
    """
    host = options.host if hasattr(options, 'host') else '127.0.0.1'
    port = options.port if hasattr(options, 'port') else '2379'

    etcd = etcd3.client(host=host, port=port)

    raw_content = []

    with open('{}/src/main/resources/ttsd-biz.properties'.format(os.path.dirname(os.path.abspath(__file__)))) as f:
        raw_content += f.readlines()

    with open('{}/src/main/resources/ttsd-env.properties'.format(os.path.dirname(os.path.abspath(__file__)))) as f:
        raw_content += f.readlines()

    for line in raw_content:
        line = line.strip()
        if line and line[0] != '#':
            key_value = line.split("=")
            properties_key = key_value[0].strip()
            properties_value = '='.join(key_value[1:]).strip()
            etcd_value, _ = etcd.get('/dev/{}'.format(properties_key))
            if properties_value != etcd_value:
                print '{}={}'.format(properties_key, properties_value)
                etcd.put('/dev/{}'.format(properties_key), properties_value)
            else:
                print '{}={}'.format(properties_key, etcd_value)
