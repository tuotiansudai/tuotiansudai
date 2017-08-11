import json

import requests
from celery.utils.log import get_task_logger

from common import constants as common_constants
from jobs import current_app, settings
from jobs.base import BaseTask

logger = get_task_logger(__name__)


class OverDepositTask(BaseTask):
    name = settings.QueueName.OVER_DEPOSIT_TASK_QUEUE
    queue = "celery.current.over.deposit"
    pay_url = "{}/over-deposit".format(settings.PAY_WRAPPER_SERVER)

    def do(self, message):
        logger.info("queue: {}, message: {}".format(self.name, message))
        try:
            json_message = json.loads(message)
            deposit_id = json_message.get('id')
            rest_response = requests.get(url="{}/deposit/{}".format(settings.CURRENT_REST_SERVER, deposit_id),
                                         headers={'content-type': 'application/json'})
            deposit = rest_response.json()
            if deposit.get('status') in [common_constants.DepositStatusType.DEPOSIT_PAYBACK_SUCCESS,
                                         common_constants.DepositStatusType.DEPOSIT_PAYBACK_FAIL]:
                return True

            if deposit.get('status') == common_constants.DepositStatusType.DEPOSIT_OVER_PAY:
                requests.post(url=self.pay_url, json=deposit, headers={'content-type': 'application/json'})
        except Exception, e:
            logger.error("queue: {}, message: {}, exception: {}".format(self.name, message, e))

        return False


# register task and initialize it
current_app.tasks.register(OverDepositTask())
OverDepositTask().delay()
