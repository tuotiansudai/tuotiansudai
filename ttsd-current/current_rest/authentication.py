# coding: utf-8
from rest_framework.authentication import BaseAuthentication


class NoAuthentication(BaseAuthentication):
    def authenticate(self, request):
        return None, None
