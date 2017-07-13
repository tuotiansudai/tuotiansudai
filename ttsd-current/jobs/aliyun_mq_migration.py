import glob
import inspect
import os

from mns.mns_common import TopicHelper
from mns.queue import QueueMeta
from mns.subscription import SubscriptionNotifyContentFormat, SubscriptionMeta, MNSExceptionBase

import settings
from jobs import aliyun_account
from jobs.base import BaseTask


class AliyunMQMigration(object):

    def migrate(self):
        self.init_topic_subs()
        self.init_queues()

    def init_topic_subs(self):
        topic_urls, _ = aliyun_account.list_topic()
        print "topic_urls:", topic_urls
        for topic_name, queue_names in settings.TopicName2SubscribeQueueNames.items():
            self.init_subscribes(topic_name, queue_names)

    def init_queues(self):
        required_queue_names = self.get_required_queue_names()
        print "required_queue_names:", required_queue_names
        queue_urls, _ = aliyun_account.list_queue(prefix='current')
        existing_queue_names = set([url.split('/')[-1] for url in queue_urls])
        print "Current queues:", existing_queue_names
        need_to_create = required_queue_names - existing_queue_names
        need_to_delete = existing_queue_names - required_queue_names
        for queue_name in need_to_create:
            self.create_queue(queue_name)

        for queue_name in need_to_delete:
            self.delete_queue(queue_name)

    def get_required_queue_names(self):
        required_queue_names = set()
        for file in glob.glob(os.path.join(os.path.dirname(os.path.abspath(__file__)), "*_task.py")):
            name = os.path.splitext(os.path.basename(file))[0]
            # add package prefix to name, if required
            module = __import__(name)
            for name, obj in inspect.getmembers(module):
                if inspect.isclass(obj) and issubclass(obj, BaseTask) and obj.name != 'currentBase':
                    required_queue_names.add(obj.name)
        return required_queue_names

    def delete_queue(self, queue_name):
        my_queue = aliyun_account.get_queue(queue_name)
        try:
            my_queue.delete()
            print "Delete Queue Succeed! QueueName:%s\n" % queue_name
        except MNSExceptionBase, e:
            print "Delete Queue Fail! Exception:%s\n" % e

    def create_queue(self, queue_name):
        my_queue = aliyun_account.get_queue(queue_name)
        queue_meta = QueueMeta(polling_wait_sec=settings.POP_MESSAGE_WAIT_SECONDS,
                               vis_timeout=60 * 60,
                               logging_enabled=True)
        try:
            my_queue.create(queue_meta)
            print "Create Queue Succeed! QueueName:%s\n" % queue_name
        except MNSExceptionBase, e:
            if e.type == "QueueAlreadyExist":
                print "Queue already exist, please delete it before creating or use it directly."
            print "Create Queue Fail! Exception:%s\n" % e

    def init_subscribes(self, topic_name, required_queue_names):
        my_topic = aliyun_account.get_topic(topic_name)
        subs, _ = my_topic.list_subscription(prefix='current')
        print "Current subscriptions:", subs
        existing_sub_names = set([sub.split('/')[-1] for sub in subs])
        need_to_create = required_queue_names - existing_sub_names
        need_to_delete = existing_sub_names - required_queue_names

        for queue_name in need_to_create:
            self.create_subscribe(my_topic, queue_name)

        for queue_name in need_to_delete:
            my_sub = my_topic.get_subscription(queue_name)
            try:
                print "Going to unsubscribe {} from topic {}".format(queue_name, my_topic.topic_name)
                my_sub.unsubscribe()
                print "Successfully unsubscribe {} from topic {}".format(queue_name, my_topic.topic_name)
            except MNSExceptionBase, e:
                print "Unsubscription Fail! Exception:%s\n" % e

    def create_subscribe(self, my_topic, queue_name):
        queue_endpoint = TopicHelper.generate_queue_endpoint(settings.ALIYUN_REGION, settings.ALIYUN_ACCOUNT_ID, queue_name)
        my_sub = my_topic.get_subscription(queue_name)
        sub_meta = SubscriptionMeta(queue_endpoint,
                                    notify_content_format=SubscriptionNotifyContentFormat.SIMPLIFIED)
        try:
            subscription_url = my_sub.subscribe(sub_meta)
            print "Successfully subscribe {} to topic {}".format(subscription_url, my_topic.topic_name)
        except MNSExceptionBase, e:
            if e.type == "TopicNotExist":
                print "Topic not exist, please create topic."
            elif e.type == "SubscriptionAlreadyExist":
                print "Subscription already exist, please unsubscribe or use it directly."
            print "Create Subscription Fail! Exception:%s\n" % e


if __name__ == '__main__':
    AliyunMQMigration().migrate()