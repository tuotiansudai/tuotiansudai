# -*- coding: utf-8 -*-

import logging

import requests
from rest_framework import status

from current_rest.exceptions import PayWrapperException

logger = logging.getLogger(__name__)


def invoke_pay(data, url):
    logger.info('invoke pay starting: url={}, data={}'.format(url, data))

    try:
        response = requests.post(url=url, json=data, timeout=10)

        if status.is_success(response.status_code):
            logger.info('invoke pay completed: url={}, data={}, response={}'.format(url, data, response.json()))
            return response.json()

        logger.error('invoke pay failed: url={}, data={}, status_code={}'.format(url, data, response.status_code))

        raise PayWrapperException(detail="call pay wrapper failed: "
                                         "url={} data={} status_code={}".format(url, data, response.status_code),
                                  status_code=response.status_code)
    except Exception, e:
        logger.error('invoke pay exception: url={}, data={}, exception={}'.format(url, data, e))
        raise PayWrapperException(detail="invoke pay exception: url={} data={}".format(url, data))
