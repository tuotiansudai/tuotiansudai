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
        try:
            response = requests.get(self.url, params=params, timeout=settings.REST_TIME_OUT)
            response.raise_for_status()
            return response.json()
        except requests.Timeout as to:
            logger.error('url:{} timeout retries:{}'.format(self.url, self.retries))
            if self.retries + 1 <= 3:
                return self.get(params)
        except requests.RequestException as re:
            logger.error('内部服务器错误,原因:{}'.format(re.message))
            return None

    def post(self, data=None):
        try:
            response = requests.post(self.url, data=data, timeout=settings.REST_TIME_OUT)
            response.raise_for_status()
            return response.json()
        except requests.Timeout as to:
            logger.error('url:{} timeout retries:{}'.format(self.url, self.retries))
            if self.retries + 1 <= 3:
                return self.post(data=data)
        except requests.RequestException as re:
            logger.error('内部服务器错误,原因:{}'.format(re.message))
            return None

    def put(self, data=None):
        try:
            response = requests.put(self.url, data=data, timeout=settings.REST_TIME_OUT)
            response.raise_for_status()
            return response.json()
        except requests.Timeout as to:
            logger.error('url:{} timeout retries:{}'.format(self.url, self.retries))
            if self.retries + 1 <= 3:
                return self.put(data=data)
        except requests.RequestException as re:
            logger.error('内部服务器错误,原因:{}'.format(re.message))
            return None
