import os

from etcd_config import EtcdConfig

ENV = os.getenv('TTSD_ETCD_ENV', 'dev')

config = EtcdConfig(ENV)

DEBUG = config.get('user.rest.service.debug', default_value=True)

REDIS_HOST = config.get('common.redis.host', default_value='192.168.33.10')
REDIS_PORT = config.get('common.redis.port', default_value=6379)
REDIS_DB = config.get('common.token.db', default_value=0)

MOBILE_TOKEN_EXPIRED_SECONDS = config.get('mobile.app.token.expire.seconds', default_value=2592000)  # 30 days
WEB_TOKEN_EXPIRED_SECONDS = config.get('web.login.lock.seconds', default_value=1800)  # 30 minutes
LOGIN_FAILED_MAXIMAL_TIMES = config.get('mobile.login.max.failed.times', default_value=3)
LOGIN_FAILED_EXPIRED_SECONDS = config.get('mobile.login.lock.seconds', default_value=1800)  # 30 minutes

ALIYUN_MNS_ENABLED = config.get('aliyun.mns.enabled', 'false').lower() == 'true'
ALIYUN_MNS_ACCESS_KEY_ID = config.get('aliyun.mns.accessKeyId', default_value='mns')
ALIYUN_MNS_ACCESS_KEY_SECRET = config.get('aliyun.mns.accessKeySecret', default_value='mns')
ALIYUN_MNS_END_POINT = config.get('aliyun.mns.endpoint', default_value='https://mns')
ALIYUN_MNS_QUEUE_NAME = "LoginLog"

MQ_REDIS_DB = config.get('common.redis.db', default_value=2)

MYSQL_HOST = config.get('common.jdbc.host', default_value='192.168.33.10')
MYSQL_PORT = config.get('common.jdbc.port', default_value=3306)
MYSQL_USER = config.get('common.jdbc.username', default_value='tuotiansd')
MYSQL_PASS = config.get('common.jdbc.password', default_value='tuotiansd')

SQLALCHEMY_DATABASE_URI = 'mysql://{}:{}@{}:{}/aa?charset=utf8'.format(MYSQL_USER, MYSQL_PASS, MYSQL_HOST, MYSQL_PORT)
SQLALCHEMY_TRACK_MODIFICATIONS = False

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
SETTING_LOCAL_DIR = os.path.join(BASE_DIR, "settings_local.py")
if os.path.exists(SETTING_LOCAL_DIR):
    execfile(SETTING_LOCAL_DIR)

execfile('logging_config.py')
