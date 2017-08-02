import os

BASE_DIR = os.path.dirname(os.path.abspath(__file__))

DEBUG = True
ENV = 'test'
# celery configuration
broker_url = 'redis://192.168.33.10/2'
task_serializer = 'json'
accept_content = ['json']

STOP_QUEUE_NAME = 'MQ:STOP'

ALIYUN_ACCOUNT_ID = '1645778055702082'
ALIYUN_REGION = 'cn-hangzhou'
ENDPOINT = 'http://1645778055702082.mns.cn-hangzhou.aliyuncs.com/'
KEY = 'LTAIxWz0o0ulReC1'
SECRET = 'DKG2r30LIf6TSuXUodHQWvfWLuthNh'
POP_MESSAGE_WAIT_SECONDS = 30

TopicName2SubscribeQueueNames = {'invest': {'currentBase', 'currentInvestCallback'}}

CURRENT_REST_SERVER = 'http://localhost:8000/rest'
PAY_WRAPPER_SERVER = 'http://localhost:9080/current'

setting_local_file = '/workspace/deploy-config/ttsd-current/jobs_settings.py'
if not os.path.isfile(setting_local_file):
    setting_local_file = os.path.join(BASE_DIR, "settings_local.py")