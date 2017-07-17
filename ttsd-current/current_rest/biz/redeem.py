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


class Redeem(object):
    def __init__(self):
        self.current_account_manager = CurrentAccountManager()

    @transaction.atomic
    def redeem(self, data):
        current_account = self.current_account_manager.fetch_account(login_name=data['login_name'])
        data.update({'current_account': current_account.__dict__['id']})
        serializer = serializers.CurrentWithdrawSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return serializer.data
        else:
            return serializer.errors

    @staticmethod
    def redeem_by_id(pk):
        try:
            withdraw = CurrentWithdraw.objects.get(pk=pk)
        except CurrentWithdraw.DoesNotExist:
            raise ValueError

        serializer = serializers.CurrentWithdrawSerializer(withdraw)
        return serializer.data

    @staticmethod
    def redeem_all_list():
        withdraws = CurrentWithdraw.objects.all()
        serializer = serializers.CurrentWithdrawSerializer(withdraws, many=True)
        return serializer.data

    @transaction.atomic
    def redeem_audit(self, pk, st):
        try:
            withdraw = CurrentWithdraw.objects.get(pk=pk)
        except CurrentWithdraw.DoesNotExist:
            raise ValueError

        if st == constants.STATUS_APPROVED:
            login_name = withdraw.current_account.login_name
            amount = withdraw.amount
            order_id = withdraw.id
            CurrentAccountManager().update_current_account(login_name, amount, constants.BILL_TYPE_WITHDRAW, order_id,
                                                           datetime.datetime.now())
        # 调用mq，更新用户的的正常账号

        # 更新赎回状态
        CurrentWithdraw.objects.filter(id=pk).update(status=st, approve_time=datetime.datetime.now())

