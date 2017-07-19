# -*- coding: utf-8 -*-
import logging
import re

from rest_framework import serializers, status
from rest_framework.response import Response

from current_rest import constants, models
from current_rest.biz.current_account_manager import CurrentAccountManager

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


class DepositDetailSerializer(serializers.Serializer):
    id = serializers.IntegerField(required=False)
    login_name = serializers.RegexField(regex=re.compile('[A-Za-z0-9_]{6,25}'), required=True)
    amount = serializers.IntegerField(min_value=0, required=True)
    source = serializers.ChoiceField(choices=constants.SOURCE_CHOICE, required=True)
    no_password = serializers.BooleanField(required=True)
    # created_time = CustomTimeZoneDateTimeField(format='%Y-%m-%d %H:%M:%S', required=True)
    updated_time = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S', required=True)


class DepositSerializer(serializers.ModelSerializer):
    login_name = serializers.RegexField(regex=re.compile('[A-Za-z0-9_]{6,25}'), required=True)
    amount = serializers.IntegerField(min_value=0, required=True)
    source = serializers.ChoiceField(choices=constants.SOURCE_CHOICE, required=True)
    no_password = serializers.BooleanField(required=True)
    created_time = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S', read_only=True)
    updated_time = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S', read_only=True)

    def create(self, validated_data):
        current_account = CurrentAccountManager().fetch_account(login_name=validated_data.get('login_name'))
        validated_data['current_account'] = current_account
        return super(DepositSerializer, self).create(validated_data=validated_data)

    class Meta:
        model = models.CurrentDeposit
        fields = ('id', 'login_name', 'amount', 'source', 'no_password', 'status', 'created_time', 'updated_time')
