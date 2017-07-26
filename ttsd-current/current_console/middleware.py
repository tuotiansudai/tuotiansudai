import logging

import requests
from django.conf import settings
from django.shortcuts import HttpResponseRedirect
from django.utils.deprecation import MiddlewareMixin

logger = logging.getLogger(__name__)


class TTSDSessionManager(MiddlewareMixin):
    token_authentication_url = '{}:{}/session/'.format(settings.SIGN_IN_HOST, settings.SIGN_IN_PORT)

    def process_request(self, request):

        if request.path == settings.LOGIN_URL:
            return

        token = request.GET.get('token', None) if request.GET.get('token', None) else request.session['token']
        if token is None:
            return HttpResponseRedirect(settings.LOGIN_URL)

        result = self.authenticate(token)

        if result is None or not result.get('result', False):
            return HttpResponseRedirect(settings.LOGIN_URL)

        request.session['token'] = result['token']
        request.session['mobile'] = result['user_info']['mobile']
        request.session['login_name'] = result['user_info']['login_name']
        request.session['roles'] = result['user_info']['roles']

    def authenticate(self, token, num_retries=3):
        url = self.token_authentication_url + token
        try:
            response = requests.get(url, timeout=5)
            response.raise_for_status()
            return response.json()
        except (requests.HTTPError, requests.exceptions.ConnectionError, ValueError) as e:
            if num_retries > 0:
                return self.authenticate(token=token, num_retries=num_retries - 1)
            else:
                logger.error('token authenticate fail, exception is {}'.format(e))
