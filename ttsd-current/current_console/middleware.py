import json

import requests
from django.http import Http404
from django.shortcuts import HttpResponseRedirect
from django.utils.deprecation import MiddlewareMixin

import settings


class TTSDSessionManager(MiddlewareMixin):
    def process_request(self, request):
        if settings.CONSOLE_ENABLED:
            if request.path != settings.REDIRECT_URL:
                token = request.GET.get('token', None)
                if token is None:
                    return HttpResponseRedirect(settings.REDIRECT_URL)

                url = settings.SIGN_IN_HOST + ':' + settings.SIGN_IN_PORT + '/session/' + token
                result = self.url_retry(request, url, 3)
                if result is None:
                    return Http404
                result = json.loads(result)
                if result.get('result', None):
                    request.session['token'] = result['token']
                    request.session['mobile'] = result['user_info']['mobile']
                    request.session['login_name'] = result['user_info']['login_name']
                    request.session['roles'] = result['user_info']['roles']
                else:
                    return HttpResponseRedirect(settings.REDIRECT_URL)
        else:
            pass

    def url_retry(self, request, url, num_retries):
        try:
            result = requests.get(url, timeout=5)
            result.raise_for_status()
            resp = result.content
        except requests.HTTPError:
            resp = None
            if num_retries > 0:
                return self.url_retry(request, url, num_retries - 1)
        except requests.exceptions.ConnectionError:
            resp = None
        return resp
