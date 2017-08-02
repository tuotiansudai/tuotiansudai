# -*- coding: utf-8 -*-
import logging

from django.db import transaction
from rest_framework import status, viewsets, mixins
from rest_framework.renderers import JSONRenderer
from rest_framework.response import Response

import jobs
from current_rest import serializers, constants, models
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.biz.current_daily_manager import CurrentDailyManager, calculate_success_deposit_today
from current_rest.biz.pay_manager import PayManager
from current_rest.settings import PAY_WRAPPER_SERVER
from jobs.client import MessageClient

logger = logging.getLogger(__name__)


class DepositViewSet(mixins.RetrieveModelMixin,
                     mixins.CreateModelMixin,
                     mixins.UpdateModelMixin,
                     viewsets.GenericViewSet):
    serializer_class = serializers.DepositSerializer
    queryset = models.CurrentDeposit.objects.all()

    pay_with_password_url = '{}/deposit-with-password/'.format(PAY_WRAPPER_SERVER)
    pay_with_no_password_url = '{}/deposit-with-no-password/'.format(PAY_WRAPPER_SERVER)

    def __init__(self):
        super(DepositViewSet, self).__init__()
        self.current_account_manager = CurrentAccountManager()
        self.current_daily_manager = CurrentDailyManager()
        self.mq_client = MessageClient(jobs.settings.QueueName.OVER_DEPOSIT_TASK_QUEUE)

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        response = super(DepositViewSet, self).create(request, *args, **kwargs)
        no_password = response.data.get('no_password')
        url = self.pay_with_no_password_url if no_password else self.pay_with_password_url
        return Response(PayManager.invoke_pay(response.data, url), status=status.HTTP_201_CREATED)

    @transaction.atomic
    def perform_update(self, serializer):
        updated_deposit = serializer.save()

        update_strategy = getattr(self, 'deposit_update_{}'.format(updated_deposit.status.lower()), None)
        if update_strategy:
            update_strategy(updated_deposit)

    def deposit_update_success(self, updated_deposit):
        self.current_account_manager.update_current_account_for_deposit(login_name=updated_deposit.login_name,
                                                                        amount=updated_deposit.amount,
                                                                        order_id=updated_deposit.pk)

        if self.__is_over_deposit(updated_deposit):
            updated_deposit.status = constants.DEPOSIT_OVER_PAY
            updated_deposit.save()
            self.mq_client.send(JSONRenderer().render(self.serializer_class(instance=updated_deposit).data))

    def deposit_update_payback_success(self, updated_deposit):
        self.current_account_manager.update_current_account_for_over_deposit(login_name=updated_deposit.login_name,
                                                                             amount=updated_deposit.amount,
                                                                             order_id=updated_deposit.pk)

    def __is_over_deposit(self, deposit):
        account = self.current_account_manager.fetch_account(login_name=deposit.login_name)
        total_deposit_today = calculate_success_deposit_today()
        current_daily_amount = self.current_daily_manager.get_current_daily_amount()
        return total_deposit_today > current_daily_amount or account.balance > constants.PERSONAL_MAX_DEPOSIT
