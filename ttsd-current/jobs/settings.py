from celery.schedules import crontab
import os
from celery.schedules import crontab

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


class QueueName(object):
    DEPOSIT_CALLBACK_TASK_QUEUE = 'current-deposit-callback'
    OVER_DEPOSIT_TASK_QUEUE = 'current-over-deposit'
    REDEEM_CALLBACK_TASK_QUEUE = 'current-redeem-callback'


CURRENT_REST_SERVER = 'http://localhost:8000/rest'
PAY_WRAPPER_SERVER = 'http://localhost:9080/current'

timezone = 'Asia/Shanghai'
beat_schedule = {
    'add-every-day-morning': {
        'task': 'jobs.loan_matching_cron.loan_matching',
        'schedule': crontab(hour='1,2,3,4')
    },

}

setting_local_file = '/workspace/deploy-config/ttsd-current/jobs_settings.py'
if not os.path.isfile(setting_local_file):
    setting_local_file = os.path.join(BASE_DIR, "settings_local.py")

if os.path.isfile(setting_local_file):
    exec (compile(open(setting_local_file).read(), setting_local_file, 'exec'))

timezone = 'Asia/Shanghai'

beat_schedule = {
    'calculate-interest-every-day-morning': {
        'task': 'jobs.calculate_interest_cron.calculate_interest',
        'schedule': crontab(minute='10', hour='0')
    }
}
