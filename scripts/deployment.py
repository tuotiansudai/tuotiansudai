from paver.shell import sh


class QADeployment(object):
    def deploy(self):
        self.clean()
        self.compile()
        self.migrate()
        self.jcversion()
        self.mkwar()
        self.mk_static_package()
        self.init_docker()

    def clean(self):
        print "Cleaning..."
        sh('/opt/gradle/latest/bin/gradle clean')
        sh('/usr/bin/git clean -fd', ignore_error=True)

    def compile(self):
        print "Compiling..."
        sh('/opt/gradle/latest/bin/gradle compileJava')

    def migrate(self):
        print "Migrating..."
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=aa ttsd-service:flywayRepair')
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=aa ttsd-service:flywayMigrate')
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=ump_operations ttsd-service:flywayRepair')
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=ump_operations ttsd-service:flywayMigrate')
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=sms_operations ttsd-service:flywayRepair')
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=sms_operations ttsd-service:flywayMigrate')
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=job_worker ttsd-service:flywayRepair')
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=job_worker ttsd-service:flywayMigrate')

    def build_and_unzip_worker(self):
        print "Making worker build..."
        sh('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle distZip')
        sh('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle -Pwork=invest distZip')
        sh('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle -Pwork=jpush distZip')
        sh('cd ./ttsd-job-worker/build/distributions && unzip \*.zip')

    def mkwar(self):
        print "Making war..."
        sh('/opt/gradle/latest/bin/gradle war')
        self.build_and_unzip_worker()

    def mk_static_package(self):
        print "Making static package..."
        sh('cd ./ttsd-web/src/main/webapp && zip -r static.zip images/ js/ pdf/ style/ tpl/ robots.txt')
        sh('cd ./ttsd-mobile-api/src/main/webapp && zip -r staticApi.zip api/')
        sh('mv ./ttsd-web/src/main/webapp/static.zip  ./ttsd-web/build/')
        sh('mv ./ttsd-mobile-api/src/main/webapp/staticApi.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static.zip -d static')
        sh('cd ./ttsd-web/build && unzip staticApi.zip -d static')

    def init_docker(self):
        print "Initialing docker..."
        import platform

        sudoer = 'sudo' if 'centos' in platform.platform() else ''
        self._remove_old_container(sudoer)
        self._start_new_container(sudoer)

    def _remove_old_container(self, suoder):
        sh('{0} /usr/local/bin/docker-compose -f dev.yml stop'.format(suoder))
        sh('{0} /bin/bash -c "export COMPOSE_HTTP_TIMEOUT=300 && /usr/local/bin/docker-compose -f dev.yml rm -f"'.format(suoder))

    def _start_new_container(self, sudoer):
        sh('{0} /usr/local/bin/docker-compose -f dev.yml up -d'.format(sudoer))

    def jcversion(self):
        print "Starting jcmin..."
        sh('/usr/bin/paver jcversion')
