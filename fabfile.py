from __future__ import with_statement
import os
from fabric.api import *
from fabric.contrib.project import upload_project


env.use_ssh_config = True
env.ssh_config_path = '/workspace/v2config/config'
env.roledefs = {
    'portal': ['beijing', 'shanghai'],
    'pay': ['chongqing', 'tianjin'],
    'worker': ['changchun'],
    'static': ['guangzhou'],
    'redis': ['chengdu'],
    'sms': ['shenzhen'],
    'console': ['shenzhen'],
    'api': ['hongkong', 'macau'],
}


def migrate():
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
    local('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle  distZip -PconfigPath=/workspace/v2config/default/')
    local('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle  -Pwork=invest distZip -PconfigPath=/workspace/v2config/default/')
    local('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle  -Pwork=jpush distZip -PconfigPath=/workspace/v2config/default/')


def mk_static_zip():
    local('cd ./ttsd-web/src/main/webapp && zip -r static.zip images/ js/ pdf/ style/ tpl/')


def build():
    mk_war()
    mk_worker_zip()
    mk_static_zip()


def compile():
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
        sudo('rm -rf ttsd-job-worker-jpush/')
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


def pre_deploy():
    compile()
    migrate()
    build()


def all():
    pre_deploy()
    deploy_all()


def web():
    pre_deploy()
    execute(deploy_web)
    execute(deploy_static)


def console():
    pre_deploy()
    execute(deploy_console)


def api():
    pre_deploy()
    execute(deploy_api)


def sms():
    pre_deploy()
    execute(deploy_sms)


def worker():
    pre_deploy()
    execute(deploy_worker)


def get_30days_before(date_format="%Y-%m-%d"):
    from datetime import timedelta, date
    return (date.today() - timedelta(days=30)).strftime(date_format)


def remove_tomcat_logs():
    iso_date = get_30days_before()
    with cd('/var/log/tomcat'):
        run('rm -f *{0}.log'.format(iso_date))
        run('rm -f *{0}.txt'.format(iso_date))


def remove_nginx_logs():
    normal_date = get_30days_before(date_format='%Y%m%d')
    with cd('/var/log/nginx'):
        run('rm -f *{0}.gz'.format(normal_date))


@roles('portal')
@parallel
def remove_web_logs():
    remove_tomcat_logs()
    remove_nginx_logs()


@roles('pay')
@parallel
def remove_pay_logs():
    remove_tomcat_logs()
    remove_nginx_logs()


@roles('api')
@parallel
def remove_api_logs():
    remove_tomcat_logs()
    remove_nginx_logs()


@roles('worker')
@parallel
def remove_worker_logs():
    iso_date = get_30days_before()
    with cd('/var/log/job-worker'):
        run('rm -f *{0}.log'.format(iso_date))


@roles('static')
@parallel
def remove_static_logs():
    remove_nginx_logs()


def remove_old_logs():
    """
    Remove logs which was generated 30 days ago
    """
    execute(remove_web_logs)
    execute(remove_pay_logs)
    execute(remove_api_logs)
    execute(remove_worker_logs)
    execute(remove_static_logs)


ROOT = os.path.abspath(os.path.dirname(__file__))
fab_local_file = os.path.join(ROOT, "fab_local.py")
if os.path.exists(fab_local_file):
    execfile(fab_local_file)