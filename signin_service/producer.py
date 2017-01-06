import sys

import settings
from mns.account import Account
from mns.queue import *
import redis

pool = redis.ConnectionPool(host=settings.REDIS_HOST, port=settings.REDIS_PORT, db=settings.MQ_REDIS_DB)
queue_name = settings.ALIYUN_MNS_QUEUE_NAME


class Producer:
    def __init__(self):
        pass

    def send_message(self, message):
        pass


class AliyunProducer(Producer):
    def __init__(self):
        Producer.__init__(self)

    def send_message(self, message):

        accid, acckey, endpoint = settings.ALIYUN_MNS_ACCESS_KEY_ID, settings.ALIYUN_MNS_ACCESS_KEY_SECRET, settings.ALIYUN_MNS_END_POINT

        # initialize my_account, my_queue
        my_account = Account(endpoint, accid, acckey, "")
        my_queue = my_account.get_queue(queue_name)

        # send some messages
        print "[MQ] ready to send message, queue: %s, message: %s" % (queue_name, message)
        try:
            msg = Message(message)
            re_msg = my_queue.send_message(msg)
            print "[MQ] send message success, queue: %s, MessageID: %s, message: %s" % (
                queue_name, re_msg.message_id, message)
        except MNSExceptionBase, e:
            if e.type == "QueueNotExist":
                print "[MQ] Queue not exist, please create queue before send message."
                sys.exit(0)
            print "[MQ] Send Message Fail! Exception:%s" % e


class RedisProducer(Producer):
    def __init__(self):
        Producer.__init__(self)
        self.connection = redis.Redis(connection_pool=pool)

    def send_message(self, message):
        print "[MQ] ready to send message, queue: %s, message: %s" % (queue_name, message)
        self.connection.lpush("MQ:LOCAL:" + queue_name, message);
        print "[MQ] push message to queue % success, message: %s" % (queue_name, message)


def get_producer():
    use_aliyun = settings.ALIYUN_MNS_ENABLED
    if use_aliyun == 1:
        return AliyunProducer()
    else:
        return RedisProducer()
