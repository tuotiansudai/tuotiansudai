from __future__ import with_statement
import logging
import os
from fabric.api import *
from fabric.contrib.project import upload_project
from scripts import migrate_db
from scripts import etcd_client

config_path = os.getenv('TTSD_CONFIG_PATH', '/workspace/deploy-config')

env.use_ssh_config = True
env.always_use_pty = False
env.ssh_config_path = config_path + '/config'
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
    'point': ['kunming'],
    'ask-rest': ['shijiazhuang'],
    'anxin': ['shijiazhuang']
}

etcd3 = etcd_client.client('prod')


def migrate():
    migrate_db.migrate('/opt/gradle/latest/bin/gradle', etcd3, local)


def mk_war():
    local('/usr/local/bin/paver jcversion.static_server={0} jcversion'.format(etcd3.get('common.static.server')))
    local('TTSD_ETCD_ENV=prod /opt/gradle/latest/bin/gradle war renameWar initMQ')


def mk_worker_zip():
    local('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-diagnosis && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-worker-monitor && /opt/gradle/latest/bin/gradle bootRepackage')


def mk_mq_consumer():
    local('cd ./ttsd-loan-mq-consumer && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-message-mq-consumer && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-point-mq-consumer && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-activity-mq-consumer && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-user-mq-consumer && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-amount-mq-consumer && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-auditLog-mq-consumer && /opt/gradle/latest/bin/gradle distZip')
    local('cd ./ttsd-email-mq-consumer && /opt/gradle/latest/bin/gradle distZip')


def mk_rest_service():
    local('cd ./ttsd-ask-rest && /opt/gradle/latest/bin/gradle distZip')


def mk_static_zip():
    local('cd ./ttsd-frontend-manage/resources/prod && zip -r static_all.zip *')


def mk_signin_zip():
    local('cp ./ttsd-etcd/src/main/resources/etcd-endpoints.yml ./ttsd-user-rest-service/')
    for i in ('1', '2'):
        local('cp {0}/signin_service/{1}/prod.yml ./ttsd-user-rest-service/'.format(config_path, i))
        local('cd ./ttsd-user-rest-service/ && zip -r signin_{0}.zip *.py *.ini *.yml'.format(i))


def build():
    mk_war()
    mk_worker_zip()
    mk_mq_consumer()
    mk_rest_service()
    mk_static_zip()
    mk_signin_zip()


def compile():
    local('/opt/gradle/latest/bin/gradle clean')
    local('/usr/bin/git clean -fd')
    local('/opt/gradle/latest/bin/gradle compileJava')


def check_worker_status():
    local('TTSD_ETCD_ENV=prod /opt/gradle/latest/bin/gradle ttsd-worker-monitor:consumerCheck')


def clear_worker_status():
    local('TTSD_ETCD_ENV=prod /opt/gradle/latest/bin/gradle ttsd-worker-monitor:clearWorkerMonitorStatus')


@roles('static')
def deploy_static():
    upload_project(local_dir='./ttsd-frontend-manage/resources/prod/static_all.zip', remote_dir='/workspace')
    with cd('/workspace'):
        sudo('rm -rf static/')
        sudo('unzip static_all.zip -d static')
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
    put(local_path='./ttsd-email-mq-consumer/build/distributions/*.zip', remote_path='/workspace/')
    put(local_path='./ttsd-amount-mq-consumer/build/distributions/*.zip', remote_path='/workspace/')
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
        sudo('rm -rf ttsd-email-mq-consumer/')
        sudo('rm -rf ttsd-amount-mq-consumer/')
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
        local("echo sign in start...")
        folder_name = 'signin_{0}'.format(i)
        local('echo sign in start...' + folder_name)
        try:
            local("echo sign in upload")
            upload_project(local_dir='./ttsd-user-rest-service/{0}.zip'.format(folder_name), remote_dir='/workspace')
            logging.info("sign in upload done")
        except Exception as e:
            local("echo " + e.message)
            raise e

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


