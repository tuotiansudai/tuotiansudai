# encoding=utf-8
import re


def deploy(etcd, env):
    deploy_prop = [load_properties("./ttsd-config/src/main/resources/ttsd-env.properties"),
                   load_properties("./ttsd-config/src/main/resources/ttsd-biz.properties")]

    if re.match('^qa\d$', env, re.I) or env.lower() in ('ft', 'hzft'):
        # 读取QA环境公用配置 ttsd-env-QA-common.properties 生成一个 dict 对象 qa_common_prop
        deploy_prop.append(load_properties("./ttsd-config/src/main/resources/envs/QA-common.properties"))
        # 读取每个环境特有的配置 envs/${env}.properties, 生成一个 dict 对象 specified_prop
        deploy_prop.append(load_properties("./ttsd-config/src/main/resources/envs/{0}.properties".format(env)))

        flush_etcd(etcd, deploy_prop)
        return

    # 读取每个环境特有的配置 envs/${env}.properties, 生成一个 dict 对象 specified_prop
    deploy_prop.append(load_properties("./ttsd-config/src/main/resources/envs/{0}.properties".format(env)))
    flush_etcd(etcd, deploy_prop)


def flush_etcd(etcd, deploy_prop):
    for props in deploy_prop:
        for key, value in props.items():
            etcd.put(key, value)
            print 'put etcd {0}={1}'.format(key, value)


# 读取properties 文件, 生成 dict 对象
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
