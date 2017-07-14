import logging
from mns.account import Account
from mns.queue import Message
from mns.topic import TopicMessage, MNSExceptionBase
from redis import Redis

import settings
from jobs import redis_conn, aliyun_account

logger = logging.getLogger(__file__)


class MessageClient(object):
    def __init__(self, queue_or_topic_name):
        self.queue_or_topic_name = queue_or_topic_name
        self.instance = {'test': RedisMessageClient}.get(settings.ENV, AliyunMessageClient)()

    def publish(self, message):
        self.instance.publish(self.queue_or_topic_name, message)

    def send(self, message):
        self.instance.send(self.queue_or_topic_name, message)


class RedisMessageClient(object):
    def publish(self, topic_name, message):
        subscribes = settings.TopicName2SubscribeQueueNames[topic_name]
        [self.send(queue_name, message) for queue_name in subscribes]

    def send(self, queue_name, message):
        redis_conn.lpush(queue_name, message)


class AliyunMessageClient(object):
    def publish(self, topic_name, message):
        topic = aliyun_account.get_topic(topic_name)
        try:
            msg = topic.publish_message(TopicMessage(message))
            logger.debug("Publish Message Succeed. MessageBody:%s MessageID:%s" % (message, msg.message_id))
        except MNSExceptionBase, e:
            if e.type == "TopicNotExist":
                logger.error("Topic not exist, please create it.")
            else:
                logger.error("Publish Message Fail. Exception:%s" % e)

    def send(self, queue_name, message):
        my_queue = aliyun_account.get_queue(queue_name)
        try:
            msg = Message(message)
            re_msg = my_queue.send_message(msg)
            logger.debug("Send Message Succeed! MessageBody:%s MessageID:%s" % (message, re_msg.message_id))
        except MNSExceptionBase, e:
            if e.type == "QueueNotExist":
                logger.error("Queue not exist, please create queue before send message.")
            else:
                logger.error("Send Message Fail! Exception:%s\n" % e)
