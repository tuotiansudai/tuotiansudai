import json

import requests
from celery.utils.log import get_task_logger

from jobs.base import BaseTask
from jobs import current_app, settings

logger = get_task_logger(__name__)


class RedeemCallbackTask(BaseTask):
    name = settings.REDEEM_CALLBACK_TASK_QUEUE
    queue = "celery.current.redeem.callback"
    rest_url = "{}/redeem/{}"

    def do(self, message):
        logger.info("queue: {}, message: {}".format(self.name, message))
        try:
            json_message = json.loads(message)
            response = requests.put(url=self.rest_url.format(settings.CURRENT_REST_SERVER,
                                                             json_message.get('id')),
                                    json={'status': json_message.get('status')},
                                    headers={'content-type': 'application/json'})
            return response.status_code == requests.codes.ok
        except Exception, e:
            logger.error("queue: {}, message: {}, exception: {}".format(self.name, message, e))
            return False


# register task and initialize it
current_app.tasks.register(RedeemCallbackTask())
RedeemCallbackTask().delay()
