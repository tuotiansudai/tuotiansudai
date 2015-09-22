from paver.shell import sh


class NewVersionDeployment(object):

    def deploy(self):
        self.clean()
        self.compile()
        self.migrate()
        self.mkwar()
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

    def mkwar(self):
        print "Making war..."
        sh('/opt/gradle/latest/bin/gradle war')

    def init_docker(self):
        print "Initialing docker..."
        self._remove_old_container()
        self._start_new_container()

    def _remove_old_container(self):
        sh('sudo /usr/local/bin/docker-compose -f dev.yml stop')
        sh('sudo /usr/local/bin/docker-compose -f dev.yml rm -f')

    def _start_new_container(self):
        sh('sudo /usr/local/bin/docker-compose -f dev.yml up -d')

