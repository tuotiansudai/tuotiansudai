import json

import requests
from celery.utils.log import get_task_logger

from jobs.base import BaseTask
from jobs import current_app, settings

logger = get_task_logger(__name__)


class DepositCallbackTask(BaseTask):
    name = settings.QueueName.DEPOSIT_CALLBACK_TASK_QUEUE
    queue = "celery.current.deposit.callback"
    rest_url = "{}/deposit/{}"

    def do(self, message):
        logger.info("queue: {}, message: {}".format(self.name, message))
        try:
            json_message = json.loads(message)
            response = requests.put(url=self.rest_url.format(settings.CURRENT_REST_SERVER,
                                                             json_message.get('id')),
                                    json=json_message,
                                    headers={'content-type': 'application/json'})
            return response.status_code == requests.codes.ok
        except Exception, e:
            logger.error("queue: {}, message: {}, exception: {}".format(self.name, message, e))
            return False


# register task and initialize it
current_app.tasks.register(DepositCallbackTask())
DepositCallbackTask().delay()
