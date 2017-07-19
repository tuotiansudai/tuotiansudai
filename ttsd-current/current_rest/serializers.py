# -*- coding: utf-8 -*-
import logging
import re

from django.utils import timezone
from rest_framework import serializers, status
from rest_framework.response import Response

from current_rest import constants

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


class CustomTimeZoneDateTimeField(serializers.DateTimeField):
    def to_representation(self, value):
        tz = timezone.get_default_timezone()
        value = timezone.localtime(value, timezone=tz)
        return super(CustomTimeZoneDateTimeField, self).to_representation(value)


class DepositDetailSerializer(serializers.Serializer):
    id = serializers.IntegerField(required=False)
    login_name = serializers.RegexField(regex=re.compile('[A-Za-z0-9_]{6,25}'), required=True)
    amount = serializers.IntegerField(min_value=0, required=True)
    source = serializers.ChoiceField(choices=constants.SOURCE_CHOICE, required=True)
    no_password = serializers.BooleanField(required=True)
    created_time = CustomTimeZoneDateTimeField(format='%Y-%m-%d %H:%M:%S', required=True)
    updated_time = CustomTimeZoneDateTimeField(format='%Y-%m-%d %H:%M:%S', required=True)


class DepositSerializer(serializers.Serializer):
    id = serializers.IntegerField(required=False)
    login_name = serializers.RegexField(regex=re.compile('[A-Za-z0-9_]{6,25}'), required=True)
    amount = serializers.IntegerField(min_value=0, required=True)
    source = serializers.ChoiceField(choices=constants.SOURCE_CHOICE, required=True)


class DepositSuccessSerializer(serializers.Serializer):
    order_id = serializers.IntegerField(min_value=0, required=True)
    success = serializers.BooleanField(required=True)
