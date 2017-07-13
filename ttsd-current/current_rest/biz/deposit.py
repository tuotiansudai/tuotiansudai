# -*- coding: utf-8 -*-
import datetime
import logging

import requests
from django.db import transaction

from current_rest import serializers, constants
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.models import CurrentDeposit
from settings import PAY_WRAPPER_HOST

logger = logging.getLogger(__name__)


class Deposit(object):
    pay_with_password_url = '{}/deposit-with-password/'.format(PAY_WRAPPER_HOST)
    pay_with_no_password_url = '{}/deposit-with-no-password/'.format(PAY_WRAPPER_HOST)

    def __init__(self):
        self.current_account_manager = CurrentAccountManager()

    @transaction.atomic
    def deposit(self, data):
        serializer = serializers.DepositSerializer(data=data)

        if not serializer.is_valid():
            logger.error(serializer.errors)
            raise ValueError

        login_name = serializer.validated_data.get('login_name')
        amount = serializer.validated_data.get('amount')
        source = serializer.validated_data.get('source', constants.SOURCE_WEB)
        no_password = serializer.validated_data.get('no_password')
        current_account = self.current_account_manager.fetch_account(login_name=login_name)

        current_deposit = CurrentDeposit.objects.create(current_account=current_account,
                                                        login_name=login_name,
                                                        amount=amount,
                                                        source=source,
                                                        no_password=no_password)

        return self.__invoke_pay(current_deposit)

    @transaction.atomic
    def deposit_with_password_callback(self, data):
        serializer = serializers.DepositSuccessSerializer(data=data)

        if not serializer.is_valid():
            logger.error(serializer.errors)
            raise ValueError

        order_id = serializer.validated_data.get(u'order_id')
        status = serializer.validated_data.get(u'status')

        if not CurrentDeposit.objects.filter(pk=order_id).exists():
            logger.error('order id {} does not exist', order_id)
            raise ValueError

        deposit = CurrentDeposit.objects.get(pk=order_id)
        deposit.status = status
        deposit.updated_time = datetime.datetime.now()

        deposit.save()

        if constants.DEPOSIT_SUCCESS == status:
            self.current_account_manager.update_current_account(login_name=deposit.login_name,
                                                                amount=deposit.amount,
                                                                bill_type=constants.BILL_TYPE_DEPOSIT,
                                                                order_id=deposit.pk,
                                                                updated_time=deposit.updated_time)

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
