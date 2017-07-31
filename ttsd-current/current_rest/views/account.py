# -*- coding: utf-8 -*-
import logging

from datetime import datetime, timedelta

import requests
from django.db import transaction
from django.http import Http404
from rest_framework import viewsets, mixins
from rest_framework.response import Response

from current_rest import constants, redis_client
from current_rest import serializers, models
from current_rest.biz import current_interest
from current_rest.exceptions import PayWrapperException
from current_rest.settings import PAY_WRAPPER_HOST

logger = logging.getLogger(__name__)


class AccountViewSet(mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    serializer_class = serializers.AccountSerializer
    queryset = models.CurrentAccount.objects.all()
    lookup_field = 'login_name'

    calculate_interest_key = "interest:{0}"
    pay_with_no_password_url = '{}/interest-settlement/'.format(PAY_WRAPPER_HOST)
    valid_time = 60 * 60 * 24

    def retrieve(self, request, *args, **kwargs):
        instance = models.CurrentAccount(login_name=kwargs.get(self.lookup_field),
                                         balance=0)
        try:
            instance = self.get_object()
        except Http404:
            pass
        serializer = self.get_serializer(instance)

        return Response(serializer.data)

    @transaction.atomic
    def update_balance(self, request):
        self.__invoke_pay({"login_name": "", "amount": self.calculate_yesterday_interest()})
        yesterday = request.data.get('yesterday')
        interest_key = self.calculate_interest_key.format(yesterday)
        if redis_client.exists(interest_key):
            data = {"code": "0001", "message": "昨天利息已经计算完成，不能重复就按"}
            return Response(data)

        accounts = models.CurrentAccount.objects.all()
        for account in accounts:
            interest = int(current_interest.calculate_interest(account.balance))
            account.balance += interest
            account.save()

            models.CurrentBill.objects.create(current_account=account, login_name=account.login_name,
                                              bill_date=datetime.now(), bill_type=constants.BILL_TYPE_INTEREST,
                                              amount=interest, balance=account.balance,
                                              order_id=account.id)
        redis_client.setex(interest_key, yesterday, self.valid_time)
        data = {"code": "0000", "message": ""}
        return Response(data)

    def calculate_yesterday_interest(self):
        accounts = models.CurrentAccount.objects.all()
        yesterday_total_interest = 0
        for account in accounts:
            yesterday_total_interest += int(current_interest.calculate_interest(account.balance))
        return yesterday_total_interest

    def __invoke_pay(self, data):
        url = self.pay_with_no_password_url
        try:
            response = requests.post(url=url, json=data, timeout=10)

            if response.status_code == requests.codes.ok:
                return response.json()
            logger.error('response code {} is not ok, request data is {}'.format(response.status_code, data))
            raise PayWrapperException('call pay wrapper fail, check current-rest log for more information')
        except Exception, e:
            raise PayWrapperException(
                'call pay wrapper fail, check current-rest log for more information, {}'.format(e))
