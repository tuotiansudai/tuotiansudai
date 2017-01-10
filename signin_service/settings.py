import os

ENV = "Dev"

DEBUG = True

REDIS_HOST = "192.168.33.10"
REDIS_PORT = 6379
REDIS_DB = 0
MOBILE_TOKEN_EXPIRED_SECONDS = 60 * 60 * 24 * 30 # 30 days
WEB_TOKEN_EXPIRED_SECONDS = 60 * 30
LOGIN_FAILED_MAXIMAL_TIMES = 3
LOGIN_FAILED_EXPIRED_SECONDS = 60 * 30

ALIYUN_MNS_ACCESS_KEY_ID = "mns"
ALIYUN_MNS_ACCESS_KEY_SECRET = "mns"
ALIYUN_MNS_END_POINT = "https://mns"
ALIYUN_MNS_QUEUE_NAME ="LoginLog"

MQ_REDIS_DB = 2

SQLALCHEMY_DATABASE_URI = "mysql://tuotiansd:tuotiansd@192.168.33.10/aa?charset=utf8"
SQLALCHEMY_TRACK_MODIFICATIONS = False


BASE_DIR = os.path.dirname(os.path.abspath(__file__))
SETTING_LOCAL_DIR = os.path.join(BASE_DIR, "settings_local.py")
if os.path.exists(SETTING_LOCAL_DIR):
    execfile(SETTING_LOCAL_DIR)

execfile('logging_config.py')