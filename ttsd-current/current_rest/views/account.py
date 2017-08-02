# -*- coding: utf-8 -*-
import logging

from django.db import transaction
from django.http import Http404
from rest_framework import status
from rest_framework import viewsets, mixins
from rest_framework.response import Response

from current_rest import redis_client
from current_rest import serializers, models
from current_rest import settings
from current_rest.biz import current_interest
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.biz.pay_manager import PayManager
from current_rest.settings import PAY_WRAPPER_HOST

logger = logging.getLogger(__name__)


class AccountViewSet(mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    serializer_class = serializers.AccountSerializer
    queryset = models.CurrentAccount.objects.all()
    lookup_field = 'login_name'

    calculate_interest_key = "interest:{0}"
    interest_pay_with_no_password_url = '{}/interest-settlement/'.format(PAY_WRAPPER_HOST)
    valid_time = 60 * 60 * 24
    login_name = settings.LOGIN_NAME

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
    def calculate_interest_yesterday(self, request):
        data = {"login_name": self.login_name, "amount": self.calculate_yesterday_interest()}
        PayManager.invoke_pay(data, self.interest_pay_with_no_password_url)

        yesterday = request.data.get('yesterday')
        interest_key = self.calculate_interest_key.format(yesterday)
        if redis_client.exists(interest_key):
            logger.info("{} calculate interest done!".format(yesterday))
            return Response(status=status.HTTP_429_TOO_MANY_REQUESTS)

        for account in self.queryset:
            interest = current_interest.calculate_interest(account.balance)
            CurrentAccountManager().update_current_account_for_interest(account.login_name, interest, account.id)

        redis_client.setex(interest_key, yesterday, self.valid_time)
        return Response(status=status.HTTP_200_OK)

    def calculate_yesterday_interest(self):
        accounts = models.CurrentAccount.objects.all()
        yesterday_total_interest = 0
        for account in accounts:
            yesterday_total_interest += current_interest.calculate_interest(account.balance)
        return yesterday_total_interest
