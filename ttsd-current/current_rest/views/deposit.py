# -*- coding: utf-8 -*-
import logging

from django.db import transaction
from rest_framework import status, viewsets, mixins
from rest_framework.response import Response

from current_rest import serializers, constants, models
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.biz.pay_manager import PayManager
from current_rest.settings import PAY_WRAPPER_HOST

logger = logging.getLogger(__name__)


class DepositViewSet(mixins.RetrieveModelMixin,
                     mixins.CreateModelMixin,
                     mixins.UpdateModelMixin,
                     viewsets.GenericViewSet):
    serializer_class = serializers.DepositSerializer
    queryset = models.CurrentDeposit.objects.all()

    pay_with_password_url = '{}/deposit-with-password/'.format(PAY_WRAPPER_HOST)
    pay_with_no_password_url = '{}/deposit-with-no-password/'.format(PAY_WRAPPER_HOST)

    def __init__(self):
        super(DepositViewSet, self).__init__()
        self.current_account_manager = CurrentAccountManager()

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        response = super(DepositViewSet, self).create(request, *args, **kwargs)
        no_password = response.data.get('no_password')
        url = self.pay_with_no_password_url if no_password else self.pay_with_password_url
        return Response(PayManager.invoke_pay(response.data, url), status=status.HTTP_201_CREATED)

    @transaction.atomic
    def update(self, request, *args, **kwargs):
        instance = self.get_object()
        if instance.status != constants.DEPOSIT_WAITING_PAY:
            logger.error('order id {} had already updated', instance.pk)
            return

        response = super(DepositViewSet, self).update(request, *args, **kwargs)

        self.current_account_manager.update_current_account_for_deposit(login_name=instance.login_name,
                                                                        amount=instance.amount,
                                                                        order_id=instance.pk)
        return response
