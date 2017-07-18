# -*- coding: utf-8 -*-
import logging

from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response

from current_rest import serializers
from current_rest.biz.deposit import Deposit
from current_rest.serializers import json_validation_required

logger = logging.getLogger(__name__)


@api_view(['GET'])
def personal_max_deposit(request, login_name):
    try:
        amount = Deposit().calculate_max_deposit(login_name=login_name)
        return Response({'amount': amount}, status=status.HTTP_200_OK)
    except ValueError:
        return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['POST'])
@json_validation_required(serializers.DepositSerializer)
def deposit_with_password(request, validated_data):
    try:
        pay_response = Deposit().deposit(no_password=False, validated_data=validated_data)
        return Response(pay_response, status=status.HTTP_200_OK)
    except ValueError:
        return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['POST'])
@json_validation_required(serializers.DepositSerializer)
def deposit_with_no_password(request, validated_data):
    try:
        pay_response = Deposit().deposit(no_password=True, validated_data=validated_data)
        return Response(pay_response, status=status.HTTP_200_OK)
    except ValueError:
        return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['POST'])
@json_validation_required(serializers.DepositSuccessSerializer)
def deposit_callback(request, validated_data):
    try:
        Deposit().deposit_callback(validated_data=validated_data)
        return Response(status=status.HTTP_200_OK)
    except Exception:
        return Response(status=status.HTTP_400_BAD_REQUEST)
