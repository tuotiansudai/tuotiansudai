import json

import requests
from celery.utils.log import get_task_logger

from jobs.base import BaseTask
from jobs import current_app, settings

logger = get_task_logger(__name__)


class LoanOutCallbackTask(BaseTask):
    name = settings.QueueName.LOAN_OUT_CALLBACK_TASK_QUEUE
    queue = "celery.current.loan_out.callback"
    rest_url = "{}/loan-out/{}"

    def do(self, message):
        logger.info("queue: {}, message: {}".format(self.name, message))
        try:
            json_message = json.loads(message)
            response = requests.put(url=self.rest_url.format(settings.CURRENT_REST_SERVER,
                                                             json_message.get('id')),
                                    json={'status': json_message.get('status')},
                                    headers={'content-type': 'application/json'})

            log_message = "queue: {}, message: {}, " \
                          "call current rest loan out, response {}".format(self.name, message, response.status_code)
            if response.ok:
                logger.info(log_message)
            else:
                logger.error(log_message)

            return response.ok
        except Exception, e:
            logger.error("queue: {}, message: {}, "
                         "call current rest loan out exception {}".format(self.name, message, e))

        return False


# register task and initialize it
current_app.tasks.register(LoanOutCallbackTask())
LoanOutCallbackTask().delay()
