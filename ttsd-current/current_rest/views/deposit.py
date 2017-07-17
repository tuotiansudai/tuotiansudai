# -*- coding: utf-8 -*-
import logging

from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response

from current_rest.biz.deposit import Deposit

logger = logging.getLogger(__name__)


@api_view(['POST'])
def deposit_with_password(request, login_name):
    data = request.data
    data.update({'login_name': login_name, 'no_password': False})
    try:
        pay_response = Deposit().deposit(data=data)
        return Response(pay_response, status=status.HTTP_200_OK)
    except ValueError:
        return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['POST'])
def deposit_with_no_password(request, login_name):
    data = request.data
    data.update({'login_name': login_name, 'no_password': True})
    try:
        pay_response = Deposit().deposit(data=data)
        return Response(pay_response, status=status.HTTP_200_OK)
    except ValueError:
        return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['POST'])
def deposit_with_password_callback(request):
    data = request.data

    try:
        Deposit().deposit_with_password_callback(data=data)
        return Response(status=status.HTTP_200_OK)
    except Exception:
        return Response(status=status.HTTP_400_BAD_REQUEST)
