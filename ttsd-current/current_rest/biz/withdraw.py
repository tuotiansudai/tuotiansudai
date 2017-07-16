# -*- coding: utf-8 -*-
import datetime
import logging

import requests
from django.db import transaction

from current_rest import serializers, constants
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.models import CurrentDeposit, CurrentWithdraw
from settings import PAY_WRAPPER_HOST

logger = logging.getLogger(__name__)


class Withdraw(object):
    def __init__(self):
        self.current_account_manager = CurrentAccountManager()

    @transaction.atomic
    def withdraw_create(self, data):
        serializer = serializers.CurrentWithdrawSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return serializer.data
        else:
            return serializer.errors

    @staticmethod
    def withdraw_by_id(pk):
        try:
            withdraw = CurrentWithdraw.objects.get(pk=pk)
        except CurrentWithdraw.DoesNotExist:
            raise ValueError

        serializer = serializers.CurrentWithdrawSerializer(withdraw)
        return serializer.data

    @staticmethod
    def withdraw_all_list():
        withdraws = CurrentWithdraw.objects.all()
        serializer = serializers.CurrentWithdrawSerializer(withdraws, many=True)
        return serializer.data
