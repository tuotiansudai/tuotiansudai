from __future__ import with_statement
from fabric.api import *


env.use_ssh_config = True
env.roledefs = {
    'db': ['web1'],
    'web': ['web1', 'web2'],
}


@roles('web')
def pull():
    with cd('/workspace/tuotian'):
        sudo('chmod -R g=u .')
        run('git checkout master')
        run('git pull')


def mkwar():
    with cd('/workspace/tuotian/v1'):
        run('/opt/gradle/latest/bin/gradle clean')
        run('/opt/gradle/latest/bin/gradle war')


def deploy_tomcat():
    with cd('/workspace/tuotian/v1'):
        sudo('service tomcat6 stop')
        sudo('rm -rf /usr/share/tomcat6/webapps/ROOT')
        sudo('cp war/ROOT.war /usr/share/tomcat6/webapps/')
        sudo('service tomcat6 start')


@roles('web')
# @parallel
def build():
    pull()
    mkwar()
    deploy_tomcat()


def deploy():
    """
        Usage: fab deploy -p password --show=debug
    """
    execute(pull)
    execute(migrate)
    execute(build)


@roles('db')
def migrate():
    with cd('/workspace/tuotian/v1'):
        run('/opt/gradle/latest/bin/gradle clean')
        run('/opt/gradle/latest/bin/gradle flywayMigrate')
