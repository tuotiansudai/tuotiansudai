# coding: utf-8

import redis
from django.conf import settings

_config = settings.REDIS_CONFIG
_client = redis.StrictRedis(host=_config['host'], port=_config['port'], db=_config['db'])


def exists(key):
    return _client.exists(key)


def setex(key, seconds, value):
    return _client.setex(key, seconds, value)


def get(key):
    return _client.get(key)


def delete(key):
    return _client.delete(key)


def get_calculate_interest_key(date):
    return 'interest:' + date


