# -*- coding: utf-8 -*-
import logging

import datetime
import requests
from django.db import transaction

from current_rest import serializers
from current_rest.models import CurrentAccount, CurrentDeposit
from settings import PAY_WRAPPER_HOST

logger = logging.getLogger(__name__)


class Deposit(object):
    @transaction.atomic
    def deposit_with_password(self, data):
        serializer = serializers.DepositSerializer(data=data)

        if not serializer.is_valid():
            logger.error(serializer.errors)
            raise ValueError

        login_name = serializer.validated_data.get(u'login_name')
        amount = serializer.validated_data.get(u'amount')
        current_account = CurrentAccount.objects.get(login_name=login_name) \
            if CurrentAccount.objects.filter(login_name=login_name).exists() \
            else CurrentAccount.objects.create(login_name=login_name)

        current_deposit = CurrentDeposit.objects.create(current_account=current_account,
                                                        login_name=login_name,
                                                        amount=amount)

        return self.__invoke_pay_with_password(current_deposit)

    @transaction.atomic
    def deposit_with_password_callback(self, data):
        serializer = serializers.DepositSuccessSerializer(data=data)

        if not serializer.is_valid():
            logger.error(serializer.errors)
            raise ValueError

        order_id = serializer.validated_data.get(u'order_id')
        status = serializer.validated_data.get(u'status')
        updated_time = datetime.datetime.strptime(serializer.validated_data.get(u'updated_time'), '%Y-%m-%d %H:%M:%S')

        if not CurrentDeposit.objects.filter(pk=order_id).exists():
            logger.error('order id {} does not exist', order_id)
            raise ValueError

        deposit = CurrentDeposit.objects.get(pk=order_id)

        deposit.save(status=status, updated_time=updated_time)

        CurrentDeposit.objects.update()
        current_deposit = CurrentDeposit.objects.create(current_account=current_account,
                                                        login_name=login_name,
                                                        amount=amount)

        return self.__invoke_pay_with_password(current_deposit)

    @staticmethod
    def __invoke_pay_with_password(current_deposit):
        data = serializers.DepositSerializer(instance=current_deposit).data
        response = requests.post(url='{}/deposit-with-password/'.format(PAY_WRAPPER_HOST),
                                 json=data,
                                 timeout=5)

        if response.status_code == requests.codes.ok:
            return response.json()

        logger.error('response code {} is not ok, request data is {}'.format(response.status_code, data))

        raise requests.exceptions.HTTPError
