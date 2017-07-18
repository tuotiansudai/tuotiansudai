import requests
from celery.utils.log import get_task_logger

from jobs.base import BaseTask
from jobs import current_app, settings

logger = get_task_logger(__name__)


class DepositCallback(BaseTask):
    name = "current-deposit-callback"
    queue = "celery.current.deposit.callback"
    rest_url = "{}/deposit-callback".format(settings.CURRENT_REST_SERVER)

    def do(self, message):
        logger.info("queue: {}, message: {}".format(self.name, message))
        try:
            response = requests.post(url=self.rest_url,
                                     data=message,
                                     timeout=10,
                                     headers={'content-type': 'application/json'})
            return response.status_code == requests.codes.ok
        except Exception, e:
            logger.error("queue: {}, message: {}, exception: {}".format(self.name, message, e))
            return False


# register task and initialize it
current_app.tasks.register(DepositCallback())
DepositCallback().delay()
