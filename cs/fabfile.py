from __future__ import with_statement
from fabric.api import *


env.use_ssh_config = True
env.ssh_config_path = '/workspace/v2config/config'
env.roledefs = {
    'cs': ['web1'],
}


@roles('cs')
def run():
    with cd('/workspace/tuotian/cs'):
        run('/usr/bin/git pull')
        sudo('/usr/local/bin/docker-compose -f cs.yml stop')
        sudo('/usr/local/bin/docker-compose -f cs.yml rm -f')
        sudo('/usr/local/bin/docker-compose -f cs.yml up -d')


def deploy():
    try:
        ci_file = open('/workspace/ci/abc', 'rb')
        pwd = ci_file.readline().strip()
        env.password = pwd
        ci_file.close()
        execute(run)
    except IOError as e:
        print e

