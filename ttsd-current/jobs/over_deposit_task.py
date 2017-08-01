import json

import requests
from celery.utils.log import get_task_logger

from current_rest import constants
from jobs.base import BaseTask
from jobs import current_app, settings

logger = get_task_logger(__name__)


class OverDepositTask(BaseTask):
    name = "current-over-deposit"
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
            if deposit.get('status') in [constants.DEPOSIT_PAYBACK_SUCCESS, constants.DEPOSIT_PAYBACK_FAIL]:
                return True

            if deposit.get('status') == constants.DEPOSIT_OVER_PAY:
                requests.post(url=self.pay_url, json=deposit, headers={'content-type': 'application/json'})
        except Exception, e:
            logger.error("queue: {}, message: {}, exception: {}".format(self.name, message, e))

        return False


# register task and initialize it
current_app.tasks.register(OverDepositTask())
OverDepositTask().delay()
