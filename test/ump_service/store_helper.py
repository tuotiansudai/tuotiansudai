import redis
from test.ump_service.constants import REDIS_HOST, FRONTEND_NOTIFY_CHANNEL, BACKEND_NOTIFY_CHANNEL


class Store(object):
    def __init__(self, user_id=None):
        self.user_id = user_id
        self.connection = redis.Redis(host=REDIS_HOST, db=10)

    def set(self, key, value):
        self.connection.hset("user_id:{0}".format(self.user_id), key, value)

    def set_frontend_notify(self, params):
        self.connection.rpush(FRONTEND_NOTIFY_CHANNEL, params)

    def set_backend_notify(self, params):
        self.connection.rpush(BACKEND_NOTIFY_CHANNEL, params)

    def wait_until_new_msg(self, channel):
        return self.connection.blpop(channel)[1]
