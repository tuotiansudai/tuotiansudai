# -*- coding: utf-8 -*-

import logging
from datetime import datetime, timedelta

import requests
from django.db.models import Sum

from current_rest import redis_client, models, constants
from current_rest.exceptions import PayWrapperException

logger = logging.getLogger(__name__)


class PayManager(object):
    @staticmethod
    def invoke_pay(data, url):

        try:
            response = requests.post(url=url, json=data, timeout=10)

            if response.status_code == requests.codes.ok:
                return response.json()
            logger.error('response code {} is not ok, request data is {}'.format(response.status_code, data))
            raise PayWrapperException('call pay wrapper fail, check current-rest log for more information')
        except Exception, e:
            raise PayWrapperException(
                'call pay wrapper fail, check current-rest log for more information, {}'.format(e))
