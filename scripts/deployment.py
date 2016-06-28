import commands
from paver.shell import sh


class Deployment(object):

    _, _gradle=commands.getstatusoutput("which gradle")
    _, _dockerCompose=commands.getstatusoutput("which docker-compose")
    _, _paver=commands.getstatusoutput("which paver")

    _env='QA'

    def deploy(self, env):
        self._env=env
        self.clean()
        self.compile()
        self.migrate()
        self.jcversion()
        self.mkwar()
        self.mk_static_package()
        self.set_nginx_host()
        self.init_docker()

    def clean(self):
        print "Cleaning..."
        print self._gradle
        sh('{0} clean'.format(self._gradle))
        sh('/usr/bin/git clean -fd', ignore_error=True)

    def compile(self):
        print "Compiling..."
        sh('{0} compileJava'.format(self._gradle))

    def migrate(self):
        print "Migrating..."
        sh('{0} -Pdatabase=aa ttsd-service:flywayRepair'.format(self._gradle))
        sh('{0} -Pdatabase=aa ttsd-service:flywayMigrate'.format(self._gradle))
        sh('{0} -Pdatabase=ump_operations ttsd-service:flywayRepair'.format(self._gradle))
        sh('{0} -Pdatabase=ump_operations ttsd-service:flywayMigrate'.format(self._gradle))
        sh('{0} -Pdatabase=sms_operations ttsd-service:flywayRepair'.format(self._gradle))
        sh('{0} -Pdatabase=sms_operations ttsd-service:flywayMigrate'.format(self._gradle))
        sh('{0} -Pdatabase=job_worker ttsd-service:flywayRepair'.format(self._gradle))
        sh('{0} -Pdatabase=job_worker ttsd-service:flywayMigrate'.format(self._gradle))

    def build_and_unzip_worker(self):
        print "Making worker build..."
        sh('cd ./ttsd-job-worker && {0} distZip'.format(self._gradle))
        sh('cd ./ttsd-job-worker && {0} -Pwork=invest distZip'.format(self._gradle))
        sh('cd ./ttsd-job-worker && {0} -Pwork=jpush distZip'.format(self._gradle))
        sh('cd ./ttsd-job-worker/build/distributions && unzip \*.zip')

    def mkwar(self):
        print "Making war..."
        if self._env == 'QA' :
            sh('{0} war'.format(self._gradle))
        else :
            sh('{0} ttsd-web:war -PconfigPath=/workspace/dev-config/'.format(self._gradle))
            sh('{0} ttsd-activity:war -PconfigPath=/workspace/dev-config/'.format(self._gradle))
            sh('{0} ttsd-pay-wrapper:war -PconfigPath=/workspace/dev-config/'.format(self._gradle))
            sh('{0} ttsd-console:war -PconfigPath=/workspace/dev-config/'.format(self._gradle))
            sh('{0} ttsd-mobile-api:war -PconfigPath=/workspace/dev-config/'.format(self._gradle))
            sh('{0} ttsd-sms-wrapper:war -PconfigPath=/workspace/dev-config/'.format(self._gradle))
        self.build_and_unzip_worker()

    def mk_static_package(self):
        print "Making static package..."
        sh('cd ./ttsd-web/src/main/webapp && zip -r static.zip images/ js/ pdf/ style/ tpl/ robots.txt')
        sh('mv ./ttsd-web/src/main/webapp/static.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static.zip -d static')

        sh('cd ./ttsd-mobile-api/src/main/webapp && zip -r static_api.zip api/')
        sh('mv ./ttsd-mobile-api/src/main/webapp/static_api.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static_api.zip -d static')

        sh('cd ./ttsd-activity/src/main/webapp && zip -r static_activity.zip activity/')
        sh('mv ./ttsd-activity/src/main/webapp/static_activity.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static_activity.zip -d static')

    def set_nginx_host(self):
        _, _host_ = commands.getstatusoutput("ifconfig en0 | grep inet | grep -v inet6 | awk '{print $2}'")
        sh("sed 's/_host_name_/{0}/g' ./scripts/docker/ttsd-test-nginx-rewrite.conf.temp > ./scripts/docker/ttsd-test-nginx-rewrite.conf".format(_host_))

    def init_docker(self):
        print "Initialing docker..."
        import platform

        sudoer = 'sudo' if 'centos' in platform.platform() else ''
        self._remove_old_container(sudoer)
        self._start_new_container(sudoer)

    def _remove_old_container(self, suoder):
        sh('{0} {1} -f dev.yml stop'.format(suoder, self._dockerCompose))
        sh('{0} /bin/bash -c "export COMPOSE_HTTP_TIMEOUT=300 && {1} -f dev.yml rm -f"'.format(suoder, self._dockerCompose))

    def _start_new_container(self, sudoer):
        sh('{0} {1} -f dev.yml up -d'.format(sudoer, self._dockerCompose))

    def jcversion(self):
        print "Starting jcmin..."
        sh('{0} jcversion'.format(self._paver))
