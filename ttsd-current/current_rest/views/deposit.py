# -*- coding: utf-8 -*-
import logging

import requests
from django.db import transaction
from rest_framework import status, viewsets, mixins
from rest_framework.decorators import api_view
from rest_framework.response import Response

from current_rest import serializers, constants
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.biz.deposit import Deposit
from current_rest.exceptions import PayWrapperException
from current_rest.models import CurrentDeposit
from settings import PAY_WRAPPER_HOST

logger = logging.getLogger(__name__)


@api_view(['GET'])
def personal_max_deposit(request, login_name):
    try:
        amount = Deposit().calculate_max_deposit(login_name=login_name)
        return Response({'amount': amount}, status=status.HTTP_200_OK)
    except ValueError:
        return Response(status=status.HTTP_400_BAD_REQUEST)


class DepositViewSet(mixins.RetrieveModelMixin,
                     mixins.CreateModelMixin,
                     mixins.UpdateModelMixin,
                     viewsets.GenericViewSet):
    serializer_class = serializers.DepositSerializer
    queryset = CurrentDeposit.objects.all()

    pay_with_password_url = '{}/deposit-with-password/'.format(PAY_WRAPPER_HOST)
    pay_with_no_password_url = '{}/deposit-with-no-password/'.format(PAY_WRAPPER_HOST)

    def __init__(self):
        super(DepositViewSet, self).__init__()
        self.current_account_manager = CurrentAccountManager()

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        self.perform_create(serializer)
        headers = self.get_success_headers(serializer.data)
        return Response(self.__invoke_pay(serializer), status=status.HTTP_201_CREATED, headers=headers)

    @transaction.atomic
    def update(self, request, *args, **kwargs):
        partial = kwargs.pop('partial', False)
        instance = self.get_object()
        if instance.status != constants.DEPOSIT_WAITING_PAY:
            logger.error('order id {} had already updated', instance.pk)
            return

        serializer = self.get_serializer(instance, data=request.data, partial=partial)
        serializer.is_valid(raise_exception=True)
        self.perform_update(serializer)
        self.current_account_manager.update_current_account_for_deposit(login_name=instance.login_name,
                                                                        amount=instance.amount,
                                                                        order_id=instance.pk)

        if getattr(instance, '_prefetched_objects_cache', None):
            # If 'prefetch_related' has been applied to a queryset, we need to
            # forcibly invalidate the prefetch cache on the instance.
            instance._prefetched_objects_cache = {}

        return Response(serializer.data)

    def __invoke_pay(self, serializer):
        no_password = serializer.validated_data.get('no_password')
        url = self.pay_with_no_password_url if no_password else self.pay_with_password_url
        try:
            response = requests.post(url=url, json=serializer.data, timeout=10)

            if response.status_code == requests.codes.ok:
                return response.json()
            logger.error('response code {} is not ok, request data is {}'.format(response.status_code, serializer.data))
        except Exception:
            raise PayWrapperException('call pay wrapper fail, check current-rest log for more information')
