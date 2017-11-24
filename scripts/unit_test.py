import os
from paver.shell import sh

import config_deploy
import etcd_client


class UTRunner(object):
    _config_path = os.getenv('TTSD_CONFIG_PATH', '/workspace/deploy-config')
    _gradle = '/opt/gradle/latest/bin/gradle'

    def __init__(self):
        self.env = 'UT'
        self.etcd = etcd_client.client(self.env)

    def test(self):
        self.config_file()
        self.init_docker()
        self.run_test()
        self.clean_env()

    def config_file(self):
        print "Generate config file..."
        config_deploy.deploy(self.etcd, self.env, 'true')

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
        from scripts import migrate_db
        migrate_db.migrate(self._gradle, self.etcd)
        sh('TTSD_ETCD_ENDPOINT=UT {} test'.format(self._gradle))
        sh('cp {0}/signin_service/settings_local.py ./ttsd-user-rest-service/'.format(self._config_path))
        sh(
            'docker run -v `pwd`/ttsd-user-rest-service:/app --rm --net=bridge --link test-db-server --link test-redis-server leoshi/ttsd-signin-flask python test.py')

    def clean_env(self):
        self._remove_old_container()
