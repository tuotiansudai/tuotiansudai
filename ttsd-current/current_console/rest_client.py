# coding=utf-8
import ast

import logging
import requests

import settings

logger = logging.getLogger(__name__)


class RestClient(object):
    REST_URL_TEMPLATE = u'http://{host}:{port}/{applicationContext}{uri}'

    def __init__(self, uri):
        self.retries = 3
        self.url = self.REST_URL_TEMPLATE.format(host=settings.REST_SERVICE_HOST,
                                                 port=settings.REST_SERVICE_PORT,
                                                 applicationContext=settings.REST_PATH,
                                                 uri=uri)

    def get(self, params=None):
        return self._execute(requests.get, data=None, params=params)

    def post(self, data=None):
        return self._execute(requests.post, data=data, params=None)

    def put(self, data=None):

        return self._execute(requests.put, data=data, params=None)

    def _execute(self, method, data=None, params=None):
        try:
            response = method(self.url, data=data, params=params, timeout=settings.REST_TIME_OUT)
            response.raise_for_status()
            return response.json()
        except requests.Timeout:
            logger.error('url:{} timeout retries:{}'.format(self.url, self.retries))
            self.retries = self.retries - 1

            if self.retries > 0:
                return self._execute(method, data=data, params=params)
        except requests.RequestException as re:
            logger.error('内部服务器错误,原因:{}'.format(re.message))
