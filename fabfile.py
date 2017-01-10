from __future__ import with_statement
import os
import time
from fabric.api import *
from fabric.contrib.project import upload_project

config_path = os.getenv('TTSD_CONFIG_PATH', '/workspace/deploy-config')

env.use_ssh_config = True
env.always_use_pty = False
env.ssh_config_path = config_path+'/config'
env.roledefs = {
    'portal': ['beijing', 'shanghai'],
    'pay': ['chongqing', 'tianjin'],
    'worker': ['changchun'],
    'static': ['guangzhou'],
    'redis': ['chengdu'],
    'sms': ['shenzhen'],
    'console': ['shenzhen'],
    'api': ['hongkong', 'macau'],
    'cms': ['wuhan'],
    'activity': ['sanya'],
    'signin': ['xian'],
    'ask': ['taiyuan'],
    'point': ['kunming']
}


def migrate():
    local('/opt/gradle/latest/bin/gradle ttsd-config:processResources')
    local('/opt/gradle/latest/bin/gradle -Pdatabase=aa ttsd-config:flywayMigrate')
    local('/opt/gradle/latest/bin/gradle -Pdatabase=ump_operations ttsd-config:flywayMigrate')
    local('/opt/gradle/latest/bin/gradle -Pdatabase=sms_operations ttsd-config:flywayMigrate')
    local('/opt/gradle/latest/bin/gradle -Pdatabase=job_worker ttsd-config:flywayMigrate')
    local('/opt/gradle/latest/bin/gradle -Pdatabase=edxask ttsd-config:flywayMigrate')
    local('/opt/gradle/latest/bin/gradle -Pdatabase=edxactivity ttsd-config:flywayMigrate')
    local('/opt/gradle/latest/bin/gradle -Pdatabase=edxpoint ttsd-config:flywayMigrate')
    local('/opt/gradle/latest/bin/gradle -Pdatabase=anxin_operations ttsd-config:flywayMigrate')
    local('/opt/gradle/latest/bin/gradle -Pdatabase=edxmessage ttsd-config:flywayMigrate')
    local('/opt/gradle/latest/bin/gradle -Pdatabase=edxlog ttsd-config:flywayMigrate')

def mk_war():
    local('/usr/local/bin/paver jcversion')
    local('/opt/gradle/latest/bin/gradle war renameWar initMQ')

def mk_worker_zip():
    local('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle -Pwork=jpush distZip')
    local('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle -Pwork=repay distZip')
    local('cd ./ttsd-loan-mq-consumer && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-message-mq-consumer && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-point-mq-consumer && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-activity-mq-consumer && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-user-mq-consumer && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-auditLog-mq-consumer && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-diagnosis && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-worker-monitor && /opt/gradle/latest/bin/gradle bootRepackage')


def mk_static_zip():
    local('cd ./ttsd-web/src/main/webapp && zip -r static.zip images/ js/ pdf/ style/ tpl/ robots.txt')
    local('cd ./ttsd-mobile-api/src/main/webapp && zip -r static_api.zip api/')
    local('cd ./ttsd-activity-web/src/main/webapp && zip -r static_activity.zip activity/')
    local('cd ./ttsd-point-web/src/main/webapp && zip -r static_point.zip point/')
    local('cd ./ttsd-ask-web/src/main/webapp && zip -r static_ask.zip ask/')


def mk_signin_zip():
    for i in ('1', '2'):
        local('cp {0}/signin_service/{1}/* ./signin_service/'.format(config_path, i))
        local('cd ./signin_service/ && zip -r signin_{0}.zip *.py *.ini *.yml'.format(i))


def build():
    mk_war()
    mk_worker_zip()
    mk_static_zip()
    mk_signin_zip()


def compile():
    local('/opt/gradle/latest/bin/gradle clean')
    local('/usr/bin/git clean -fd')
    local('/opt/gradle/latest/bin/gradle compileJava')


def check_worker_status():
    local('/opt/gradle/latest/bin/gradle ttsd-worker-monitor:consumerCheck')


@roles('static')
def deploy_static():
    upload_project(local_dir='./ttsd-web/src/main/webapp/static.zip', remote_dir='/workspace')
    upload_project(local_dir='./ttsd-mobile-api/src/main/webapp/static_api.zip', remote_dir='/workspace')
    upload_project(local_dir='./ttsd-activity-web/src/main/webapp/static_activity.zip', remote_dir='/workspace')
    upload_project(local_dir='./ttsd-point-web/src/main/webapp/static_point.zip', remote_dir='/workspace')
    upload_project(local_dir='./ttsd-ask-web/src/main/webapp/static_ask.zip', remote_dir='/workspace')
    with cd('/workspace'):
        sudo('rm -rf static/')
        sudo('unzip static.zip -d static')
        sudo('unzip static_api.zip -d static')
        sudo('unzip static_activity.zip -d static')
        sudo('unzip static_point.zip -d static')
        sudo('unzip static_ask.zip -d static')
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
    upload_project(local_dir='./ttsd-activity-console/war/ROOT.war', remote_dir='/workspace/activity-console')
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
    sudo('service nginx restart')


