# -*- coding: utf-8 -*-
import logging
import re

from rest_framework import serializers, status
from rest_framework.response import Response

from current_rest import constants, models
from current_rest.biz import PERSONAL_MAX_DEPOSIT
from current_rest.biz.current_account_manager import CurrentAccountManager
<<<<<<< HEAD
from current_rest.biz.current_daily_manager import CurrentDailyManager, calculate_success_deposit_today
=======
from current_rest.biz.current_daily_manager import CurrentDailyManager
from current_rest.models import Agent
>>>>>>> current

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
    created_time = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S')
    updated_time = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S')
    personal_max_deposit = serializers.SerializerMethodField()
    personal_available_redeem = serializers.SerializerMethodField()
    personal_max_redeem = serializers.SerializerMethodField()

    def get_personal_max_deposit(self, instance):
        user_max_deposit = PERSONAL_MAX_DEPOSIT - instance.balance if PERSONAL_MAX_DEPOSIT - instance.balance > 0 else 0
        today_sum_deposit = calculate_success_deposit_today()
        current_daily_amount = CurrentDailyManager().get_current_daily_amount()
        return min(user_max_deposit, current_daily_amount - today_sum_deposit)

<<<<<<< HEAD
=======
    def get_personal_available_redeem(self, instance):
        today = datetime.now().date()
        today_sum_redeem = models.CurrentRedeem.objects.filter(created_time__startswith=today,
                                                               current_account=instance).exclude(
            status=constants.STATUS_DENIED).aggregate(
            Sum('amount')).get('amount__sum', 0)
        today_sum_redeem = today_sum_redeem if today_sum_redeem is not None else 0
        return min(instance.balance, constants.EVERY_DAY_OF_MAX_REDEEM_AMOUNT - today_sum_redeem)

    def get_personal_max_redeem(self, instance):
        return constants.EVERY_DAY_OF_MAX_REDEEM_AMOUNT

    @staticmethod
    def __calculate_success_deposit_today():
        today = datetime.now().date()
        tomorrow = today + timedelta(1)
        amount_sum = models.CurrentDeposit.objects.filter(status=constants.DEPOSIT_SUCCESS,
                                                          updated_time__range=(today, tomorrow)) \
            .all().aggregate(Sum('amount')) \
            .get('amount__sum', 0)
        return amount_sum if amount_sum else 0

>>>>>>> current
    class Meta:
        model = models.CurrentAccount
        fields = ('balance', 'updated_time', 'created_time', 'personal_max_deposit',
                  'personal_available_redeem', 'personal_max_redeem')
        read_only_fields = ('id', 'login_name')


class DepositSerializer(serializers.ModelSerializer):
    login_name = serializers.RegexField(regex=re.compile('[A-Za-z0-9_]{6,25}'))
    amount = serializers.IntegerField(min_value=0)
    source = serializers.ChoiceField(choices=constants.SOURCE_CHOICE)
    no_password = serializers.BooleanField()

    def create(self, validated_data):
        current_account = CurrentAccountManager().fetch_account(login_name=validated_data.get('login_name'))
        validated_data['current_account'] = current_account
        return super(DepositSerializer, self).create(validated_data=validated_data)

    class Meta:
        model = models.CurrentDeposit
        fields = ('id', 'login_name', 'amount', 'source', 'no_password', 'status')


class AgentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Agent
        fields = '__all__'


class LoanSerializer(serializers.ModelSerializer):
    amount = serializers.IntegerField(min_value=0, max_value=99999)
    debtor = serializers.RegexField(regex=re.compile('[A-Za-z0-9]{6,25}'))
    effective_date = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S')
    expiration_date = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S')
    creator = serializers.RegexField(regex=re.compile('[A-Za-z0-9]{6,25}'), required=False)
    auditor = serializers.RegexField(regex=re.compile('[A-Za-z0-9]{6,25}'), required=False)

    class Meta:
        model = models.Loan
        fields = '__all__'


class LoanListSerializer(LoanSerializer):
    agent = AgentSerializer()


class CurrentRedeemSerializer(serializers.ModelSerializer):
    login_name = serializers.RegexField(regex=re.compile('[A-Za-z0-9_]{6,25}'))
    amount = serializers.IntegerField(min_value=0)

    def create(self, validated_data):
        current_account = CurrentAccountManager().fetch_account(login_name=validated_data.get('login_name'))
        validated_data['current_account'] = current_account
        return super(CurrentRedeemSerializer, self).create(validated_data=validated_data)

    def update(self, instance, validated_data):
        instance = super(CurrentRedeemSerializer, self).update(instance, validated_data)
        if validated_data['status'] == 'SUCCESS':  # TODO: replace by constants
            CurrentAccountManager().update_current_account_for_withdraw(instance.login_name, instance.amount,
                                                                        instance.id)
        return instance

    class Meta:
        model = models.CurrentRedeem
        fields = ('id', 'login_name', 'amount', 'source', 'status')
        read_only_fields = ('created_time', 'approve_time')


class FundHistoryQueryForm(serializers.Serializer):
    begin_date = serializers.DateField(input_formats=['%Y-%m-%d'])
    end_date = serializers.DateField(input_formats=['%Y-%m-%d'])


class CurrentDailyFundInfoSerializer(serializers.ModelSerializer):
    allow_change_quota = serializers.SerializerMethodField()

    def get_allow_change_quota(self, instance):
        return instance.config_quota_status in (
            constants.DAILY_QUOTA_STATUS_UNSET, constants.DAILY_QUOTA_STATUS_REFUSED)

    class Meta:
        model = models.CurrentDailyFundInfo
        fields = ('date', 'loan_remain_amount', 'quota_amount', 'config_quota_amount', 'config_quota_status',
                  'invest_amount', 'allow_change_quota')
        read_only_fields = ('date', 'loan_remain_amount', 'quota_amount', 'invest_amount', 'allow_change_quota')
