# -*- coding: utf-8 -*-
import logging
import re
from datetime import datetime

from django.db.models import Sum
from rest_framework import serializers

from common import constants as common_constants
from current_rest import constants, models
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.biz.current_daily_manager import CurrentDailyManager, sum_success_deposit_by_date
from current_rest.models import Agent

logger = logging.getLogger(__name__)


class AccountSerializer(serializers.ModelSerializer):
    personal_max_deposit = serializers.SerializerMethodField()
    personal_available_redeem = serializers.SerializerMethodField()
    personal_max_redeem = serializers.SerializerMethodField()

    def get_personal_max_deposit(self, instance):
        user_max_deposit = constants.PERSONAL_MAX_DEPOSIT - instance.balance if constants.PERSONAL_MAX_DEPOSIT - instance.balance > 0 else 0
        today_sum_deposit = sum_success_deposit_by_date(datetime.now().date())
        current_daily_amount = CurrentDailyManager().get_current_daily_amount()
        return min(user_max_deposit, current_daily_amount - today_sum_deposit)

    def get_personal_available_redeem(self, instance):
        today = datetime.now().date()
        today_sum_redeem = models.CurrentRedeem.objects.filter(created_time__startswith=today,
                                                               current_account=instance).exclude(
            status=constants.REDEEM_REJECT).aggregate(
            Sum('amount')).get('amount__sum', 0)
        today_sum_redeem = today_sum_redeem if today_sum_redeem is not None else 0
        return min(instance.balance, constants.EVERY_DAY_OF_MAX_REDEEM_AMOUNT - today_sum_redeem)

    def get_personal_max_redeem(self, instance):
        return constants.EVERY_DAY_OF_MAX_REDEEM_AMOUNT

    class Meta:
        model = models.CurrentAccount
        fields = ('login_name', 'username', 'mobile', 'balance',
                  'personal_max_deposit', 'personal_available_redeem', 'personal_max_redeem')
        read_only_fields = ('id', 'updated_time', 'created_time',
                            'personal_max_deposit', 'personal_available_redeem', 'personal_max_redeem')


class DepositSerializer(serializers.ModelSerializer):
    login_name = serializers.RegexField(regex=re.compile('[A-Za-z0-9_]{6,25}'))
    amount = serializers.IntegerField(min_value=0)
    source = serializers.ChoiceField(choices=common_constants.SourceType.SOURCE_CHOICE)
    no_password = serializers.BooleanField()
    updated_time = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S', required=False)
    current_account = AccountSerializer(required=False)

    def create(self, validated_data):
        current_account = CurrentAccountManager().fetch_account(login_name=validated_data.get('login_name'))
        validated_data['current_account'] = current_account
        return super(DepositSerializer, self).create(validated_data=validated_data)

    class Meta:
        model = models.CurrentDeposit
        fields = ('id', 'current_account', 'login_name', 'amount', 'source', 'no_password', 'updated_time', 'status')


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
    created_time = serializers.DateTimeField(format("%Y-%m-%d %H:%M:%S"), required=False)
    approved_time = serializers.DateTimeField(format("%Y-%m-%d %H:%M:%S"), required=False)
    current_account = AccountSerializer(required=False)

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
        fields = '__all__'
        read_only_fields = ('created_time', 'approved_time', 'current_account')


class FundHistoryQueryForm(serializers.Serializer):
    begin_date = serializers.DateField(input_formats=['%Y-%m-%d'])
    end_date = serializers.DateField(input_formats=['%Y-%m-%d'])


class FundDistributionQueryForm(FundHistoryQueryForm):
    granularity = serializers.CharField(max_length=10)


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


class LoanOutHistorySerializer(serializers.ModelSerializer):
    bill_date = serializers.DateField(format='%Y-%m-%d')
    created_time = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S', required=False)
    updated_time = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S', required=False)

    class Meta:
        model = models.CurrentLoanOutHistory
        fields = '__all__'
        read_only_fields = ('reserve_account', 'agent_account', 'interest_amount',
                            'deposit_amount', 'bill_date', 'created_time', 'updated_time')
