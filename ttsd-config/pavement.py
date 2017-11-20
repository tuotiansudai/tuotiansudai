import os
import sys

import etcd3
from paver.tasks import task, cmdopts

sys.path.insert(1, os.path.dirname(os.path.abspath(__file__)))

ETCD_HOST = {'DEV': '127.0.0.1',
             'QA1': '192.168.1.139',
             'QA2': '192.168.1.139',
             'QA3': '192.168.1.139',
             'QA4': '192.168.1.139',
             'QA5': '192.168.1.139'}

ETCD_PORT = {'DEV': '2379',
             'QA1': '23791',
             'QA2': '23791',
             'QA3': '23791',
             'QA4': '23791',
             'QA5': '23791'}


@task
@cmdopts([
    ('env=', 'e', 'environment, default is DEV')
])
def flush(options):
    """
    Import config into etcd
    e.g. paver flush.env=DEV flush
    """
    env = options.env if hasattr(options, 'env') else 'DEV'

    etcd = etcd3.client(host=ETCD_HOST.get(env), port=ETCD_PORT.get(env))

    raw_content = []

    with open('{}/src/main/resources/ttsd-biz.properties'.format(os.path.dirname(os.path.abspath(__file__)))) as f:
        raw_content += f.readlines()

    if env != 'DEV':
        with open('{}/src/main/resources/envs/QA-common.properties'.format(os.path.dirname(os.path.abspath(__file__)))) as f:
            raw_content += f.readlines()

        with open('{}/src/main/resources/envs/{}.properties'.format(os.path.dirname(os.path.abspath(__file__)), env)) as f:
            raw_content += f.readlines()
    else:
        with open('{}/src/main/resources/ttsd-env.properties'.format(os.path.dirname(os.path.abspath(__file__)))) as f:
            raw_content += f.readlines()

    for line in raw_content:
        line = line.strip()
        if line and line[0] != '#':
            properties_key, properties_value = line.split("=")
            etcd_value, _ = etcd.get(properties_key)
            if properties_value != etcd_value:
                etcd.put(properties_key, properties_value)
