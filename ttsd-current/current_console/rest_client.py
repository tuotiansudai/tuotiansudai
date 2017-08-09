# coding=utf-8

import logging

import requests
from django.conf import settings

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
        status_code, json = self._execute(requests.get, params=params)
        if status_code < 400:
            return json

    def post(self, data=None):
        status_code, json = self._execute(requests.post, data=data)
        if status_code < 400:
            return json

    def post_status_and_response(self, data=None):
        return self._execute(requests.post, data=data)

    def put(self, data=None):
        status_code, json = self._execute(requests.get, data=data)
        if status_code < 400:
            return json

    def put_status_and_response(self, data=None):
        return self._execute(requests.put, data=data)

    def _execute(self, method, data=None, params=None):
        try:
            response = method(self.url, data=data, params=params, timeout=settings.REST_TIME_OUT)
            return response.status_code, response.json()
        except requests.Timeout:
            logger.error('url:{} timeout retries:{}'.format(self.url, self.retries))
            self.retries = self.retries - 1

            if self.retries > 0:
                return self._execute(method, data=data, params=params)