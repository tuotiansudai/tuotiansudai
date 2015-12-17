from __future__ import with_statement
import os
from fabric.api import *
from fabric.contrib.project import upload_project


env.use_ssh_config = True
env.ssh_config_path = '/workspace/v2config/config'
env.roledefs = {
    'db': ['web1'],
    'web': ['web1', 'web2'],
    'portal': ['beijing', 'shanghai'],
    'pay': ['chongqing', 'tianjin'],
    'worker': ['changchun'],
    'static': ['guangzhou'],
    'redis': ['chengdu'],
    'sms': ['shenzhen'],
    'console': ['shenzhen'],
    'api': ['hongkong', 'macau'],
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


def v2migrate():
    local('/opt/gradle/latest/bin/gradle clean')
    local('/opt/gradle/latest/bin/gradle -PconfigPath=/workspace/v2config/default/ -Pdatabase=aa ttsd-service:flywayMigrate')
    local('/opt/gradle/latest/bin/gradle -PconfigPath=/workspace/v2config/default/ -Pdatabase=ump_operations ttsd-service:flywayMigrate')
    local('/opt/gradle/latest/bin/gradle -PconfigPath=/workspace/v2config/default/ -Pdatabase=sms_operations ttsd-service:flywayMigrate')
    local('/opt/gradle/latest/bin/gradle -PconfigPath=/workspace/v2config/default/ -Pdatabase=job_worker ttsd-service:flywayMigrate')


def mk_war():
    local('/usr/local/bin/paver jcversion')
    local('/opt/gradle/latest/bin/gradle ttsd-web:war -PconfigPath=/workspace/v2config/default/')
    local('/opt/gradle/latest/bin/gradle ttsd-pay-wrapper:war -PconfigPath=/workspace/v2config/default/')
    local('/opt/gradle/latest/bin/gradle ttsd-console:war -PconfigPath=/workspace/v2config/default/')
    local('/opt/gradle/latest/bin/gradle ttsd-mobile-api:war -PconfigPath=/workspace/v2config/default/')
    local('/opt/gradle/latest/bin/gradle ttsd-sms-wrapper:war -PconfigPath=/workspace/v2config/default/')


def mk_worker_zip():
    local('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle distZip -PconfigPath=/workspace/v2config/default/')
    local('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle -Prop=invest distZip -PconfigPath=/workspace/v2config/default/')


def mk_static_zip():
    local('cd ./ttsd-web/src/main/webapp && zip -r static.zip images/ js/ pdf/ style/ tpl/')


def v2build():
    mk_war()
    mk_worker_zip()
    mk_static_zip()


def v2compile():
    local('/opt/gradle/latest/bin/gradle clean')
    local('/usr/bin/git clean -fd')
    local('/opt/gradle/latest/bin/gradle compileJava')


@roles('static')
def deploy_static():
    upload_project(local_dir='./ttsd-web/src/main/webapp/static.zip', remote_dir='/workspace')
    with cd('/workspace'):
        sudo('rm -rf static/')
        sudo('unzip static.zip -d static')
        sudo('service nginx restart')


@roles('sms')
def deploy_sms():
    upload_project(local_dir='./ttsd-sms-wrapper/war/ROOT.war', remote_dir='/workspace/sms')
    with cd('/workspace'):
        sudo('/usr/local/bin/docker-compose -f sms.yml -p ttsd stop')
        sudo('/usr/local/bin/docker-compose -f sms.yml -p ttsd rm -f')
        sudo('/usr/local/bin/docker-compose -f sms.yml -p ttsd up -d')


@roles('console')
def deploy_console():
    upload_project(local_dir='./ttsd-console/war/ROOT.war', remote_dir='/workspace/console')
    with cd('/workspace'):
        sudo('/usr/local/bin/docker-compose -f console.yml -p ttsd stop')
        sudo('/usr/local/bin/docker-compose -f console.yml -p ttsd rm -f')
        sudo('/usr/local/bin/docker-compose -f console.yml -p ttsd up -d')


@roles('pay')
@parallel
def deploy_pay():
    sudo('service tomcat stop')
    sudo('rm -rf /opt/tomcat/webapps/ROOT')
    upload_project(local_dir='./ttsd-pay-wrapper/war/ROOT.war', remote_dir='/opt/tomcat/webapps')
    sudo('service tomcat start')


@roles('worker')
def deploy_worker():
    put(local_path='./ttsd-job-worker/build/distributions/*.zip', remote_path='/workspace/')
    sudo('supervisorctl stop all')
    with cd('/workspace'):
        sudo('rm -rf ttsd-job-worker-all/')
        sudo('rm -rf ttsd-job-worker-invest/')
        sudo('unzip \*.zip')
        sudo('supervisorctl start all')


@roles('api')
@parallel
def deploy_api():
    sudo('service tomcat stop')
    sudo('rm -rf /opt/tomcat/webapps/ROOT')
    upload_project(local_dir='./ttsd-mobile-api/war/ROOT.war', remote_dir='/opt/tomcat/webapps')
    sudo('service tomcat start')


@roles('portal')
@parallel
def deploy_web():
    sudo('service tomcat stop')
    sudo('rm -rf /opt/tomcat/webapps/ROOT')
    upload_project(local_dir='./ttsd-web/war/ROOT.war', remote_dir='/opt/tomcat/webapps')
    sudo('service tomcat start')


def deploy_all():
    execute(deploy_static)
    execute(deploy_sms)
    execute(deploy_console)
    execute(deploy_pay)
    execute(deploy_worker)
    execute(deploy_api)
    execute(deploy_web)


def v2deploy():
    v2compile()
    v2migrate()
    v2build()
    deploy_all()


@roles('db')
def migrate():
    with cd('/workspace/tuotian/v1'):
        run('/opt/gradle/latest/bin/gradle clean')
        run('/opt/gradle/latest/bin/gradle flywayMigrate')

ROOT = os.path.abspath(os.path.dirname(__file__))
fab_local_file = os.path.join(ROOT, "fab_local.py")
if os.path.exists(fab_local_file):
    execfile(fab_local_file)