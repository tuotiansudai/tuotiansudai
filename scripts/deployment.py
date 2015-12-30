from paver.shell import sh


class NewVersionDeployment(object):
    def deploy(self):
        self.clean()
        self.compile()
        self.migrate()
        self.jcversion()
        self.mkwar()
        self.mk_static_package()
        self.init_docker()

    def clean(self):
        print "Cleaning..."
        sh('/opt/gradle/latest/bin/gradle clean')
        sh('/usr/bin/git clean -fd')

    def compile(self):
        print "Compiling..."
        sh('/opt/gradle/latest/bin/gradle compileJava')

    def migrate(self):
        print "Migrating..."
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=aa ttsd-service:flywayMigrate')
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=ump_operations ttsd-service:flywayMigrate')
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=sms_operations ttsd-service:flywayMigrate')
        sh('/opt/gradle/latest/bin/gradle -Pdatabase=job_worker ttsd-service:flywayMigrate')

    def build_and_unzip_worker(self):
        print "Making worker build..."
        sh('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle distZip')
        sh('cd ./ttsd-job-worker && /opt/gradle/latest/bin/gradle -Prop=invest distZip')
        sh('cd ./ttsd-job-worker/build/distributions && unzip \*.zip')

    def mkwar(self):
        print "Making war..."
        sh('/opt/gradle/latest/bin/gradle war')
        self.build_and_unzip_worker()

    def mk_static_package(self):
        print "Making static package..."
        sh('cd ./ttsd-web/src/main/webapp && zip -r static.zip images/ js/ pdf/ style/ tpl/ robots.txt')
        sh('mv ./ttsd-web/src/main/webapp/static.zip  ./ttsd-web/build/')
        sh('cd ./ttsd-web/build && unzip static.zip -d static')

    def init_docker(self):
        print "Initialing docker..."
        self._remove_old_container()
        self._start_new_container()

    def _remove_old_container(self):
        sh('sudo /usr/local/bin/docker-compose -f dev.yml stop')
        sh('sudo /bin/bash -c "export COMPOSE_HTTP_TIMEOUT=300 && /usr/local/bin/docker-compose -f dev.yml rm -f"')

    def _start_new_container(self):
        sh('sudo /usr/local/bin/docker-compose -f dev.yml up -d')

    def jcversion(self):
        print "Starting jcmin..."
        sh('/usr/bin/git ls-tree -r HEAD ./ttsd-web/src/main/webapp/js | awk \'{print $3,$4}\' > git_version.log')
        sh('/usr/bin/git ls-tree -r HEAD ./ttsd-web/src/main/webapp/style | awk \'{print $3,$4}\' >> git_version.log')
        self._versioning_min_files('ttsd-web/src/main/webapp/js/dest/*.min.js')
        self._versioning_min_files('ttsd-web/src/main/webapp/style/dest/*.min.css')

    def _versioning_min_files(self, path):
        import glob
        import itertools
        import shutil
        import os

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
