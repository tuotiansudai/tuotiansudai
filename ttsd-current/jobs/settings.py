from celery.schedules import crontab

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

CURRENT_REST_SERVER = 'http://localhost:8000'

timezone = 'Asia/Shanghai'

beat_schedule = {
        'calculate-interest-every-day-morning': {
        'task': 'jobs.calculate_interest_cron.calculate_interest',
        'schedule': crontab(minute='10', hour='0')
    }
}

