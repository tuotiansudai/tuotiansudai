# encoding=utf-8
import re
import redis


def deploy(etcd, env, pay_fake, sms_fake):
    deploy_prop = [load_properties("./ttsd-config/src/main/resources/ttsd-env.properties"),
                   load_properties("./ttsd-config/src/main/resources/ttsd-biz.properties")]

    if re.match('^qa\d$', env, re.I) or env.lower() in ('ft', 'hzft'):
        # 读取QA环境公用配置 ttsd-env-QA-common.properties 生成一个 dict 对象 qa_common_prop
        deploy_prop.append(load_properties("./ttsd-config/src/main/resources/envs/QA-common.properties"))
        # 读取每个环境特有的配置 envs/${env}.properties, 生成一个 dict 对象 specified_prop
        deploy_prop.append(load_properties("./ttsd-config/src/main/resources/envs/{0}.properties".format(env)))
        # 从redis里读取 三方账号配置
        _r = redis.StrictRedis(host=('192.168.1.30' if env != 'CI1' else '127.0.0.1'), port=6379, db=2)
        deploy_prop.append(_r.hgetall('qa_common_account'))

        flush_etcd(etcd, deploy_prop, pay_fake, sms_fake)
        return

    # 读取每个环境特有的配置 envs/${env}.properties, 生成一个 dict 对象 specified_prop
    deploy_prop.append(load_properties("./ttsd-config/src/main/resources/envs/{0}.properties".format(env)))
    flush_etcd(etcd, deploy_prop, pay_fake, sms_fake)


def flush_etcd(etcd, deploy_prop, pay_fake, sms_fake):
    for props in deploy_prop:
        for key, value in props.items():
            etcd.put(key, value)
            print 'put etcd {0}={1}'.format(key, value)

    if pay_fake is not None:
        etcd.put('pay.fake', pay_fake)

    if sms_fake is not None:
        etcd.put('sms.fake', sms_fake)


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
