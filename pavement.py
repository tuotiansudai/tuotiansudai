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
def qa(options):
    """
    Deploy Staging/QA environment
    """
    from scripts.deployment import Deployment

    deployment = Deployment()
    deployment.deploy(options.env)

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


def generate_git_log_file():
    from paver.shell import sh
    sh('/usr/bin/git ls-tree -r HEAD ttsd-activity-web/src/main/webapp/activity/js | awk \'{print $3,$4}\' > git_version.log')
    sh('/usr/bin/git ls-tree -r HEAD ttsd-activity-web/src/main/webapp/activity/style | awk \'{print $3,$4}\' >> git_version.log')
    sh('/usr/bin/git ls-tree -r HEAD ttsd-point-web/src/main/webapp/point/js | awk \'{print $3,$4}\' >> git_version.log')
    sh('/usr/bin/git ls-tree -r HEAD ttsd-point-web/src/main/webapp/point/style | awk \'{print $3,$4}\' >> git_version.log')


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


def build_versioning_file_mapping(path):
    import glob

    full_path_files = glob.glob(path + '*.min.js')
    name2path = {}
    for full_path_file in full_path_files:
        file_name = full_path_file.split('/')[-1]
        name_parts = file_name.split('.')
        if len(name_parts) == 4:  # config.77248946.min.js
            key, value = "{0}.min".format(name_parts[0]), '.'.join(name_parts[:-1])
            name2path[key] = value
    return name2path


def replace_versioned_config_file(name2path, path):
    import re

    try:
        with open('{0}{1}.js'.format(path, name2path.get('config.min')), 'r+') as config_file:
            content = config_file.read()
            for key, value in name2path.items():
                content = re.sub(key, value, content)
            config_file.seek(0)
            config_file.write(content)
    except IOError as e:
        print e


def replace_min_files_in_config_js_file(path):
    name2path = build_versioning_file_mapping(path)
    replace_versioned_config_file(name2path, path)


def versioning_webpack_files(path):
    from paver.shell import sh

    owd = os.getcwd()
    try:
        os.chdir(path)
        sh('/usr/bin/npm install')
        sh('/usr/bin/npm run dist')
    finally:
        os.chdir(owd)

def versioning_static_resource_files(options):
    from paver.shell import sh

    owd = os.getcwd()
    try:
        os.chdir('./ttsd-frontend-manage')
        sh('/usr/bin/npm install')
        sh('PLAT={0} /usr/bin/npm run build'.format(options.env))
    finally:
        os.chdir(owd)


@task
def jcversion(options):
    """
    Versioning all min js/css files based on git version
    """
    generate_git_log_file()

    versioning_min_files('ttsd-activity-web/src/main/webapp/activity/js/dest/*.min.js')
    versioning_min_files('ttsd-activity-web/src/main/webapp/activity/style/dest/*.min.css')
    replace_min_files_in_config_js_file('ttsd-activity-web/src/main/webapp/activity/js/dest/')

    versioning_min_files('ttsd-point-web/src/main/webapp/point/js/dest/*.min.js')
    versioning_min_files('ttsd-point-web/src/main/webapp/point/style/dest/*.min.css')
    replace_min_files_in_config_js_file('ttsd-point-web/src/main/webapp/point/js/dest/')

    versioning_webpack_files('ttsd-mobile-api/')

    versioning_static_resource_files(options)

def get_current_dir():
    return os.path.dirname(os.path.realpath(__file__))


def get_base_dir():
    test_dir = get_current_dir()
    return os.path.join(test_dir, 'test', 'ump_service')


