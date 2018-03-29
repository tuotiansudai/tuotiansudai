import os
from paver.shell import sh
import config_deploy
import etcd_client


class Deployment(object):
    _config_path = os.getenv('TTSD_CONFIG_PATH', '/workspace/deploy-config')
    _gradle = '/opt/gradle/latest/bin/gradle'
    _dockerCompose = '/usr/local/bin/docker-compose'
    _paver = '/usr/bin/paver'
    _dev_yml = 'scripts/dev_yml/{}.yml'

    def __init__(self, env='DEV', pay_fake=None):
        self.env = env
        self.pay_fake = pay_fake
        self.etcd = etcd_client.client(env)

    def deploy(self, type):
        print type
        getattr(self, type)()

        # self.clean()
        # self.config_file()
        # self.jcversion()
        # self.migrate()
        # self.compile()
        # self.build_and_unzip_worker()
        # self.build_mq_consumer()
        # self.build_rest_service()
        # self.build_diagnosis()
        # self.build_worker_monitor()
        # self.mk_static_package()
        # self.init_docker()

    def only_web(self):
        print '--------------------clean begin'
        self.clean()
        print '--------------------clean end'
        self.config_file()
        print '--------------------config_file end'
        self.compile(('ttsd-web',))
        print '--------------------compile end'
        self.migrate()
        print '--------------------migrate end'
        self.mk_static_package()
        print '--------------------mk static end'
        self.init_docker('web')
        print '--------------------docker end'

    def clean(self):
        print "Cleaning..."
        print self._gradle
        sh('/usr/bin/git clean -fd', ignore_error=True)

    def config_file(self):
        print "Generate config file..."
        config_deploy.deploy(self.etcd, self.env, self.pay_fake)

    def migrate(self):
        from scripts import migrate_db
        migrate_db.migrate(self._gradle, self.etcd, sh)

    def compile(self, targets=None):
        print "Compiling..."
        sh('TTSD_ETCD_ENV={0} {1} clean initMQ '.format(self.env, self._gradle))
        if not targets:
            sh('TTSD_ETCD_ENV={0} {1} war renameWar'.format(self.env, self._gradle))
            return
        for target in targets:
            sh('TTSD_ETCD_ENV={0} {1} {2}:war renameWar'.format(self.env, self._gradle, target))
            if target == 'sign_in':
                sh('cp {0}/signin_service/settings_local.py ./ttsd-user-rest-service/'.format(self._config_path))

    def build_and_unzip_worker(self):
        print "Making worker build..."
        sh('cd ./ttsd-job-worker && {0} distZip'.format(self._gradle))
        sh('cd ./ttsd-job-worker/build/distributions && unzip \*.zip')

    def build_mq_consumer(self):
        print "Making MQ consumer build..."
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

    def build_diagnosis(self):
        print "Making diagnosis build..."
        sh('cd ./ttsd-diagnosis && {0} distZip'.format(self._gradle))
        sh('cd ./ttsd-diagnosis/build/distributions && unzip \*.zip')

    def build_worker_monitor(self):
        print "Making diagnosis build..."
        sh('cd ./ttsd-worker-monitor && {0} bootRepackage'.format(self._gradle))

    def mk_static_package(self):
        print "Making static package..."
        self.jcversion()
        sh('cd ./ttsd-web/src/main/webapp && zip -r static.zip images/ js/ pdf/ style/ tpl/ robots.txt')
        sh('mv ./ttsd-web/src/main/webapp/static.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static.zip -d static')

        sh('cd ./ttsd-frontend-manage/resources/prod && zip -r static_all.zip *')
        sh('mv ./ttsd-frontend-manage/resources/prod/static_all.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static_all.zip -d static')

    def init_docker(self, target='dev'):
        print "Initialing docker..."
        import platform
        sudoer = 'sudo' if 'centos' in platform.platform() else ''
        self._remove_old_container(sudoer, target)
        self._start_new_container(sudoer, target)

    def _remove_old_container(self, suoder, target):
        sh('{0} {1} -f {2} stop'.format(suoder, self._dockerCompose, _dev_yml.format(target)))
        sh('{0} /bin/bash -c "export COMPOSE_HTTP_TIMEOUT=300 && {1} -f {2} rm -f"'.format(suoder,
                                                                                           self._dockerCompose,
                                                                                           _dev_yml.format(target)))

    def _start_new_container(self, sudoer, target):
        sh('{0} /bin/bash -c "export COMPOSE_HTTP_TIMEOUT=300 && TTSD_ETCD_ENV={1} {2} -f {3} up -d"'.format(sudoer,
                                                                                                             self.env,
                                                                                                             self._dockerCompose,
                                                                                                             _dev_yml.format(
                                                                                                                 target)))

    def jcversion(self):
        print "Starting jcmin..."
        sh('{0} jcversion.static_server={1} jcversion'.format(self._paver, self.etcd.get('common.static.server')))
