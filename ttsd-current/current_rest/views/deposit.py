# -*- coding: utf-8 -*-
import logging
from datetime import datetime

from django.db import transaction
from rest_framework import status, viewsets, mixins
from rest_framework.renderers import JSONRenderer
from rest_framework.response import Response

import jobs
from common import constants as common_constants
from current_rest import serializers, constants, models, filters
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.biz.current_daily_manager import sum_success_deposit_by_date, get_current_daily_amount
from current_rest.biz.pay_manager import invoke_pay
from current_rest.settings import PAY_WRAPPER_SERVER
from jobs.client import MessageClient

logger = logging.getLogger(__name__)


class DepositViewSet(mixins.RetrieveModelMixin,
                     mixins.ListModelMixin,
                     mixins.CreateModelMixin,
                     mixins.UpdateModelMixin,
                     viewsets.GenericViewSet):
    serializer_class = serializers.DepositSerializer
    queryset = models.CurrentDeposit.objects.all()
    filter_class = filters.DepositFilter
    ordering = ('-updated_time', '-pk')

    pay_with_password_url = '{}/deposit-with-password/'.format(PAY_WRAPPER_SERVER)
    pay_with_no_password_url = '{}/deposit-with-no-password/'.format(PAY_WRAPPER_SERVER)

    def __init__(self):
        super(DepositViewSet, self).__init__()
        self.current_account_manager = CurrentAccountManager()
        self.mq_client = MessageClient(jobs.settings.QueueName.OVER_DEPOSIT_TASK_QUEUE)

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        response = super(DepositViewSet, self).create(request, *args, **kwargs)
        no_password = response.data.get('no_password')
        url = self.pay_with_no_password_url if no_password else self.pay_with_password_url
        return Response(invoke_pay(response.data, url), status=status.HTTP_201_CREATED)

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
            updated_deposit.status = common_constants.DepositStatusType.DEPOSIT_OVER_PAY
            updated_deposit.save()
            self.mq_client.send(JSONRenderer().render(self.serializer_class(instance=updated_deposit).data))

    def deposit_update_payback_success(self, updated_deposit):
        self.current_account_manager.update_current_account_for_over_deposit(login_name=updated_deposit.login_name,
                                                                             amount=updated_deposit.amount,
                                                                             order_id=updated_deposit.pk)

    def __is_over_deposit(self, deposit):
        account = self.current_account_manager.fetch_account(login_name=deposit.login_name)
        total_deposit_today = sum_success_deposit_by_date(datetime.now().date())
        current_daily_amount = get_current_daily_amount()
        return total_deposit_today > current_daily_amount or account.balance > constants.PERSONAL_MAX_DEPOSIT
