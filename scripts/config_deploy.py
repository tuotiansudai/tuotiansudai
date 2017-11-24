# encoding=utf-8
import redis


def deploy(etcd, env, pay_fake):
    # 读取QA环境公用配置 ttsd-env-QA-common.properties 生成一个 dict 对象 qa_common_prop
    qa_common_prop = load_properties("./ttsd-config/src/main/resources/envs/QA-common.properties")

    # 读取每个环境特有的配置 envs/${env}.properties, 生成一个 dict 对象 specified_qa_prop
    specified_qa_prop = load_properties("./ttsd-config/src/main/resources/envs/{0}.properties".format(env))

    # 从redis里读取 三方账号配置
    _r = redis.StrictRedis(host=('192.168.1.30' if env != 'CI1' else '127.0.0.1'), port=6379, db=2)
    sec_prop = _r.hgetall('qa_common_account')

    # 逐行读取 ttsd-env.properties, 如果不是注释也不是空行，则split by "=",
    # 依次从 build_params > sec_prop > specified_qa_prop > qa_common_prop 中读取key，
    # 如果存在就替换原值，并将结果写入到结果文件 dist_file 中。

    with open("./ttsd-config/src/main/resources/ttsd-env.properties", "r") as f:
        for line in f:
            striped_line = line.strip()
            if striped_line and not striped_line.startswith("#"):
                key_value = striped_line.split("=")
                key = key_value[0].strip()
                value = (sec_prop[key] + ' ' if key in sec_prop else False) \
                        or (specified_qa_prop[key] + ' ' if key in specified_qa_prop else False) \
                        or (qa_common_prop[key] + ' ' if key in qa_common_prop else False) \
                        or '='.join(key_value[1:]).strip()
                etcd.put(key, value.strip())
                print 'put etcd {0}={1}'.format(key, value.strip())
        if pay_fake is not None:
            etcd.put('pay.fake', pay_fake)

    with open("./ttsd-config/src/main/resources/ttsd-biz.properties", "r") as f:
        for line in f:
            striped_line = line.strip()
            if striped_line and not striped_line.startswith("#"):
                key_value = striped_line.split("=")
                key = key_value[0].strip()
                value = '='.join(key_value[1:]).strip()
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