@roles('ask-rest')
def deploy_ask_rest():
    upload_project(local_dir='./ttsd-ask-rest/build/distributions/ttsd-ask-rest.zip',
                   remote_dir='/workspace/rest-service')
    with cd('/workspace/rest-service'):
        sudo('/usr/local/bin/docker-compose -f ask-rest.yml stop')
        sudo('/usr/local/bin/docker-compose -f ask-rest.yml rm -f')
        sudo('rm -rf ttsd-ask-rest')
        sudo('unzip ttsd-ask-rest.zip')
        sudo('/usr/local/bin/docker-compose -f ask-rest.yml up -d')


@roles('anxin')
def deploy_anxin():
    upload_project(local_dir='./ttsd-anxin-wrapper/war/ROOT.war', remote_dir='/workspace/anxin/war')
    with cd('/workspace/anxin'):
        sudo('/usr/local/bin/docker-compose -f anxin.yml stop')
        sudo('/usr/local/bin/docker-compose -f anxin.yml rm -f')
        sudo('rm -rf ROOT')
        sudo('/usr/local/bin/docker-compose -f anxin.yml up -d')


def deploy_all():
    execute(deploy_sign_in)
    execute(deploy_static)
    execute(deploy_sms)
    execute(deploy_console)
    execute(deploy_pay)
    execute(deploy_worker)
    execute(deploy_api)
    execute(deploy_web)
    execute(deploy_activity)
    execute(deploy_point)
    execute(deploy_anxin)


def pre_deploy():
    compile()
    migrate()
    build()


def all():
    # pre_deploy()
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
    execute(deploy_ask_rest)
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
        run('rm -f *{0}*'.format(iso_date))


def remove_nginx_logs():
    normal_date = get_7days_before(date_format='%Y%m%d')
    with cd('/var/log/nginx'):
        run('rm -f *{0}.gz'.format(normal_date))


def remove_logs_before_7days(log_folder):
    iso_date = get_7days_before()
    with cd(log_folder):
        run('rm -f *{0}*'.format(iso_date))


@roles('worker')
@parallel
def remove_worker_logs():
    remove_logs_before_7days('/var/log/job-worker')


@roles('static')
@parallel
def remove_static_logs():
    remove_nginx_logs()


@roles('signin')
@parallel
def remove_sign_in_logs():
    for item in ('1', '2'):
        folder = '/var/log/signin_{}'.format(item)
        remove_logs_before_7days(folder)
    remove_nginx_logs()


@roles('ask-rest')
@parallel
def remove_ask_rest_logs():
    remove_logs_before_7days('/var/log/tuotian/ask-rest')
    remove_nginx_logs()


@roles('anxin')
@parallel
def remove_anxin_logs():
    remove_logs_before_7days('/var/log/tuotian/anxin')
    remove_nginx_logs()


@roles('cms')
@parallel
def remove_cms_logs():
    remove_logs_before_7days('/var/log/tuotian/cms')
    remove_nginx_logs()


@roles('console')
@parallel
def remove_admin_and_sms_logs():
    for folder in ('activity-console', 'console', 'sms', 'cms'):
        log_path = '/var/log/tuotian/{}'.format(folder)
        remove_logs_before_7days(log_path)
    remove_nginx_logs()


@roles('portal', 'api', 'pay', 'ask', 'activity', 'point')
@parallel
def remove_nginx_and_tomcat_logs():
    remove_tomcat_logs()
    remove_nginx_logs()


def remove_old_logs():
    """
    Remove logs which was generated 7 days ago
    """
    execute(remove_nginx_and_tomcat_logs)
    execute(remove_ask_rest_logs)
    execute(remove_anxin_logs)
    execute(remove_cms_logs)
    execute(remove_worker_logs)
    execute(remove_static_logs)
    execute(remove_sign_in_logs)
    execute(remove_admin_and_sms_logs)


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


@roles('ask', 'ask-rest')
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
