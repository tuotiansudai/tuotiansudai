# coding=utf-8
import ast

import requests

import settings


class RestClient(object):
    REST_URL_TEMPLATE = u'http://{host}:{port}/{applicationContext}{uri}'

    def __init__(self, uri):
        self.url = self.REST_URL_TEMPLATE \
            .replace('{host}', settings.REST_SERVICE_HOST) \
            .replace('{port}', settings.REST_SERVICE_PORT) \
            .replace('{applicationContext}', settings.REST_PATH) \
            .replace('{uri}', uri)

    def execute(self, data=None, retries=0, method=None, params=None):
        response = None
        try:

            if method is not None and str(method).upper() == 'POST':
                response = requests.post(self.url, data=data, timeout=settings.REST_TIME_OUT)
            elif method is not None and str(method).upper() == 'PUT':
                response = requests.put(self.url, data=data, timeout=settings.REST_TIME_OUT)
            else:
                response = requests.get(self.url, params=params, timeout=settings.REST_TIME_OUT)
            response.raise_for_status()
            return response.json()

        except requests.HTTPError as he:
            print he
            if retries > 0:
                return self.execute(data, retries - 1, method, params)
        except requests.exceptions.ConnectionError as ce:
            print ce
            return response
        except requests.exceptions.RequestException as re:
            print re
            return response
