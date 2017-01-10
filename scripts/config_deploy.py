# encoding=utf-8
import os

def deploy(source_folder, dist_file):
    # 读取ttsd-env-QA-common.properties 生成一个 dict 对象 qa_common_prop
    qa_common_prop = load_properties("{0}envs/QA-common.properties".format(source_folder))

    # 读取当前系统环境变量 env
    env = os.getenv('env')
    if not env:
        print "Fail: system environment variable 'env' not assigned!"
        return

    # 读取 ttsd-env-${env}.properties, 生成一个 dict 对象 env_prop
    env_prop = load_properties("{0}envs/{1}.properties".format(source_folder, env))

    # 逐行读取 ttsd-env.properties, 如果不是注释也不是空行，则split by "=", 依次从 qa_common_prop 和 ${env}_prop 中读取key，
    # 如果存在就替换原值，并将结果写入到结果文件 ttsd-env-dist.properties中。
    dist = open(dist_file, "w")
    with open("{0}/ttsd-env.properties".format(source_folder), "r") as f:
        for line in f:
            l = line.strip()
            if l and not l.startswith("#"):
                key_value = l.split("=")
                key = key_value[0].strip()
                value = "=".join(key_value[1:]).strip()
                if env_prop.has_key(key):
                    value = env_prop[key]
                elif qa_common_prop.has_key(key):
                    value = qa_common_prop[key]
                dist.write(key + "=" + value + "\n")
            else:
                dist.write(l + "\n")
    f.close()
    dist.close()


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
    f.close()
    return props
