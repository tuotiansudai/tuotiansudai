from time import sleep

from celery import Task
from celery.utils.log import get_task_logger
from mns.mns_exception import MNSExceptionBase

import settings
from jobs import aliyun_account, redis_conn
from jobs.client import RedisMessageClient

logger = get_task_logger(__name__)


class MessageBrokerMixin(object):
    def receive_message(self):
        func = {'test': self.redis}.get(settings.ENV, self.aliyun)
        func()

    def redis(self):
        redis_message_client = RedisMessageClient()
        local_queue_name = "MQ:LOCAL:{}".format(self.name)
        while True:
            try:
                row_msg = redis_conn.brpop(local_queue_name, timeout=settings.POP_MESSAGE_WAIT_SECONDS)
                logger.error('Receive Message Succeed! Message Body: {}'.format(row_msg))
                if row_msg:
                    _, msg = row_msg
                    try:
                        if not self.do(msg):
                            redis_message_client.send(self.name, msg)
                    except Exception, e:
                        logger.error('Message exception:{}'.format(e))
                        redis_message_client.send(self.name, msg)
            except Exception, e:
                logger.error('Pop message exception:{}'.format(e))

    def aliyun(self):
        queue = aliyun_account.get_queue(self.name)
        try:
            msg = queue.receive_message(settings.POP_MESSAGE_WAIT_SECONDS)
            logger.debug("Receive Message Succeed! ReceiptHandle:%s MessageBody:%s MessageID:%s" % (
                msg.receipt_handle, msg.message_body, msg.message_id))
            ret = self.do(msg.message_body)
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


class BaseTask(Task, MessageBrokerMixin):
    ignore_result = True
    name = "currentBase"  # this is real business queue name.
    queue = "celery.base"  # this one used by celery to distinguish different messages

    def run(self, *args, **kwargs):
        while True:
            if self.is_quit():
                logger.info('Task {} has been terminated'.format(self.name))
                return
            self.receive_message()

    def is_quit(self):
        return redis_conn.srem(settings.STOP_QUEUE_NAME, self.name)

    def do(self, message):
        raise NotImplementedError('Tasks must define the do method.')
