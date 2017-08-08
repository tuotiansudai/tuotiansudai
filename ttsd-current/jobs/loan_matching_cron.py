# -*- coding: utf-8 -*-
import datetime

import requests
from celery.utils.log import get_task_logger

from jobs import current_app
from jobs import settings

logger = get_task_logger(__name__)


@current_app.task
def loan_matching():
    logger.info("[loan_matching:]date:{} loan matching  begin ...".format(
        datetime.datetime.today().strftime("%Y-%m-%d")))
    if not _notify_loan_matching():
        pass
        # todo:发送短信通知 债权匹配失败
    logger.info("[loan_matching:]date:{} loan matching  end ...".format(
        datetime.datetime.today().strftime("%Y-%m-%d")))


def _notify_loan_matching(retries=3):
    try:
        response = requests.get(url="{}/loan-matching".format(settings.CURRENT_REST_SERVER), timeout=10)
        return response.status_code == requests.codes.ok
    except Exception as e:
        logger.error("[loan_matching:] loan matching error exception: {},retries:{}".format(e, retries))
        retries -= 1
        if retries > 0:
            return _notify_loan_matching(retries)
        # todo:发送短信通知rest 挂掉了
        pass
