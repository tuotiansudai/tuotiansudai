# coding=utf-8
import os
from scripts import etcd_client


def flush_prod_properties(etcd):
    file_names = file_name('./config-properties/prod')
    for file in file_names:
        if not etcd.get(file):
            deploy_prop = load_properties('./config-properties/prod/{0}.properties'.format(file))
            for key, value in deploy_prop.items():
                etcd.put(key, value)
            etcd.put(file, 'SUCCESS')


def flush_qa_properties(etcd):
    # 先刷新prod配置
    flush_prod_properties(etcd)

    # 如果存在 qa与prod value 不同,则添加一个qa文件覆盖prod值
    file_names = file_name('./config-properties/qa')
    for file in file_names:
        deploy_prop = load_properties('./config-properties/dev/{0}.properties'.format(file))
        for key, value in deploy_prop.items():
            etcd.put(key, value)


def file_name(file_dir):
    file_names = []
    for root, dirs, files in os.walk(file_dir):
        for file in files:
            if os.path.splitext(file)[1] == '.properties':
                file_names.append(os.path.splitext(file)[0])
                print os.path.splitext(file)
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


if __name__ == '__main__':
    flush_prod_properties(None)