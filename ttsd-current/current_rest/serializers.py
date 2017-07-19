# -*- coding: utf-8 -*-
import logging
import re
from datetime import datetime, timedelta

from django.db.models import Sum
from rest_framework import serializers, status
from rest_framework.response import Response

from current_rest import constants, models
from current_rest.biz import PERSONAL_MAX_DEPOSIT
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.biz.current_daily_manager import CurrentDailyManager

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


class AccountSerializer(serializers.ModelSerializer):
    login_name = serializers.RegexField(regex=re.compile('[A-Za-z0-9_]{6,25}'), read_only=True)
    balance = serializers.IntegerField(min_value=0, required=True)
    created_time = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S', read_only=True)
    updated_time = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S', read_only=True)
    personal_max_deposit = serializers.SerializerMethodField()

    def get_personal_max_deposit(self, instance):
        user_max_deposit = PERSONAL_MAX_DEPOSIT - instance.balance if PERSONAL_MAX_DEPOSIT - instance.balance > 0 else 0
        today_sum_deposit = self.__calculate_success_deposit_today()
        current_daily_amount = CurrentDailyManager().get_current_daily_amount()
        return min(user_max_deposit, current_daily_amount - today_sum_deposit)

    @staticmethod
    def __calculate_success_deposit_today():
        today = datetime.now().date()
        tomorrow = today + timedelta(1)
        amount_sum = models.CurrentDeposit.objects.filter(status=constants.DEPOSIT_SUCCESS,
                                                          updated_time__range=(today, tomorrow)) \
            .all().aggregate(Sum('amount')) \
            .get('amount__sum', 0)
        return amount_sum if amount_sum else 0

    class Meta:
        model = models.CurrentAccount
        fields = ('id', 'login_name', 'balance', 'created_time', 'updated_time', 'personal_max_deposit')


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
