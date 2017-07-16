from time import sleep

from celery import Task
from celery.utils.log import get_task_logger
from mns.account import Account
from mns.mns_exception import MNSExceptionBase

import settings
from jobs import aliyun_account, redis_conn

logger = get_task_logger(__name__)


class MessageBroker(object):
    def __init__(self, queue_name, callback_func):
        self.callback_func = callback_func
        self.queue_name = queue_name

    def receive_message(self):
        func = {'test': self.redis}.get(settings.ENV, self.aliyun)
        return func()

    def redis(self):
        while True:
            msg = redis_conn.brpop(self.queue_name)
            logger.debug('Receive Message Succeed! Message Body:{}'.format(msg))
            self.callback_func(msg)

    def aliyun(self):
        queue = aliyun_account.get_queue(self.queue_name)
        while True:
            try:
                msg = queue.receive_message(settings.POP_MESSAGE_WAIT_SECONDS)
                logger.debug("Receive Message Succeed! ReceiptHandle:%s MessageBody:%s MessageID:%s" % (
                    msg.receipt_handle, msg.message_body, msg.message_id))
                ret = self.callback_func(msg.message_body)
                if ret:
                    queue.delete_message(msg.receipt_handle)
                    logger.debug('message {} has been deleted'.format(msg.message_id))
                else:
                    logger.error('message {} executed failure, body: {}'.format(msg.message_id, msg.message_body))
            except MNSExceptionBase, e:
                if e.type == "QueueNotExist":
                    logger.error("Queue not exist, please create queue before receive message.")
                elif e.type == "MessageNotExist":
                    logger.warning("Queue is empty!")
                logger.error("Aliyun Message Fail! Exception:%s\n" % e)
                sleep(settings.POP_MESSAGE_WAIT_SECONDS)
            except Exception, e:
                logger.error('Message exception:{}'.format(e))


class BaseTask(Task):
    ignore_result = True
    name = "currentBase"  # this is real business queue name.
    queue = "celery.base"  # this one used by celery to distinguish different messages

    def run(self, *args, **kwargs):
        MessageBroker(self.name, self.do).receive_message()

    def do(self, message):
        raise NotImplementedError('Tasks must define the do method.')
