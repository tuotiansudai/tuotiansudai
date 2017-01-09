import settings
from mns.account import Account
from mns.queue import *
import redis
from logging_config import logger


class AliyunProducer(object):
    def __init__(self):
        # initialize my_account, my_queue
        my_account = Account(settings.ALIYUN_MNS_END_POINT, settings.ALIYUN_MNS_ACCESS_KEY_ID,
                             settings.ALIYUN_MNS_ACCESS_KEY_SECRET, "")
        self.my_queue = my_account.get_queue(settings.ALIYUN_MNS_QUEUE_NAME)

    def send_message(self, message):
        # send some messages
        logger.info("[MQ] ready to send message, queue: %s, message: %s" % (settings.ALIYUN_MNS_QUEUE_NAME, message))
        try:
            msg = Message(message)
            re_msg = self.my_queue.send_message(msg)
            logger.info("[MQ] send message success, queue: %s, MessageID: %s, message: %s" % (
                settings.ALIYUN_MNS_QUEUE_NAME, re_msg.message_id, message))
        except MNSExceptionBase, e:
            if e.type == "QueueNotExist":
                logger.error("[MQ] Send Message Fail! Queue not exist, queue name:%s" % settings.ALIYUN_MNS_QUEUE_NAME)
            logger.error("[MQ] Send Message Fail! Exception:%s" % e)


class RedisProducer(object):
    def __init__(self):
        self.pool = redis.ConnectionPool(host=settings.REDIS_HOST, port=settings.REDIS_PORT, db=settings.MQ_REDIS_DB)
        self.connection = redis.Redis(connection_pool=self.pool)

    def send_message(self, message):
        logger.info("[MQ] ready to send message, queue: %s, message: %s" % (settings.ALIYUN_MNS_QUEUE_NAME, message))
        self.connection.lpush("MQ:LOCAL:" + settings.ALIYUN_MNS_QUEUE_NAME, message)
        logger.info("[MQ] push message to queue % success, message: %s" % (settings.ALIYUN_MNS_QUEUE_NAME, message))


producer = AliyunProducer() if settings.ALIYUN_MNS_ENABLED else RedisProducer()
