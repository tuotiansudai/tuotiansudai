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


@task
def qa(options):
    """
    Deploy Staging/QA environment
    """
    from scripts.deployment import Deployment

    deployment = Deployment(env=options.env, pay_fake=options.get('pay', {}).get('fake'))
    deployment.deploy()


@task
def dev():
    from scripts.deployment import Deployment

    deployment = Deployment(env='DEV')
    deployment.deploy()


@task
def ut():
    from scripts.unit_test import UTRunner
    UTRunner().test()


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
def only_activity():
    """
    Deploy activity component to PROD from CI
    """
    fab_command("activity")


@task
def only_ask():
    """
    Deploy ask component to PROD from CI
    """
    fab_command("ask")


@task
def only_worker():
    """
    Deploy worker component to PROD from CI
    """
    fab_command("worker")


@task
def only_sign_in():
    """
    Deploy worker component to PROD from CI
    """
    fab_command("signin")


@task
def only_point():
    """
    Deploy worker component to PROD from CI
    """
    fab_command("point")


@task
def jcversion(options):
    from paver.shell import sh

    owd = os.getcwd()
    try:
        os.chdir('./ttsd-frontend-manage')
        sh('/usr/bin/npm install')
        sh('STATIC_SERVER={0} /usr/bin/npm run build'.format(options.static_server))
    finally:
        os.chdir(owd)


def get_current_dir():
    return os.path.dirname(os.path.realpath(__file__))


def get_base_dir():
    test_dir = get_current_dir()
    return os.path.join(test_dir, 'test', 'ump_service')
