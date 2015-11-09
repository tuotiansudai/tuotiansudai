from paver.shell import sh


class NewVersionDeployment(object):

    def deploy(self):
        self.clean()
        self.compile()
        self.migrate()
        self.mkwar()
        self.mk_static_package()
        self.init_docker()

    def clean(self):
        print "Cleaning..."
        sh('/opt/gradle/latest/bin/gradle clean')

    def compile(self):
        print "Compiling..."
        sh('/opt/gradle/latest/bin/gradle compileJava')

    def migrate(self):
        print "Migrating..."
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=aa ttsd-service:flywayMigrate')
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=ump_operations ttsd-service:flywayMigrate')
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=sms_operations ttsd-service:flywayMigrate')
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=job_worker ttsd-service:flywayMigrate')

    def build_and_unzip_worker(self):
        print "Making worker build..."
        sh('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle distZip')
        sh('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle -Prop=invest distZip')
        sh('cd ./ttsd-job-worker/build/distributions && unzip \*.zip')

    def mkwar(self):
        print "Making war..."
        sh('/opt/gradle/latest/bin/gradle war')
        self.build_and_unzip_worker()

    def mk_static_package(self):
        print "Making static package..."
        sh('cd ./ttsd-web/src/main/webapp && zip -r static.zip images/ js/ pdf/ style/ tpl/')
        sh('mv ./ttsd-web/src/main/webapp/static.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static.zip -d static')

    def init_docker(self):
        print "Initialing docker..."
        self._remove_old_container()
        self._start_new_container()

    def _remove_old_container(self):
        sh('sudo /usr/local/bin/docker-compose -f dev.yml stop')
        sh('sudo /usr/local/bin/docker-compose -f dev.yml rm -f')

    def _start_new_container(self):
        sh('sudo /usr/local/bin/docker-compose -f dev.yml up -d')

