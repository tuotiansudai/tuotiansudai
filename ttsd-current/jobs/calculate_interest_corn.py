import json

import datetime
import requests
from celery.utils.log import get_task_logger

from jobs.base import BaseTask
from jobs import current_app, settings

logger = get_task_logger(__name__)


@current_app.task
def calculate_interest():
    calculate_interest_rest_url = "{}/account/calculate_interest_yesterday"
    yesterday = (datetime.datetime.today() - datetime.timedelta(days=1)).strftime("%Y-%m-%d")
    logger.info("[calculate_interest:]date:{} calculate_interest is beginning".format(yesterday))

    def __calculate_interest():
        try:
            response = requests.post(url=calculate_interest_rest_url.format(settings.CURRENT_REST_SERVER),
                                     data={"date": yesterday},
                                     format='json')
            logger.info("response:{}".format(response))
            return response.status_code == requests.codes.ok
        except requests.Timeout:
            logger.info("request timeout ")

    return False