@roles('worker')
def deploy_worker():
    put(local_path='./ttsd-job-worker/build/distributions/*.zip', remote_path='/workspace/')
    put(local_path='./ttsd-loan-mq-consumer/build/distributions/*.zip', remote_path='/workspace/')
    put(local_path='./ttsd-message-mq-consumer/build/distributions/*.zip', remote_path='/workspace/')
    put(local_path='./ttsd-point-mq-consumer/build/distributions/*.zip', remote_path='/workspace/')
    put(local_path='./ttsd-activity-mq-consumer/build/distributions/*.zip', remote_path='/workspace/')
    put(local_path='./ttsd-user-mq-consumer/build/distributions/*.zip', remote_path='/workspace/')
    put(local_path='./ttsd-auditLog-mq-consumer/build/distributions/*.zip', remote_path='/workspace/')
    put(local_path='./ttsd-diagnosis/build/distributions/*.zip', remote_path='/workspace/')
    put(local_path='./scripts/supervisor/job-worker.ini', remote_path='/etc/supervisord.d/')
    put(local_path='./scripts/logstash/worker.conf', remote_path='/etc/logstash/conf.d/prod.conf')
    sudo('supervisorctl stop all')
    restart_logstash_process()
    with cd('/workspace'):
        sudo('rm -rf ttsd-job-worker-all/')
        sudo('rm -rf ttsd-job-worker-jpush/')
        sudo('rm -rf ttsd-job-worker-repay/')
        sudo('rm -rf ttsd-loan-mq-consumer/')
        sudo('rm -rf ttsd-message-mq-consumer/')
        sudo('rm -rf ttsd-point-mq-consumer/')
        sudo('rm -rf ttsd-activity-mq-consumer/')
        sudo('rm -rf ttsd-user-mq-consumer/')
        sudo('rm -rf ttsd-auditLog-mq-consumer/')
        sudo('rm -rf ttsd-diagnosis/')
        sudo('unzip \*.zip')
        sudo('supervisorctl reload')
        sudo('supervisorctl start all')


@roles('api')
@parallel
def deploy_api():
    sudo('service tomcat stop')
    sudo('rm -rf /opt/tomcat/webapps/ROOT')
    upload_project(local_dir='./ttsd-mobile-api/war/ROOT.war', remote_dir='/opt/tomcat/webapps')
    sudo('service tomcat start')
    sudo('service nginx restart')


@roles('portal')
@parallel
def deploy_web():
    sudo('service tomcat stop')
    sudo('rm -rf /opt/tomcat/webapps/ROOT')
    upload_project(local_dir='./ttsd-web/war/ROOT.war', remote_dir='/opt/tomcat/webapps')
    sudo('service tomcat start')
    sudo('service nginx restart')


@roles('activity')
@parallel
def deploy_activity():
    sudo('service tomcat stop')
    sudo('rm -rf /opt/tomcat/webapps/ROOT')
    upload_project(local_dir='./ttsd-activity-web/war/ROOT.war', remote_dir='/opt/tomcat/webapps')
    sudo('service tomcat start')
    sudo('service nginx restart')


@roles('ask')
@parallel
def deploy_ask():
    sudo('service tomcat stop')
    sudo('rm -rf /opt/tomcat/webapps/ROOT')
    upload_project(local_dir='./ttsd-ask-web/war/ROOT.war', remote_dir='/opt/tomcat/webapps')
    sudo('service tomcat start')
    sudo('service nginx restart')


@roles('signin')
@parallel
def deploy_sign_in():
    for i in ('1', '2'):
        folder_name = 'signin_{0}'.format(i)
        upload_project(local_dir='./signin_service/{0}.zip'.format(folder_name), remote_dir='/workspace')
        with cd('/workspace'):
            sudo('rm -rf {0}'.format(folder_name))
            sudo('unzip {0}.zip -d {0}'.format(folder_name))
        with cd('/workspace/{0}'.format(folder_name)):
            sudo('/usr/local/bin/docker-compose -f prod.yml -p ttsd stop')
            sudo('/usr/local/bin/docker-compose -f prod.yml -p ttsd rm -f')
            sudo('/usr/local/bin/docker-compose -f prod.yml -p ttsd up -d')
    sudo('service nginx restart')


@roles('point')
@parallel
def deploy_point():
    sudo('service tomcat stop')
    sudo('rm -rf /opt/tomcat/webapps/ROOT')
    upload_project(local_dir='./ttsd-point-web/war/ROOT.war', remote_dir='/opt/tomcat/webapps')
    sudo('service tomcat start')
    sudo('service nginx restart')


def deploy_all():
    execute(deploy_static)
    execute(deploy_sign_in)
    execute(deploy_sms)
    execute(deploy_console)
    execute(deploy_pay)
    execute(deploy_worker)
    execute(deploy_api)
    execute(deploy_web)
    execute(deploy_activity)
    execute(deploy_point)
    execute(deploy_ask)


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


def activity():
    pre_deploy()
    execute(deploy_activity)
    execute(deploy_static)


