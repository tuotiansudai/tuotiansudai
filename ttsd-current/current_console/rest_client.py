# coding=utf-8
import ast

import logging
import requests

import settings

logger = logging.getLogger(__name__)


class RestClient(object):
    REST_URL_TEMPLATE = u'http://{host}:{port}/{applicationContext}{uri}'

    def __init__(self, uri):
        self.retries = 1
        self.url = self.REST_URL_TEMPLATE.format(host=settings.REST_SERVICE_HOST,
                                                 port=settings.REST_SERVICE_PORT,
                                                 applicationContext=settings.REST_PATH,
                                                 uri=uri)

    def get(self, params=None):
        return self._execute('GET', params)

    def post(self, data=None):
        return self._execute('POST', data)

    def put(self, data=None):

        return self._execute('PUT', data)

    def _execute(self, method, data=None, params=None):
        try:
            if str(method).upper() == 'POST':
                response = requests.post(self.url, data=data, timeout=settings.REST_TIME_OUT)
            elif str(method).upper() == 'PUT':
                response = requests.put(self.url, data=data, timeout=settings.REST_TIME_OUT)
            else:
                response = requests.get(self.url, params=params, timeout=settings.REST_TIME_OUT)
            response.raise_for_status()
            return response.json()
        except requests.Timeout:
            logger.error('url:{} timeout retries:{}'.format(self.url, self.retries))
            if self.retries + 1 <= 3:
                return self._execute(data=data, params=params)
        except requests.RequestException as re:
            logger.error('内部服务器错误,原因:{}'.format(re.message))
            return None
