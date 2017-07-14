from celery import Celery
from mns.account import Account
from redis import Redis

import settings

current_app = Celery('current')
current_app.config_from_object('jobs.settings')

redis_conn = Redis.from_url(settings.broker_url)
aliyun_account = Account(settings.ENDPOINT, settings.KEY, settings.SECRET, debug=settings.DEBUG)
