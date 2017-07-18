# -*- coding: utf-8 -*-
import logging
from datetime import datetime, timedelta

import requests
from django.db import transaction
from django.db.models import Sum

from current_rest import serializers, constants
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.biz.current_daily_manager import CurrentDailyManager
from current_rest.models import CurrentDeposit, CurrentAccount
from settings import PAY_WRAPPER_HOST

logger = logging.getLogger(__name__)


class Deposit(object):
    pay_with_password_url = '{}/deposit-with-password/'.format(PAY_WRAPPER_HOST)
    pay_with_no_password_url = '{}/deposit-with-no-password/'.format(PAY_WRAPPER_HOST)
    personal_max_deposit = 50000000

    def __init__(self):
        self.current_account_manager = CurrentAccountManager()
        self.current_daily_manager = CurrentDailyManager()

    def calculate_max_deposit(self, login_name):
        current_account_filter = CurrentAccount.objects.filter(login_name=login_name)
        balance = current_account_filter.first().balance if current_account_filter.exists() else 0
        user_max_deposit = self.personal_max_deposit - balance if self.personal_max_deposit - balance > 0 else 0
        today_sum_deposit = self.__calculate_success_deposit_today()
        current_daily_amount = self.current_daily_manager.get_current_daily_amount()
        return min(user_max_deposit, current_daily_amount - today_sum_deposit)

    @transaction.atomic
    def deposit(self, no_password, validated_data):
        current_account = self.current_account_manager.fetch_account(login_name=validated_data.get('login_name'))

        current_deposit = CurrentDeposit.objects.create(current_account=current_account,
                                                        login_name=validated_data.get('login_name'),
                                                        amount=validated_data.get('amount'),
                                                        source=validated_data.get('source', constants.SOURCE_WEB),
                                                        no_password=no_password)

        return self.__invoke_pay(current_deposit)

    @transaction.atomic
    def deposit_callback(self, validated_data):
        order_id = validated_data.get('order_id')
        success = validated_data.get('success')

        if not CurrentDeposit.objects.filter(pk=order_id).exists():
            logger.error('order id {} does not exist', order_id)
            return

        deposit = CurrentDeposit.objects.get(pk=order_id)

        if deposit.status != constants.DEPOSIT_WAITING_PAY:
            logger.error('order id {} had already updated', order_id)
            return

        deposit.status = constants.DEPOSIT_SUCCESS if success else constants.DEPOSIT_FAIL
        deposit.save()

        if success:
            self.current_account_manager.update_current_account_for_deposit(login_name=deposit.login_name,
                                                                            amount=deposit.amount,
                                                                            order_id=deposit.pk)

    def __invoke_pay(self, current_deposit):
        data = serializers.DepositSerializer(instance=current_deposit).data
        response = requests.post(
            url=self.pay_with_no_password_url if current_deposit.no_password else self.pay_with_password_url,
            json=data,
            timeout=5)

        if response.status_code == requests.codes.ok:
            return response.json()

        logger.error('response code {} is not ok, request data is {}'.format(response.status_code, data))

        raise requests.exceptions.HTTPError

    @staticmethod
    def __calculate_success_deposit_today():
        today = datetime.now().date()
        tomorrow = today + timedelta(1)
        amount_sum = CurrentDeposit.objects.filter(status=constants.DEPOSIT_SUCCESS,
                                                   updated_time__range=(today, tomorrow))\
            .all().aggregate(Sum('amount'))\
            .get('amount__sum', 0)
        return amount_sum if amount_sum else 0
