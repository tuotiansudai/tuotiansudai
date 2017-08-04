from datetime import datetime

from django.http import Http404
from rest_framework import status
from rest_framework.exceptions import APIException
from rest_framework.views import exception_handler


class PayWrapperException(APIException):
    status_code = status.HTTP_500_INTERNAL_SERVER_ERROR
    default_detail = 'call pay wrapper fail.'
    default_code = 'error'

    def __init__(self, detail=None, code=None, status_code=None):
        super(PayWrapperException, self).__init__(detail=detail, code=code)
        if status_code is not None:
            self.status_code = status_code


def api_exception_handler(ex, context):
    response = exception_handler(ex, context)

    if not isinstance(ex, Http404) and response is not None:
        response.data = {
            'message': 'current rest service error',
            'status_code': ex.status_code,
            'exception': ex.detail,
            'detail': ex.detail,
            'path': context['request'].path,
            'timestamp': datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        }
    return response
