import os
from paver.shell import sh
import config_deploy
import etcd_client


class Deployment(object):
    _config_path = os.getenv('TTSD_CONFIG_PATH', '/workspace/deploy-config')
    _gradle = '/usr/local/bin/gradle'
    _dockerCompose = '/usr/local/bin/docker-compose'
    _paver = 'paver'

    def __init__(self, env='DEV'):
        self.env = env
        self.etcd = etcd_client.client(env)

    def deploy(self, type):
        print type
        getattr(self, type)()

    def all(self):
        self.clean()
        self.config_file()
        self.clean_class()
        self.migrate()
        self.compile()
        self.mk_war()
        self.build_and_unzip_worker()
        self.build_mq_consumer()
        self.build_rest_service()
        self.build_worker_monitor()
        self.mk_static_package()
        self.init_docker()

    def only_web(self):
        self.clean()
        self.config_file()
        self.clean_class()
        self.mk_war(('ttsd-web',))
        self.migrate()
        self.mk_static_package()
        self.init_docker(('static-server', 'web', 'nginx-server_web'))

    def only_console(self):
        self.clean()
        self.config_file()
        self.clean_class(('ttsd-console', 'ttsd-activity-console'))
        self.mk_war(('ttsd-console', 'ttsd-activity-console'))
        self.migrate()
        self.init_docker(('console', 'activity-console', 'nginx-server_console'))

    def only_activity(self):
        self.clean()
        self.config_file()
        self.clean_class()
        self.mk_war(('ttsd-activity-web',))
        self.migrate()
        self.mk_static_package()
        self.init_docker(('static-server', 'activity'))

    def only_point(self):
        self.clean()
        self.config_file()
        self.clean_class(('ttsd-point-web',))
        self.mk_war(('ttsd-point-web',))
        self.migrate()
        self.mk_static_package()
        self.init_docker(('static-server', 'point'))

    def only_ask(self):
        self.clean()
        self.config_file()
        self.clean_class(('ttsd-ask-web', 'ttsd-ask-rest'))
        self.compile(('ttsd-ask-rest',))
        self.mk_war(('ttsd-ask-web',))
        self.build_rest_service()
        self.migrate()
        self.mk_static_package()
        self.init_docker(('static-server', 'ask', 'ask-rest-service'))

    def only_api(self):
        self.clean()
        self.config_file()
        self.clean_class(('ttsd-mobile-api',))
        self.mk_war(('ttsd-mobile-api',))
        self.migrate()
        self.mk_static_package()
        self.init_docker(('static-server', 'mobile-api'))

    def only_pay(self):
        self.clean()
        self.config_file()
        self.clean_class(('ttsd-pay-wrapper',))
        self.mk_war(('ttsd-pay-wrapper',))
        self.migrate()
        self.init_docker(('pay-wrapper',))

    def only_sms(self):
        self.clean()
        self.config_file()
        self.clean_class(('ttsd-sms-wrapper',))
        self.mk_war(('ttsd-sms-wrapper',))
        self.migrate()
        self.init_docker(('sms-wrapper',))

    def only_worker(self):
        self.clean()
        self.config_file()
        self.clean_class(('ttsd-job-worker',
                          'ttsd-loan-mq-consumer',
                          'ttsd-message-mq-consumer',
                          'ttsd-point-mq-consumer',
                          'ttsd-activity-mq-consumer',
                          'ttsd-user-mq-consumer',
                          'ttsd-auditLog-mq-consumer',
                          'ttsd-email-mq-consumer',
                          'ttsd-amount-mq-consumer'))
        self.mk_worker_zip()
        self.init_docker(('worker-all', 'auditLog-mq-consumer',
                          'loan-mq-consumer',
                          'message-mq-consumer',
                          'email-mq-consumer',
                          'point-mq-consumer',
                          'activity-mq-consumer',
                          'user-mq-consumer',
                          'amount-mq-consumer',))

    def only_sign_in(self):
        self.mk_war(('sign_in',))
        self.init_docker(('user-rest-service',))

    def clean(self):
        print "Cleaning..."
        print self._gradle
        sh('/usr/bin/git clean -fd', ignore_error=True)

    def config_file(self):
        print "Generate config file..."
        config_deploy.deploy(self.etcd, self.env)

    def migrate(self):
        from scripts import migrate_db
        migrate_db.migrate(self._gradle, self.etcd, sh)

    def clean_class(self, targets=None):
        print "clean_class..."
        if not targets:
            sh('TTSD_ETCD_ENV={0} {1} clean'.format(self.env, self._gradle))
            return
        for target in targets:
            sh('TTSD_ETCD_ENV={0} {1} {2}:clean'.format(self.env, self._gradle, target))

    def compile(self, targets=None):
        print "compile..."
        if not targets:
            sh('TTSD_ETCD_ENV={0} {1} compileJava'.format(self.env, self._gradle))
            return
        for target in targets:
            sh('TTSD_ETCD_ENV={0} {1} {2}:compileJava'.format(self.env, self._gradle, target))

    def mk_war(self, targets=None):
        print "mk_war..."
        if not targets:
            sh('TTSD_ETCD_ENV={0} {1} bootWar war renameWar'.format(self.env, self._gradle))
            sh('cp {0}/signin_service/settings_local.py ./ttsd-user-rest-service/'.format(self._config_path))
            return
        for target in targets:
            if target == 'sign_in':
                sh('cp {0}/signin_service/settings_local.py ./ttsd-user-rest-service/'.format(self._config_path))
            else:
                sh('TTSD_ETCD_ENV={0} {1} {2}:war renameWar'.format(self.env, self._gradle, target))

    def mk_worker_zip(self):
        self.build_and_unzip_worker()
        self.build_diagnosis()
        self.build_worker_monitor()
        self.build_mq_consumer()

    def build_and_unzip_worker(self):
        print "Making worker build..."
        sh('cd ./ttsd-job-worker && {0} distZip'.format(self._gradle))
        sh('cd ./ttsd-job-worker/build/distributions && unzip \*.zip')

    def build_mq_consumer(self):
        print "Making MQ consumer build..."
        sh('TTSD_ETCD_ENV={0} {1} initMQ '.format(self.env, self._gradle))
        sh('cd ./ttsd-loan-mq-consumer && {0} distZip'.format(self._gradle))
        sh('cd ./ttsd-loan-mq-consumer/build/distributions && unzip \*.zip')
        sh('cd ./ttsd-message-mq-consumer && {0} distZip'.format(self._gradle))
        sh('cd ./ttsd-message-mq-consumer/build/distributions && unzip \*.zip')
        sh('cd ./ttsd-point-mq-consumer && {0} distZip'.format(self._gradle))
        sh('cd ./ttsd-point-mq-consumer/build/distributions && unzip \*.zip')
        sh('cd ./ttsd-activity-mq-consumer && {0} distZip'.format(self._gradle))
        sh('cd ./ttsd-activity-mq-consumer/build/distributions && unzip \*.zip')
        sh('cd ./ttsd-user-mq-consumer && {0} distZip'.format(self._gradle))
        sh('cd ./ttsd-user-mq-consumer/build/distributions && unzip \*.zip')
        sh('cd ./ttsd-auditLog-mq-consumer && {0} distZip'.format(self._gradle))
        sh('cd ./ttsd-auditLog-mq-consumer/build/distributions && unzip \*.zip')
        sh('cd ./ttsd-email-mq-consumer && {0} distZip'.format(self._gradle))
        sh('cd ./ttsd-email-mq-consumer/build/distributions && unzip \*.zip')
        sh('cd ./ttsd-amount-mq-consumer && {0} distZip'.format(self._gradle))
        sh('cd ./ttsd-amount-mq-consumer/build/distributions && unzip \*.zip')

    def build_rest_service(self):
        print "Making rest services build..."
        sh('cd ./ttsd-ask-rest && {0} distZip'.format(self._gradle))
        sh('cd ./ttsd-ask-rest/build/distributions && unzip \*.zip')

    def build_worker_monitor(self):
        print "Making diagnosis build..."
        sh('cd ./ttsd-worker-monitor && {0} bootJar'.format(self._gradle))

    def mk_static_package(self):
        print "Making static package..."
        sh('cd ./ttsd-web/build && rm -rf *.zip static')
        self.jcversion()
        sh('cd ./ttsd-web/src/main/webapp && zip -r static.zip images/ js/ pdf/ style/ tpl/ robots.txt')
        sh('mv ./ttsd-web/src/main/webapp/static.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static.zip -d static')

        sh('cd ./ttsd-frontend-manage/resources/prod && zip -r static_all.zip *')
        sh('mv ./ttsd-frontend-manage/resources/prod/static_all.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static_all.zip -d static')

    def init_docker(self, targets=None):
        print "Initialing docker..."
        import platform
        sudoer = 'sudo' if 'centos' in platform.platform() else ''
        self._remove_old_container(sudoer, targets)
        self._start_new_container(sudoer, targets)

    def _remove_old_container(self, suoder, targets):
        if not targets:
            sh('{0} TTSD_ETCD_ENV={1} {2} -f dev.yml stop '.format(suoder, self.env, self._dockerCompose))
            sh('{0} /bin/bash -c "export COMPOSE_HTTP_TIMEOUT=300 && TTSD_ETCD_ENV={1} {2} -f dev.yml rm -f"'.format(
                suoder,
                self.env,
                self._dockerCompose))
            return

        for target in targets:
            sh('{0} TTSD_ETCD_ENV={1} {2} -f dev.yml stop {3}'.format(suoder, self.env, self._dockerCompose, target))
            sh('{0} /bin/bash -c "export COMPOSE_HTTP_TIMEOUT=300 && TTSD_ETCD_ENV={1} {2} -f dev.yml  rm -f {3}"'
               .format(suoder, self.env, self._dockerCompose, target))

    def _start_new_container(self, sudoer, targets):
        if not targets:
            sh('{0} /bin/bash -c "export COMPOSE_HTTP_TIMEOUT=300 && TTSD_ETCD_ENV={1} {2} -f dev.yml up -d"'.format(
                sudoer,
                self.env,
                self._dockerCompose))
            return
        for target in targets:
            sh('{0} /bin/bash -c "export COMPOSE_HTTP_TIMEOUT=300 && TTSD_ETCD_ENV={1} {2} -f dev.yml up -d {3}"'
               .format(sudoer, self.env, self._dockerCompose, target))

    def jcversion(self):
        print "Starting jcmin..."
        sh('{0} jcversion.static_server={1} jcversion'.format(self._paver, self.etcd.get('common.static.server')))
