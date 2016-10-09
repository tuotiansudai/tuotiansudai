from paver.shell import sh


class Deployment(object):

    _gradle='/opt/gradle/latest/bin/gradle'
    _dockerCompose='/usr/local/bin/docker-compose'
    _paver='/usr/bin/paver'

    _env='QA'

    def deploy(self, env):
        self._env = env
        self.clean()
        self.jcversion()
        self.compile()
        self.build_and_unzip_worker()
        self.mk_static_package()
        self.init_docker()

    def clean(self):
        print "Cleaning..."
        print self._gradle
        sh('/usr/bin/git clean -fd', ignore_error=True)

    def compile(self):
        print "Compiling..."
        sh('{} clean ttsd-config:flywayAA ttsd-config:flywayUMP ttsd-config:flywaySms ttsd-config:flywayWorker ttsd-config:flywayAsk ttsd-config:flywayActivity war'.format(
                self._gradle))
        sh('cp /workspace/new_version_config/signin_service/settings_local.py ./signin_service/')

    def build_and_unzip_worker(self):
        print "Making worker build..."
        sh('cd ./ttsd-job-worker && {} distZip'.format(self._gradle))
        sh('cd ./ttsd-job-worker && {} -Pwork=invest distZip'.format(self._gradle))
        sh('cd ./ttsd-job-worker && {} -Pwork=jpush distZip'.format(self._gradle))
        sh('cd ./ttsd-job-worker/build/distributions && unzip \*.zip')

    def mkwar(self):
        print "Making war..."
        if self._env == 'QA' :
            sh('{} war'.format(self._gradle))
        else :
            sh('{} ttsd-web:war -PconfigPath=/workspace/dev-config/'.format(self._gradle))
            sh('{} ttsd-activity:war -PconfigPath=/workspace/dev-config/'.format(self._gradle))
            sh('{} ttsd-pay-wrapper:war -PconfigPath=/workspace/dev-config/'.format(self._gradle))
            sh('{} ttsd-console:war -PconfigPath=/workspace/dev-config/'.format(self._gradle))
            sh('{} ttsd-mobile-api:war -PconfigPath=/workspace/dev-config/'.format(self._gradle))
            sh('{} ttsd-sms-wrapper:war -PconfigPath=/workspace/dev-config/'.format(self._gradle))
            sh('{} ttsd-point-web:war -PconfigPath=/workspace/dev-config/'.format(self._gradle))
        self.build_and_unzip_worker()

    def mk_static_package(self):
        print "Making static package..."
        sh('cd ./ttsd-web/src/main/webapp && zip -r static.zip images/ js/ pdf/ style/ tpl/ robots.txt')
        sh('mv ./ttsd-web/src/main/webapp/static.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static.zip -d static')

        sh('cd ./ttsd-mobile-api/src/main/webapp && zip -r static_api.zip api/')
        sh('mv ./ttsd-mobile-api/src/main/webapp/static_api.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static_api.zip -d static')

        sh('cd ./ttsd-ask-web/src/main/webapp && zip -r static_ask.zip ask/')
        sh('mv ./ttsd-ask-web/src/main/webapp/static_ask.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static_ask.zip -d static')

        sh('cd ./ttsd-activity/src/main/webapp && zip -r static_activity.zip activity/')
        sh('mv ./ttsd-activity/src/main/webapp/static_activity.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static_activity.zip -d static')

        sh('cd ./ttsd-point-web/src/main/webapp && zip -r static_point.zip point/')
        sh('mv ./ttsd-point-web/src/main/webapp/static_point.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static_point.zip -d static')

    def init_docker(self):
        print "Initialing docker..."
        import platform

        sudoer = 'sudo' if 'centos' in platform.platform() else ''
        self._remove_old_container(sudoer)
        self._start_new_container(sudoer)

    def _remove_old_container(self, suoder):
        sh('{} {} -f dev.yml stop'.format(suoder, self._dockerCompose))
        sh('{} /bin/bash -c "export COMPOSE_HTTP_TIMEOUT=300 && {} -f dev.yml rm -f"'.format(suoder, self._dockerCompose))

    def _start_new_container(self, sudoer):
        sh('{} {} -f dev.yml up -d'.format(sudoer, self._dockerCompose))

    def jcversion(self):
        print "Starting jcmin..."
        sh('{} jcversion'.format(self._paver))
