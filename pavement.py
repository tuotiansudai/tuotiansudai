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


def fab_command(command, skip_package):
    from paver.shell import sh

    try:
        ci_file = open('/workspace/ci/def', 'rb')
        pwd = ci_file.readline().strip()
        sh("/usr/local/bin/fab {1}:skip_package={2} -p {0} --show=debug".format(pwd, command, skip_package))
        ci_file.close()
    except IOError as e:
        print e


@task
def prod(options):
    """
    Deploy all components to PROD from CI
    """
    fab_command('all', parse_options(options))


@task
def only_package(options):
    """
    compile and package
    """
    fab_command('package', 'False')


@task
def only_web(options):
    """
    Deploy web component to PROD from CI
    """
    fab_command("web", parse_options(options))


@task
def only_console(options):
    """
    Deploy console component to PROD from CI
    """
    fab_command("console", parse_options(options))


@task
def only_api(options):
    """
    Deploy api component to PROD from CI
    """
    fab_command("api", parse_options(options))


@task
def only_pay(options):
    """
    Deploy pay component to PROD from CI
    """
    fab_command("pay", parse_options(options))


@task
def only_sms(options):
    """
    Deploy sms component to PROD from CI
    """
    fab_command("sms", parse_options(options))


@task
def only_activity(options):
    """
    Deploy activity component to PROD from CI
    """
    fab_command("activity", parse_options(options))


@task
def only_ask(options):
    """
    Deploy ask component to PROD from CI
    """
    fab_command("ask", parse_options(options))


@task
def only_worker(options):
    """
    Deploy worker component to PROD from CI
    """
    fab_command("worker", parse_options(options))


@task
def only_sign_in(options):
    """
    Deploy worker component to PROD from CI
    """
    fab_command("signin", parse_options(options))


@task
def only_point(options):
    """
    Deploy worker component to PROD from CI
    """
    fab_command("point", parse_options(options))


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


def parse_options(options):
    skip_package = options.skip_package == 'True' if hasattr(options, 'skip_package') else False
    print 'skip packege is {0}'.format(skip_package)
    return skip_package


def get_current_dir():
    return os.path.dirname(os.path.realpath(__file__))


def get_base_dir():
    test_dir = get_current_dir()
    return os.path.join(test_dir, 'test', 'ump_service')
