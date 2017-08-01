import datetime

import requests
from celery.utils.log import get_task_logger

from jobs import current_app, settings

logger = get_task_logger(__name__)


@current_app.task(bind=True, default_retry_delay=10, max_retries=3)
def calculate_interest(self):
    calculate_interest_rest_url = "{}/account/calculate_interest_yesterday"
    yesterday = (datetime.datetime.today() - datetime.timedelta(days=1)).strftime("%Y-%m-%d")
    logger.info("[calculate_interest:]date:{} calculate_interest is beginning".format(yesterday))

    try:
        response = requests.post(url=calculate_interest_rest_url.format(settings.CURRENT_REST_SERVER),
                                 data={'yesterday': yesterday})
        logger.info("{} interest is calculated".format(yesterday))
        return response.status_code == requests.codes.ok
    except Exception as e:
        logger.error("{} call calculate interest fail,exception: {}".format(yesterday, e))
        raise self.retry(exc=e, countdown=20)
