from __future__ import with_statement
import os
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


def stop_tomcat():
    sudo('kill -9 `cat /var/run/tomcat6.pid`')
    sudo('rm /var/run/tomcat6.pid')
    sudo('rm /var/lock/subsys/tomcat6')


def deploy_tomcat():
    with cd('/workspace/tuotian/v1'):
        stop_tomcat()
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

ROOT = os.path.abspath(os.path.dirname(__file__))
fab_local_file = os.path.join(ROOT, "fab_local.py")
if os.path.exists(fab_local_file):
    execfile(fab_local_file)