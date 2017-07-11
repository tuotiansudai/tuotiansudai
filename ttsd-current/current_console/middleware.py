import json

import requests
from django.shortcuts import HttpResponseRedirect
from django.utils.deprecation import MiddlewareMixin

import settings


class SimpleMiddleware(MiddlewareMixin):
    def process_request(self, request):
        if request.path != settings.REDIRECT_URL:
            token = request.GET.get('token', None)
            if token is None:
                return HttpResponseRedirect(settings.REDIRECT_URL)

            url = settings.SIGN_IN_HOST + ':' + settings.SIGN_IN_PORT + '/session/' + token
            result = requests.get(url)
            result = json.loads(result.content)
            if result.get('result', None):
                request.session['token'] = result['token']
                request.session['mobile'] = result['user_info']['mobile']
                request.session['login_name'] = result['user_info']['login_name']
                request.session['roles'] = result['user_info']['roles']
                return None
            else:
                return HttpResponseRedirect(settings.REDIRECT_URL)
