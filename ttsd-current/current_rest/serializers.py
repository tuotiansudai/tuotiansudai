# -*- coding: utf-8 -*-
import re

import logging
from rest_framework import serializers, status
from rest_framework.response import Response

from current_rest import constants
from current_rest import models
from current_rest.models import Loan, Agent

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


class LoanSerializer(serializers.ModelSerializer):
    id = serializers.IntegerField(required=False)
    serial_number = serializers.IntegerField(required=True)
    agent = serializers.PrimaryKeyRelatedField(queryset=Agent.objects.all(), required=True)
    amount = serializers.IntegerField(min_value=0, max_value=99999, required=True)
    loan_type = serializers.ChoiceField(choices=constants.LOAN_TYPE_CHOICES, required=True)
    debtor = serializers.RegexField(regex=re.compile('[A-Za-z0-9]{6,25}'), required=True)
    debtor_identity_card = serializers.CharField(required=True, max_length=18)
    effective_date = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S', required=True)
    expiration_date = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S', required=True)
    status = serializers.ChoiceField(choices=constants.LOAN_STATUS_CHOICES, required=True)
    creator = serializers.RegexField(regex=re.compile('[A-Za-z0-9]{6,25}'), required=False)
    auditor = serializers.RegexField(regex=re.compile('[A-Za-z0-9]{6,25}'), required=False)

    class Meta:
        model = models.Loan
        fields = ('id', 'serial_number', 'agent', 'amount', 'loan_type', 'debtor', 'debtor_identity_card',
                  'effective_date', 'expiration_date', 'status', 'create_time', 'update_time', 'creator', 'auditor')
