import os
import sys

sys.path.insert(1, os.path.dirname(os.path.abspath(__file__)))
from paver.tasks import task, needs, cmdopts


@task
def start_web():
    base_dir = get_base_dir()
    application_file_path = os.path.join(base_dir, 'application.py')
    from paver.easy import sh

    sh('python {0}&'.format(application_file_path))


def start_listen(channel):
    base_dir = get_base_dir()
    worker_file_path = os.path.join(base_dir, 'workers.py')
    from paver.easy import sh

    sh('python {0} {1}&'.format(worker_file_path, channel))


@task
def listen_fe():
    from test.ump_service.constants import FRONTEND_NOTIFY_CHANNEL

    start_listen(FRONTEND_NOTIFY_CHANNEL)


@task
def listen_be():
    from test.ump_service.constants import BACKEND_NOTIFY_CHANNEL

    start_listen(BACKEND_NOTIFY_CHANNEL)


@task
def set_env():
    from paver.shell import sh

    sh("export PYTHONPATH={0}".format(get_current_dir()))


@task
@needs('start_web', 'listen_fe', 'listen_be')
def run():
    """
    Run web and all workers
    """


@task
def stop():
    from paver.shell import sh

    sh('pkill python')
    sh('pkill python')


def remove_old_container(name):
    from docker import Client

    client = Client(base_url='unix://var/run/docker.sock', version="1.17")
    try:
        client.stop(name)
        client.remove_container(name)
    except Exception:
        pass


@task
def qa():
    """
    Deploy Staging/QA environment
    """
    from scripts.deployment import Deployment

    deployment = Deployment()
    deployment.deploy('QA')

@task
def dev():
    from scripts.deployment import Deployment

    deployment = Deployment()
    deployment.deploy('DEV')


@task
@cmdopts([
    ('dbhost=', '', 'database host'),
    ('dbport=', '', 'database port'),
    ('redishost=', '', 'redis host'),
    ('redisport=', '', 'redis port'),
])
def ut(options):
    """
    Run Unit Test
    e.g. paver ut.dbhost=127.0.0.1 ut.dbport=40020 ut.redishost=127.0.0.1 ut.redisport=40016 ut
    """
    from scripts.unit_test import UTRunner

    v2 = UTRunner(options.dbhost, options.dbport, options.redishost, options.redisport)
    v2.test()


def fab_command(command):
    from paver.shell import sh

    try:
        ci_file = open('/workspace/ci/def', 'rb')
        pwd = ci_file.readline().strip()
        sh("/usr/local/bin/fab {1} -p {0} --show=debug".format(pwd, command))
        ci_file.close()
    except IOError as e:
        print e


@task
def prod():
    """
    Deploy all components to PROD from CI
    """
    fab_command('all')


@task
def only_web():
    """
    Deploy web component to PROD from CI
    """
    fab_command("web")


@task
def only_console():
    """
    Deploy console component to PROD from CI
    """
    fab_command("console")


@task
def only_api():
    """
    Deploy api component to PROD from CI
    """
    fab_command("api")

@task
def only_pay():
    """
    Deploy pay component to PROD from CI
    """
    fab_command("pay")

@task
def only_sms():
    """
    Deploy sms component to PROD from CI
    """
    fab_command("sms")


@task
def only_worker():
    """
    Deploy worker component to PROD from CI
    """
    fab_command("worker")


def versioning_files(path):
    from paver.shell import sh

    owd = os.getcwd()
    try:
        os.chdir(path)
        sh('/usr/bin/npm install')
        sh('/usr/bin/npm run dist')
    finally:
        os.chdir(owd)

@task
def jcversion():
    """
    Versioning all min js/css files based on git version
    """
    versioning_files('ttsd-web/')
    versioning_files('ttsd-mobile-api/')
    versioning_files('ttsd-activity/')

def get_current_dir():
    return os.path.dirname(os.path.realpath(__file__))


def get_base_dir():
    test_dir = get_current_dir()
    return os.path.join(test_dir, 'test', 'ump_service')


