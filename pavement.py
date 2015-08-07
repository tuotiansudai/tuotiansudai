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


@task
def mkwar():
    run_shell_under_v1('/opt/gradle/latest/bin/gradle clean')
    run_shell_under_v1('/opt/gradle/latest/bin/gradle war')


def stop_tomcat():
    run_shell_under_v1('sudo kill -9 `cat /var/run/tomcat6.pid`')
    run_shell_under_v1('sudo rm /var/run/tomcat6.pid')
    run_shell_under_v1('sudo rm /var/lock/subsys/tomcat6')


@task
def deploy_tomcat():
    stop_tomcat()
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


def generate_git_log_file():
    from paver.shell import sh

    sh('/usr/bin/git ls-tree -r HEAD ttsd-web/src/main/webapp/js | awk \'{print $3,$4}\' > git_version.log')
    sh('/usr/bin/git ls-tree -r HEAD ttsd-web/src/main/webapp/style | awk \'{print $3,$4}\' >> git_version.log')


def versioning_min_files(path):
    import glob
    import itertools
    import shutil

    target_files = glob.glob(path)
    log_file = open('git_version.log', 'rb')
    for line in log_file:
        columns = line.strip().split()
        original_file_path, file_version = columns[-1], columns[0]
        if original_file_path in target_files:
            full_path_parts = original_file_path.split('/')
            name_parts = full_path_parts[-1].split('.')
            new_name_parts = itertools.chain([name_parts[0], file_version[:8]], name_parts[1:])
            new_name = '.'.join(new_name_parts)
            new_file_full_path = os.path.join('/'.join(full_path_parts[:-1]), new_name)
            shutil.copyfile(original_file_path, new_file_full_path)


@task
def jcversion():
    """
    Versioning all min js/css files based on git version
    """
    generate_git_log_file()
    versioning_min_files('ttsd-web/src/main/webapp/js/dest/*.min.js')
    versioning_min_files('ttsd-web/src/main/webapp/style/dest/*.min.css')


def run_shell_under_v1(command):
    from paver.shell import sh

    return sh(command, cwd='v1')


def get_current_dir():
    return os.path.dirname(os.path.realpath(__file__))


def get_base_dir():
    test_dir = get_current_dir()
    return os.path.join(test_dir, 'test', 'ump_service')
