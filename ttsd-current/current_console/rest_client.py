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

    def execute(self, method='GET', data=None, params=None):
        response_action = {
            "GET": self.get,
            "POST": self.post,
            "PUT": self.put,
        }
        try:
            response = response_action[method](params, data)
            response.raise_for_status()
            return response.json()
        except requests.Timeout as to:
            logger.error('url:{} timeout retries:{}'.format(self.url, self.retries))
            if self.retries + 1 <= 3:
                return self.get(params)
        except requests.RequestException as re:
            logger.error('内部服务器错误,原因:{}'.format(re.message))
            return None

    def get(self, params, data):
        return requests.get(self.url, params=params, timeout=settings.REST_TIME_OUT)

    def put(self, params, data):
        return requests.put(self.url, data=data, timeout=settings.REST_TIME_OUT)

    def post(self, params, data):
        return requests.post(self.url, data=data, timeout=settings.REST_TIME_OUT)
