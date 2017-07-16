DEBUG = True
ENV = 'test'
#celery configuration
broker_url = 'redis://192.168.1.30/1'
task_serializer = 'json'
accept_content = ['json']
#--------------------

ALIYUN_ACCOUNT_ID = '1645778055702082'
ALIYUN_REGION = 'cn-hangzhou'
ENDPOINT = 'http://1645778055702082.mns.cn-hangzhou.aliyuncs.com/'
KEY = 'LTAIxWz0o0ulReC1'
SECRET = 'DKG2r30LIf6TSuXUodHQWvfWLuthNh'
POP_MESSAGE_WAIT_SECONDS = 30

TopicName2SubscribeQueueNames = {'invest': {'currentBase', 'currentInvestCallback'}}