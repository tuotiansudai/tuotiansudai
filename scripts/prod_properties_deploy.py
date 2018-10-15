import os
from scripts import etcd_client


def flush_prod_properties(etcd):
    print '---------------------------------------start'
    file_names = file_name('./prod-properties')
    print file_names
    for file in file_names:
        print '123123123'.format(etcd.get(file))
        if not etcd.get(file):
            etcd.put(file, 'SUCCESS')
            deploy_prop = load_properties('./prod-properties/{0}.properties'.format(file))
            print 'put etcd file:{0}'.format(file)
            for props in deploy_prop:
                for key, value in props.items():
                    etcd.put(key, value)
                    print 'put {0}={1}'.format(key, value)

    print '---------------------------------------end'



def file_name(file_dir):
    file_names = []
    for root, dirs, files in os.walk(file_dir):
        for file in files:
            if os.path.splitext(file)[1] == '.properties':
                file_names.append(os.path.splitext(file)[0])
                print os.path.splitext(file)[0]
    return file_names


def load_properties(file_path):
    props = {}
    with open(file_path, "r") as f:
        for line in f:
            striped_line = line.strip()
            if striped_line and not striped_line.startswith('#'):
                key_value = striped_line.split('=')
                key = key_value[0].strip()
                value = '='.join(key_value[1:]).strip()
                props[key] = value
    return props