def ask():
    pre_deploy()
    execute(deploy_ask)
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


def pay():
    pre_deploy()
    execute(deploy_pay)


def signin():
    mk_signin_zip()
    execute(deploy_sign_in)


def point():
    pre_deploy()
    execute(deploy_point)
    execute(deploy_static)


def get_7days_before(date_format="%Y-%m-%d"):
    from datetime import timedelta, date
    return (date.today() - timedelta(days=7)).strftime(date_format)


def remove_tomcat_logs():
    iso_date = get_7days_before()
    with cd('/var/log/tomcat'):
        run('rm -f *{0}*.log'.format(iso_date))
        run('rm -f *{0}*.txt'.format(iso_date))


def remove_nginx_logs():
    normal_date = get_7days_before(date_format='%Y%m%d')
    with cd('/var/log/nginx'):
        run('rm -f *{0}.gz'.format(normal_date))


@roles('portal')
@parallel
def remove_web_logs():
    remove_tomcat_logs()
    remove_nginx_logs()


@roles('activity')
@parallel
def remove_activity_logs():
    remove_tomcat_logs()
    remove_nginx_logs()


@roles('ask')
@parallel
def remove_ask_logs():
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
    iso_date = get_7days_before()
    with cd('/var/log/job-worker'):
        run('rm -f *{0}.log'.format(iso_date))


@roles('static')
@parallel
def remove_static_logs():
    remove_nginx_logs()


@roles('signin')
@parallel
def remove_sign_in_logs():
    remove_tomcat_logs()
    remove_nginx_logs()


@roles('point')
@parallel
def remove_point_logs():
    remove_tomcat_logs()
    remove_nginx_logs()


@roles('console')
@parallel
def remove_console_logs():
    iso_date = get_7days_before()
    with cd('/var/log/tuotian/console'):
        run('rm -f *{0}.log'.format(iso_date))
        run('rm -f *{0}.txt'.format(iso_date))

    remove_nginx_logs()


def remove_old_logs():
    """
    Remove logs which was generated 30 days ago
    """
    execute(remove_web_logs)
    execute(remove_activity_logs)
    execute(remove_pay_logs)
    execute(remove_api_logs)
    execute(remove_worker_logs)
    execute(remove_static_logs)
    execute(remove_sign_in_logs)
    execute(remove_point_logs)
    execute(remove_console_logs)


def restart_logstash_process():
    run("ps aux | grep '[l]ogstash' | awk '{print $2}' | xargs kill -9")
    run("rm -rf /var/log/logstash/*")
    run("service logstash start")


@roles('pay')
@parallel
def restart_logstash_service_for_pay():
    """
    Restart logstash service in case it stops pushing logs due to unknow reason
    """
    restart_logstash_process()


@roles('worker')
@parallel
def restart_logstash_service_for_worker():
    """
    Restart logstash service in case it stops pushing logs due to unknow reason
    """
    restart_logstash_process()


@roles('cms')
@parallel
def restart_logstash_service_for_cms():
    """
    Restart logstash service in case it stops pushing logs due to unknow reason
    """
    restart_logstash_process()


@roles('portal')
@parallel
def restart_logstash_service_for_portal():
    """
    Restart logstash service in case it stops pushing logs due to unknow reason
    """
    restart_logstash_process()


@roles('api')
@parallel
def restart_logstash_service_for_api():
    """
    Restart logstash service in case it stops pushing logs due to unknow reason
    """
    restart_logstash_process()


@roles('activity')
@parallel
def restart_logstash_service_for_activity():
    """
    Restart logstash service in case it stops pushing logs due to unknow reason
    """
    restart_logstash_process()


@roles('ask')
@parallel
def restart_logstash_service_for_ask():
    """
    Restart logstash service in case it stops pushing logs due to unknow reason
    """
    restart_logstash_process()


@roles('signin')
@parallel
def restart_logstash_service_for_sign_in():
    """
    Restart logstash service in case it stops pushing logs due to unknow reason
    """
    restart_logstash_process()


@roles('point')
@parallel
def restart_logstash_service_for_point():
    """
    Restart logstash service in case it stops pushing logs due to unknow reason
    """
    restart_logstash_process()


def restart_logstash(service):
    """
    Usage: fab restart_logstash:web
    """
    def get_password():
        with open('/workspace/ci/def', 'rb') as f:
            return f.readline().strip()
    env.password = get_password()
    func = {'web': restart_logstash_service_for_portal, 'api': restart_logstash_service_for_api,
           'pay': restart_logstash_service_for_pay, 'worker': restart_logstash_service_for_worker,
           'cms': restart_logstash_service_for_cms, 'activity': restart_logstash_service_for_activity,
           'point': restart_logstash_service_for_point,
           'signin': restart_logstash_service_for_sign_in, 'ask': restart_logstash_service_for_ask}.get(service)
    execute(func)


ROOT = os.path.abspath(os.path.dirname(__file__))
fab_local_file = os.path.join(ROOT, "fab_local.py")
if os.path.exists(fab_local_file):
    execfile(fab_local_file)