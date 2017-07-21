# -*- coding: utf-8 -*-
from datetime import datetime

from django.db import transaction
from django.db.models import Sum
from rest_framework import mixins
from rest_framework import status
from rest_framework import viewsets
from rest_framework.response import Response

from current_rest import constants
from current_rest import models
from current_rest import serializers
from current_rest.biz.current_account_manager import CurrentAccountManager


class RedeemViewSet(mixins.RetrieveModelMixin,
                    mixins.CreateModelMixin,
                    mixins.UpdateModelMixin,
                    viewsets.GenericViewSet):
    serializer_class = serializers.CurrentWithdrawSerializer
    queryset = models.CurrentWithdraw.objects.all()

    def __init__(self):
        super(RedeemViewSet, self).__init__()
        self.current_account_manager = CurrentAccountManager()

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        response = super(RedeemViewSet, self).create(request, *args, **kwargs)
        data = {"amount": response.data['amount']}
        return Response({"data": data}, status=status.HTTP_201_CREATED)

    def limits(self, request, login_name):
        current_account = self.current_account_manager.fetch_account(login_name=login_name)
        account_balance = current_account.balance

        today = datetime.now().date()
        amount_sum = models.CurrentWithdraw.objects.filter(created_time__startswith=today,
                                                           current_account=current_account).exclude(
            status=constants.STATUS_DENIED).aggregate(
            Sum('amount')).get('amount__sum', 0)

        current_remain_mount = constants.EVERY_DAY_OF_MAX_REDEEM_AMOUNT - amount_sum
        remain_mount = current_remain_mount if current_remain_mount <= account_balance else account_balance

        data = {"remainAmount": remain_mount,
                "totalAmount": constants.EVERY_DAY_OF_MAX_REDEEM_AMOUNT}

        return Response({"data": data}, status=status.HTTP_200_OK)
