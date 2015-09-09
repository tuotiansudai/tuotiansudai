import os
from paver.shell import sh
from docker import Client, utils


class NewVersionDeployment(object):
    def __init__(self, web_port, sms_port, pay_port, console_port):
        self.console_port = console_port
        self.pay_port = pay_port
        self.sms_port = sms_port
        self.web_port = web_port
        self.app_to_port = {'ttsd-web': self.web_port, 'ttsd-sms-wrapper': self.sms_port,
                            'ttsd-pay-wrapper': self.pay_port}

    def deploy(self):
        self.migrate()
        self.clean()
        self.mkwar()
        self.init_docker()

    def migrate(self):
        print "Migrating..."
        self._run_gradle('flywayMigrate')

    def clean(self):
        print "Cleaning..."
        self._run_gradle('clean')

    def mkwar(self):
        print "Making war..."
        self._run_gradle('war')

    def init_docker(self):
        print "Initialing docker..."
        self._remove_old_container()
        self._start_new_container()

    def _run_gradle(self, command):
        sh('/opt/gradle/latest/bin/gradle ttsd-web:{0}'.format(command))
        sh('/opt/gradle/latest/bin/gradle ttsd-sms-wrapper:{0}'.format(command))
        sh('/opt/gradle/latest/bin/gradle ttsd-pay-wrapper:{0}'.format(command))

    def _remove_old_container(self):
        client = Client(base_url='unix://var/run/docker.sock', version="1.17")
        for app, port in self.app_to_port.items():
            name = "ttsd-{0}-{1}".format(app, port)
            try:
                client.stop(name)
                client.remove_container(name)
            except Exception:
                pass

    def _start_new_container(self):
        client = Client(base_url='unix://var/run/docker.sock', version="1.17")
        for app, port in self.app_to_port.items():
            name = "ttsd-{0}-{1}".format(app, port)
            war_dir = self.get_war_dir(app)
            print "local war dir: {0}".format(war_dir)
            host_config = utils.create_host_config(port_bindings={8080: port}, binds=['{0}:/webapps'.format(war_dir)])
            container = client.create_container(image='leoshi/ttsd-tomcat6', ports=[8080], volumes=['/webapps'],
                                                host_config=host_config, name=name)
            print "{0} Container id:{1}".format(app, container['Id'])
            client.start(container)

    def get_war_dir(self, app):
        current_dir = os.path.dirname(os.path.realpath(__file__))
        return os.path.join(current_dir, '..', app, 'war')

