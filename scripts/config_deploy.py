# encoding=utf-8
import redis


def deploy(etcd, build_params):

    env = build_params['env']

    # 读取QA环境公用配置 ttsd-env-QA-common.properties 生成一个 dict 对象 qa_common_prop
    qa_common_prop = load_properties("./ttsd-config/src/main/resources/envs/QA-common.properties")

    # 读取每个环境特有的配置 envs/${env}.properties, 生成一个 dict 对象 env_prop
    env_prop = load_properties("./ttsd-config/src/main/resources/envs/{0}.properties".format(env))

    # 从redis里读取 三方账号配置
    _r = redis.StrictRedis(host=('192.168.1.30' if env!='CI1' else '127.0.0.1'), port=6379, db=2)
    sec_prop = _r.hgetall('qa_common_account')

    # 逐行读取 ttsd-env.properties, 如果不是注释也不是空行，则split by "=",
    # 依次从 build_params > sec_prop > env_prop > qa_common_prop 中读取key，
    # 如果存在就替换原值，并将结果写入到结果文件 dist_file 中。

    with open("./ttsd-config/src/main/resources/ttsd-env.properties", "r") as f:
        for line in f:
            l = line.strip()
            if l and not l.startswith("#"):
                key_value = l.split("=")
                key = key_value[0].strip()
                value =  get_build_params_value_by_key(build_params, key) \
                         or (sec_prop[key] + ' ' if sec_prop.has_key(key) else False) \
                         or (env_prop[key] + ' ' if env_prop.has_key(key) else False) \
                         or (qa_common_prop[key] + ' ' if qa_common_prop.has_key(key) else False) \
                         or key_value[-1].strip()
                etcd.put(key, value.strip())
                print 'put etcd {0}={1}'.format(key, value.strip())


# 读取properties 文件, 生成 dict 对象
def load_properties(file_path):
    props = {}
    with open(file_path, "r") as f:
        for line in f:
            l = line.strip()
            if l and not l.startswith('#'):
                key_value = l.split('=')
                key = key_value[0].strip()
                value = '='.join(key_value[1:]).strip()
                props[key] = value
    return props

def get_build_params_value_by_key(build_params, key):
    split = key.split('.')
    value = build_params
    for item in split:
        value = value[item] if isinstance(value, dict) and value.has_key(item) else None
    return value
