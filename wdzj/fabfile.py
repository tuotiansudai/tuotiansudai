from __future__ import with_statement
from fabric.api import *


config_path = os.getenv('TTSD_CONFIG_PATH', '/workspace/deploy-config')

env.use_ssh_config = True
env.ssh_config_path = config_path+'/config'
env.roledefs = {
    'wdzj': ['web1'],
}


def pull():
    with cd('/workspace/tuotian'):
        sudo('chmod -R g=u .')
        run('/usr/bin/git pull')


def start_docker_container():
    with cd('/workspace/tuotian/wdzj'):
        sudo('/usr/local/bin/docker-compose -f wdzj.yml stop')
        sudo('/usr/local/bin/docker-compose -f wdzj.yml rm -f')
        sudo('/usr/local/bin/docker-compose -f wdzj.yml up -d')


@roles('wdzj')
def pull_and_start_container():
    pull()
    start_docker_container()


def deploy():
    try:
        ci_file = open('/workspace/ci/abc', 'rb')
        pwd = ci_file.readline().strip()
        env.password = pwd
        ci_file.close()
        execute(pull_and_start_container)
    except IOError as e:
        print e

