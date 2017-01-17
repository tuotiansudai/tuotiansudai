# encoding=utf-8
import redis


def deploy(env, source_folder, dist_file):
    if not env:
        print "Fail: env was not set!"
        return

    # 读取QA环境公用配置 ttsd-env-QA-common.properties 生成一个 dict 对象 qa_common_prop
    qa_common_prop = load_properties("{0}envs/QA-common.properties".format(source_folder))

    # 读取每个环境特有的配置 envs/${env}.properties, 生成一个 dict 对象 env_prop
    env_prop = load_properties("{0}envs/{1}.properties".format(source_folder, env))

    # 从redis里读取 三方账号配置
    _r = redis.StrictRedis(host=('192.168.1.30' if env!='CI1' else '127.0.0.1'), port=6379, db=2)
    sec_prop = _r.hgetall('qa_common_account')

    # 逐行读取 ttsd-env.properties, 如果不是注释也不是空行，则split by "=",
    # 依次从 sec_prop > env_prop > qa_common_prop 中读取key，
    # 如果存在就替换原值，并将结果写入到结果文件 dist_file 中。

    with open("{0}/ttsd-env.properties".format(source_folder), "r") as f, open(dist_file, "w") as dist:
        for line in f:
            l = line.strip()
            if l and not l.startswith("#"):
                key_value = l.split("=")
                key = key_value[0].strip()
                value = "=".join(key_value[1:]).strip()
                if sec_prop.has_key(key):
                    value = sec_prop[key]
                elif env_prop.has_key(key):
                    value = env_prop[key]
                elif qa_common_prop.has_key(key):
                    value = qa_common_prop[key]
                dist.write(key + "=" + value + "\n")
            else:
                dist.write(l + "\n")


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
