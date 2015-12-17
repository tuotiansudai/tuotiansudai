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


def copy_files_into_product_config(port):
    if port:
        import os
        import shutil

        src = '/workspace/docker_config_{0}'.format(port)
        if os.path.isdir(src):
            src_files = os.listdir(src)
            for file_name in src_files:
                full_file_name = os.path.join(src, file_name)
                if (os.path.isfile(full_file_name)):
                    dest = '/workspace/production_config/{0}'.format(file_name)
                    shutil.copy(full_file_name, dest)


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


def remove_old_container(name):
    from docker import Client

    client = Client(base_url='unix://var/run/docker.sock', version="1.17")
    try:
        client.stop(name)
        client.remove_container(name)
    except Exception:
        pass


def start_new_container(name, local_port):
    from docker import Client, utils

    client = Client(base_url='unix://var/run/docker.sock', version="1.17")
    war_dir = get_v1_war_dir()
    print "local war dir: {0}".format(war_dir)
    host_config = utils.create_host_config(port_bindings={8080: local_port}, binds=['{0}:/webapps'.format(war_dir)])
    container = client.create_container(image='leoshi/ttsd-tomcat6:v1', ports=[8080], volumes=['/webapps'],
                                        host_config=host_config, name=name)
    print "Container id:{0}".format(container['Id'])
    client.start(container)


@task
@needs('migrate')
@cmdopts([
    ('port=', 'p', 'Local port which is mapped to containers 8080')
])
def deploy_to_docker(options):
    """
    Deploy web apps to Tomcat6 container, NEED sudo run!
    Usage:
        sudo paver deploy_to_docker.port=30001 deploy_to_docker
    """
    copy_files_into_product_config(options.port)
    mkwar()
    name = "ttsd-{0}".format(options.port)
    remove_old_container(name)
    start_new_container(name, options.port)


@task
def v2deploy():
    from scripts.deployment import NewVersionDeployment

    v2 = NewVersionDeployment()
    v2.deploy()

@task
@cmdopts([
    ('dbhost=', '', 'database host'),
    ('dbport=', '', 'database port'),
    ('redishost=', '', 'redis host'),
    ('redisport=', '', 'redis port'),
])
def v2unittest(options):
    from scripts.unit_test import NewVersionUnitTest


    v2 = NewVersionUnitTest(options.dbhost, options.dbport, options.redishost, options.redisport)
    v2.test()

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


@task
def cideploy():
    """
    Deploy to PROD from CI
    """
    from paver.shell import sh

    try:
        ci_file = open('/workspace/ci/abc', 'rb')
        pwd = ci_file.readline().strip()
        sh("/usr/local/bin/fab deploy -p {0} --show=debug".format(pwd))
        ci_file.close()
    except IOError as e:
        print e


@task
def deployall():
    """
    Deploy all components to PROD from CI
    """
    from paver.shell import sh

    try:
        ci_file = open('/workspace/ci/def', 'rb')
        pwd = ci_file.readline().strip()
        sh("/usr/local/bin/fab v2deploy -p {0} --show=debug".format(pwd))
        ci_file.close()
    except IOError as e:
        print e


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
    log_file.close()


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


def get_v1_war_dir():
    current_dir = get_current_dir()
    return os.path.join(current_dir, 'v1', 'war')


def get_base_dir():
    test_dir = get_current_dir()
    return os.path.join(test_dir, 'test', 'ump_service')
