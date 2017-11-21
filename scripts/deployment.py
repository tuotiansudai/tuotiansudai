import os
import etcd3
from paver.shell import sh
import config_deploy


class Deployment(object):
    etcd_host = {'DEV': '127.0.0.1',
                 'QA1': '192.168.1.139',
                 'QA2': '192.168.1.139',
                 'QA3': '192.168.1.139',
                 'QA4': '192.168.1.139',
                 'QA5': '192.168.1.139'}

    etcd_port = {'DEV': '2379',
                 'QA1': '23791',
                 'QA2': '23791',
                 'QA3': '23791',
                 'QA4': '23791',
                 'QA5': '23791'}

    _config_path = os.getenv('TTSD_CONFIG_PATH', '/workspace/deploy-config')
    _gradle = '/opt/gradle/latest/bin/gradle'
    _dockerCompose = '/usr/local/bin/docker-compose'
    _paver = '/usr/bin/paver'

    def deploy(self, build_params):
        host = self.etcd_host.get(build_params.get('env', 'DEV'))
        port = self.etcd_port.get((build_params.get('env', 'DEV')))
        etcd = etcd3.client(host=host, port=port)
        self.clean()
        self.config_file(etcd, build_params)
        self.jcversion(build_params)
        self.migrate(etcd)
        self.compile()
        self.build_and_unzip_worker()
        self.build_mq_consumer()
        self.build_rest_service()
        self.build_diagnosis()
        self.build_worker_monitor()
        self.mk_static_package()
        self.init_docker()

    def clean(self):
        print "Cleaning..."
        print self._gradle
        sh('/usr/bin/git clean -fd', ignore_error=True)

    def config_file(self, etcd, build_params):
        print "Generate config file..."
        config_deploy.deploy(etcd, build_params)

    def migrate(self, etcd):
        from scripts import migrate_db
        migrate_db.migrate(self._gradle, etcd)

    def compile(self):
        print "Compiling..."
        sh('{0} clean initMQ war renameWar'.format(self._gradle))
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
        sh('cd ./ttsd-web/src/main/webapp && zip -r static.zip images/ js/ pdf/ style/ tpl/ robots.txt')
        sh('mv ./ttsd-web/src/main/webapp/static.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static.zip -d static')

        sh('cd ./ttsd-frontend-manage/resources/prod && zip -r static_all.zip *')
        sh('mv ./ttsd-frontend-manage/resources/prod/static_all.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static_all.zip -d static')

    def init_docker(self):
        print "Initialing docker..."
        import platform

        sudoer = 'sudo' if 'centos' in platform.platform() else ''
        self._remove_old_container(sudoer)
        self._start_new_container(sudoer)

    def _remove_old_container(self, suoder):
        sh('{0} {1} -f dev.yml stop'.format(suoder, self._dockerCompose))
        sh('{0} /bin/bash -c "export COMPOSE_HTTP_TIMEOUT=300 && {1} -f dev.yml rm -f"'.format(suoder,
                                                                                               self._dockerCompose))

    def _start_new_container(self, sudoer):
        sh('{0} {1} -f dev.yml up -d'.format(sudoer, self._dockerCompose))

    def jcversion(self, build_params):
        print "Starting jcmin..."
        sh('{0} jcversion.env={1} jcversion'.format(self._paver, build_params.get('env')))
