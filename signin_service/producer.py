import sys

import settings
from mns.account import Account
from mns.queue import *
import redis

queue_name = settings.ALIYUN_MNS_QUEUE_NAME

class AliyunProducer():

    def __init__(self):
        accid, acckey, endpoint = settings.ALIYUN_MNS_ACCESS_KEY_ID, settings.ALIYUN_MNS_ACCESS_KEY_SECRET, settings.ALIYUN_MNS_END_POINT
        # initialize my_account, my_queue
        my_account = Account(endpoint, accid, acckey, "")
        self.my_queue = my_account.get_queue(queue_name)

    def send_message(self, message):
        # send some messages
        print "[MQ] ready to send message, queue: %s, message: %s" % (queue_name, message)
        try:
            msg = Message(message)
            re_msg = self.my_queue.send_message(msg)
            print "[MQ] send message success, queue: %s, MessageID: %s, message: %s" % (
                queue_name, re_msg.message_id, message)
        except MNSExceptionBase, e:
            if e.type == "QueueNotExist":
                print "[MQ] Queue not exist, please create queue before send message."
                sys.exit(0)
            print "[MQ] Send Message Fail! Exception:%s" % e


class RedisProducer():
    def __init__(self):
        self.pool = redis.ConnectionPool(host=settings.REDIS_HOST, port=settings.REDIS_PORT, db=settings.MQ_REDIS_DB)
        self.connection = redis.Redis(connection_pool=self.pool)

    def send_message(self, message):
        print "[MQ] ready to send message, queue: %s, message: %s" % (queue_name, message)
        self.connection.lpush("MQ:LOCAL:" + queue_name, message);
        print "[MQ] push message to queue % success, message: %s" % (queue_name, message)


producer = AliyunProducer() if settings.ALIYUN_MNS_ENABLED else RedisProducer()