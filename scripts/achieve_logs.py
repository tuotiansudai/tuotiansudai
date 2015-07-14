import datetime
import logging

logger = logging.getLogger(__name__)
hdlr = logging.FileHandler('/var/log/achieve_logs.log')
formatter = logging.Formatter('%(asctime)s %(levelname)s %(message)s')
hdlr.setFormatter(formatter)
logger.addHandler(hdlr)
logger.setLevel(logging.INFO)

access_keyid = "h3mUO1gON8xOgsKo"
access_keysecret = "9LH9VX1zpmDC3s5uqY3oUb5g1c14jy"
oss_endpoint = "oss-cn-hangzhou.aliyuncs.com"
bucket_name = "ttsd-logs"


from oss.oss_api import *



def get_yesterday_str():
    today = datetime.datetime.today()
    yesterday = today - datetime.timedelta(days=1)
    return yesterday.strftime("%Y-%m-%d")


def compress_tomcat_logs(yesterday_str):
    tomcat_debug_log = "/var/log/tomcat6/debug.log.{0}".format(yesterday_str)
    from subprocess import call

    tar_file = "{0}.tar.gz".format(tomcat_debug_log)
    ret = call(["tar", "czf", tar_file, tomcat_debug_log])
    if ret != 0:
        logger.error('compress {0} error, ret: {1}'.format(tomcat_debug_log, ret))
        raise Exception()
    else:
        call(['rm', tomcat_debug_log])
    return tar_file


def compress_nginx_logs(yesterday_str):
    nginx_home = "/var/log/nginx/"
    nginx_access_log = "{0}tuotian.access.log".format(nginx_home)
    nginx_error_log = "{0}tuotian.error.log".format(nginx_home)

    from subprocess import call

    access_log = "{0}-{1}".format(nginx_access_log, yesterday_str)
    error_log = "{0}-{1}".format(nginx_error_log, yesterday_str)
    call(["mv", nginx_access_log, access_log])
    call(["mv", nginx_error_log, error_log])
    call("kill -USR1 `cat /var/run/nginx.pid`", shell=True)
    call("sleep 1", shell=True)
    tar_file = "{0}nginx{1}.tar.gz".format(nginx_home, yesterday_str)
    ret = call(["tar", "czf", tar_file, access_log, error_log])
    if ret != 0:
        logger.error('compress {0} error, ret: {1}'.format(nginx_home, ret))
        raise Exception()
    else:
        call(["rm", access_log])
        call(["rm", error_log])
    return tar_file


def compress_logs():
    yesterday_str = get_yesterday_str()
    tomcat_log = compress_tomcat_logs(yesterday_str)
    nginx_log = compress_nginx_logs(yesterday_str)
    return [tomcat_log, nginx_log]


def create_oss_folder(oss):
    yesterday_str = get_yesterday_str()
    folder_name = "{0}/".format(yesterday_str)
    res = oss.put_object_from_string(bucket_name, folder_name, "")
    if res.status != 200:
        logger.error('create oss folder {0} error, status code:{1}'.format(folder_name, res.status))
        raise Exception("error")
    return folder_name


def upload_logs(file_paths, suffix):
    oss = OssAPI(oss_endpoint, access_keyid, access_keysecret)
    # res = oss.create_bucket("ttsd-logs", "private")
    folder_name = create_oss_folder(oss)
    print file_paths
    for file_path in file_paths:
        print file_path
        file_name = file_path.split('/')[-1]
        res = oss.put_object_from_file(bucket_name, "{0}{1}_{2}".format(folder_name, file_name, suffix), file_path)
        if res.status != 200:
            logger.error('upload {0} error, status code:{1}'.format(file_path, res.status))
            raise Exception()
        else:
            remove_file(file_path)


def remove_file(file):
    from subprocess import call

    ret = call(["rm", file])
    if ret != 0:
        logger.error("rm {0} error".format(file))


def main(suffix_name):
    try:
        logger.info("start")
        logs = compress_logs()
        logger.info(logs)
        upload_logs(logs, suffix_name)
        logger.info("done")
    except Exception as e:
        logger.error(e)


if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser(description='Log Achiever')
    parser.add_argument('-s', '--suffix', help='suffix name', required=True)
    parser.add_argument('-f', '--file', help='file full path')
    args = parser.parse_args()
    if not args.file:
        main(args.suffix)
    else:
        upload_logs([args.file], args.suffix)
