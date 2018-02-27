import os
import platform
from paver.shell import sh

import config_deploy
import etcd_client


def lifecycle(scope_list):
    def scope_check(func):
        def __wrapper(self, *args, **kwargs):
            if func and not all(scope in self.skip_steps for scope in scope_list):
                func(self, *args, **kwargs)
        return __wrapper
    return scope_check


class Deployment(object):
    _config_path = os.getenv('TTSD_CONFIG_PATH', '/workspace/deploy-config')
    _gradle = '/opt/gradle/latest/bin/gradle'
    _dockerCompose = '/usr/local/bin/docker-compose'
    _paver = '/usr/bin/paver'
    _sudoer = 'sudo' if 'centos' in platform.platform() else ''

    """
    all scopes : [ config, frontend, migrate, worker, sms, sign_in, pay, point, ask, activity, console, api, web, monitor ]
    """

    def __init__(self, env='DEV', pay_fake=None, skip_steps=None):
        self.env = env
        self.pay_fake = pay_fake
        self.etcd = etcd_client.client(env)
        self.skip_steps = skip_steps

    def deploy(self):
        self.clean()
        self.config_file()
        self.update_signin_config()
        self.jcversion()
        self.migrate()
        self.init_mq()
        self.make_war()
        self.build_and_unzip_worker()
        self.build_mq_consumer()
        self.build_ask_rest_service()
        self.build_diagnosis()
        self.build_worker_monitor()
        self.mk_static_package()
        self.restart_docker()

    @lifecycle(['frontend'])
    def clean(self):
        print "Cleaning..."
        sh('/usr/bin/git clean -fd', ignore_error=True)

    @lifecycle(['config'])
    def config_file(self):
        print "Generate config file..."
        config_deploy.deploy(self.etcd, self.env, self.pay_fake)

    @lifecycle(['migrate'])
    def migrate(self):
        print "Migrating..."
        from scripts import migrate_db
        migrate_db.migrate(self._gradle, self.etcd, sh)

    @lifecycle(['worker', 'sign_in', 'pay', 'point', 'activity', 'console', 'api', 'web'])
    def init_mq(self):
        print "Compiling..."
        sh('TTSD_ETCD_ENV={0} {1} initMQ'.format(self.env, self._gradle))

    def make_war(self):
        self.make_war_activity()
        self.make_war_anxin()
        self.make_war_ask()
        self.make_war_console()
        self.make_war_mobile()
        self.make_war_pay()
        self.make_war_point()
        self.make_war_sms()
        self.make_war_web()

    @lifecycle(['activity'])
    def make_war_activity(self):
        self._make_war_of(['ttsd-activity-console', 'ttsd-activity-web'])

    @lifecycle(['web', 'api', 'console', 'pay'])
    def make_war_anxin(self):
        self._make_war_of(['ttsd-anxin-wrapper'])

    @lifecycle(['ask'])
    def make_war_ask(self):
        self._make_war_of(['ttsd-ask-web'])

    @lifecycle(['console'])
    def make_war_console(self):
        self._make_war_of(['ttsd-console'])

    @lifecycle(['api'])
    def make_war_mobile(self):
        self._make_war_of(['ttsd-mobile-api'])

    @lifecycle(['pay'])
    def make_war_pay(self):
        self._make_war_of(['ttsd-pay-wrapper'])

    @lifecycle(['point'])
    def make_war_point(self):
        self._make_war_of(['ttsd-point-web'])

    @lifecycle(['sms'])
    def make_war_sms(self):
        self._make_war_of(['ttsd-sms-wrapper'])

    @lifecycle(['web'])
    def make_war_web(self):
        self._make_war_of(['ttsd-web'])

    @lifecycle(['sign_in'])
    def update_signin_config(self):
        sh('cp {0}/signin_service/settings_local.py ./ttsd-user-rest-service/'.format(self._config_path))

    @lifecycle(['worker'])
    def build_and_unzip_worker(self):
        print "Making worker build..."
        self._make_zip_package_of(['ttsd-job-worker'])

    @lifecycle(['worker'])
    def build_mq_consumer(self):
        print "Making MQ consumer build..."
        self._make_zip_package_of(['ttsd-loan-mq-consumer',
                                   'ttsd-message-mq-consumer',
                                   'ttsd-point-mq-consumer',
                                   'ttsd-activity-mq-consumer',
                                   'ttsd-user-mq-consumer',
                                   'ttsd-auditLog-mq-consumer',
                                   'ttsd-email-mq-consumer',
                                   'ttsd-amount-mq-consumer',
                                   ])

    @lifecycle(['ask'])
    def build_ask_rest_service(self):
        print "Making ask rest services build..."
        self._make_zip_package_of(['ttsd-ask-rest'])

    @lifecycle(['monitor'])
    def build_diagnosis(self):
        print "Making diagnosis build..."
        self._make_zip_package_of(['ttsd-diagnosis'])

    @lifecycle(['monitor'])
    def build_worker_monitor(self):
        print "Making worker build..."
        sh('cd ./ttsd-worker-monitor && {0} clean bootRepackage'.format(self._gradle))

    @lifecycle(['frontend'])
    def mk_static_package(self):
        print "Making static package..."
        sh('cd ./ttsd-web/src/main/webapp && zip -r static.zip images/ js/ pdf/ style/ tpl/ robots.txt')
        sh('mv ./ttsd-web/src/main/webapp/static.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static.zip -d static')

        sh('cd ./ttsd-frontend-manage/resources/prod && zip -r static_all.zip *')
        sh('mv ./ttsd-frontend-manage/resources/prod/static_all.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static_all.zip -d static')

    @lifecycle(['frontend'])
    def jcversion(self):
        print "Starting jcmin..."
        sh('{0} jcversion.static_server={1} jcversion'.format(self._paver, self.etcd.get('common.static.server')))

    def restart_docker(self):
        print "Initialing docker..."
        cmd = '{0} {1} -f dev.yml stop && ' \
              '{0} /bin/bash -c "export COMPOSE_HTTP_TIMEOUT=300 && {1} -f dev.yml rm -f " && ' \
              '{0} /bin/bash -c "export COMPOSE_HTTP_TIMEOUT=300 && TTSD_ETCD_ENV={2} {1} -f dev.yml up -d"'.format(
            self._sudoer, self._dockerCompose, self.env)
        sh(cmd)

    def _make_war_of(self, modules):
        cmd = 'cd {{0}} && TTSD_ETCD_ENV={0} {1} clean war renameWar'.format(self.env, self._gradle)
        self._batch_cmd(cmd, modules)

    def _make_zip_package_of(self, modules):
        cmd = 'cd ./{{0}} && rm -rf build/distributions && {0} clean distZip && cd build/distributions && unzip \*.zip'.format(
            self._gradle)
        self._batch_cmd(cmd, modules)

    def _batch_cmd(self, cmd, targets):
        for target in targets:
            sh(cmd.format(target))
