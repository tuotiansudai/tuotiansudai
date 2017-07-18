# -*- coding: utf-8 -*-
import logging
import re

from rest_framework import serializers, status
from rest_framework.response import Response

from current_rest import constants
from models import CurrentWithdraw

logger = logging.getLogger(__name__)


def json_validation_required(serializer):
    def decorator(func):
        def wrapper(request, *args, **kwargs):
            serializer_instance = serializer(data=request.data)
            if serializer_instance.is_valid():
                return func(request, serializer_instance.validated_data, *args, **kwargs)
            logger.error('request data is illegal. data is {}'.format(request.data))
            return Response(status=status.HTTP_400_BAD_REQUEST)

        return wrapper

    return decorator


class DepositSerializer(serializers.Serializer):
    id = serializers.IntegerField(required=False)
    login_name = serializers.RegexField(regex=re.compile('[A-Za-z0-9_]{6,25}'), required=True)
    amount = serializers.IntegerField(min_value=0, required=True)
    source = serializers.ChoiceField(choices=constants.SOURCE_CHOICE, required=True)


class DepositSuccessSerializer(serializers.Serializer):
    order_id = serializers.IntegerField(min_value=0, required=True)
    success = serializers.BooleanField(required=True)
    status = serializers.ChoiceField(choices=constants.DEPOSIT_STATUS_CHOICE, required=True)


class CurrentWithdrawSerializer(serializers.Serializer):

    class Meta:
        model = CurrentWithdraw
        fields = '__all__'
