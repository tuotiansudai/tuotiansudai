import os
from paver.tasks import task, needs


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
    from tests.ump_service.constants import FRONTEND_NOTIFY_CHANNEL

    start_listen(FRONTEND_NOTIFY_CHANNEL)


@task
def listen_be():
    from tests.ump_service.constants import BACKEND_NOTIFY_CHANNEL

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


@task
def mkwar():
    run_shell_under_v1('/opt/gradle/latest/bin/gradle clean')
    run_shell_under_v1('/opt/gradle/latest/bin/gradle war')


@task
def deploy_tomcat():
    run_shell_under_v1('sudo service tomcat6 stop')
    run_shell_under_v1('sudo rm -rf /usr/share/tomcat6/webapps/ROOT')
    run_shell_under_v1('sudo cp war/ROOT.war /usr/share/tomcat6/webapps/')
    run_shell_under_v1('sudo service tomcat6 start')


@task
def migrate():
    run_shell_under_v1('/opt/gradle/latest/bin/gradle flywayMigrate')


@task
@needs('mkwar', 'deploy_tomcat')
def deploy():
    """
    Deploy to production environment
    """

@task
@needs('migrate', 'deploy')
def devdeploy():
    """
    Deploy to dev/test environment
    """



def run_shell_under_v1(command):
    from paver.shell import sh

    return sh(command, cwd='v1')


def get_current_dir():
    return os.path.dirname(os.path.realpath(__file__))


def get_base_dir():
    test_dir = get_current_dir()
    return os.path.join(test_dir, 'tests', 'ump_service')
