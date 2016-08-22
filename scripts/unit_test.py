from paver.shell import sh


class UTRunner(object):
    def __init__(self, db_host, db_port, redis_host, redis_port):
        self.db_host = db_host
        self.db_port = db_port
        self.redis_host = redis_host
        self.redis_port = redis_port

    def test(self):
        self.init_docker()
        self.run_test()
        self.clean_env()

    def init_docker(self):
        print "Initialing docker..."
        self._remove_old_container()
        self._start_new_container()

    def _remove_old_container(self):
        sh('/usr/local/bin/docker-compose -f unit_test.yml stop')
        sh('/usr/local/bin/docker-compose -f unit_test.yml rm -f')

    def _start_new_container(self):
        sh('/usr/local/bin/docker-compose -f unit_test.yml up -d')

    def run_test(self):
        print "Starting test..."
        sh('/opt/gradle/latest/bin/gradle -Pdbhost={0} -Pdbport={1} -Predishost={2} -Predisport={3} clean compileJava ttsd-config:flywayAA ttsd-config:flywayUMP ttsd-config:flywaySms ttsd-config:flywayWorker ttsd-config:flywayAsk test'.format(
                self.db_host, self.db_port, self.redis_host, self.redis_port))

    def clean_env(self):
        self._remove_old_container()

