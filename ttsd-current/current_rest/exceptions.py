from datetime import datetime

from rest_framework import status
from rest_framework.exceptions import APIException
from rest_framework.views import exception_handler


class PayWrapperException(APIException):
    status_code = status.HTTP_500_INTERNAL_SERVER_ERROR
    default_detail = 'call pay wrapper fail.'
    default_code = 'error'

    def __init__(self, detail=None, code=None):
        APIException.__init__(self, detail, code)


def api_exception_handler(ex, context):
    response = exception_handler(ex, context)

    if response is not None:
        response.data = {
            'code': -1,
            'message': 'current rest service error',
            'exception': ex.detail,
            'detail': ex.detail,
            'path': '',
            'timestamp': datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        }
    return response
