# -*- coding: utf-8 -*-
import datetime
import json
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
    def redeem(self, validated_data):
        current_account = self.current_account_manager.fetch_account(login_name=validated_data.get('login_name'))

        CurrentWithdraw.objects.create(current_account=current_account,
                                       amount=validated_data.get('amount'),
                                       source=validated_data.get('source'))
        return validated_data.get('amount')

    def limits(self, request, login_name):
        current_account = self.current_account_manager.fetch_account(login_name=login_name)
        redeemed = 0
        withdraws = CurrentWithdraw.objects.filter(
            created_time__startswith=datetime.date(int(datetime.datetime.today().strftime('%Y')),
                                                   int(datetime.datetime.today().strftime('%m')),
                                                   int(datetime.datetime.today().strftime('%d'))),
            current_account=current_account)

        for withdraw in withdraws:
            redeemed += withdraw.amount

        data = {"redeemed": constants.EVERY_DAY_OF_MAX_REDEEM_AMOUNT - redeemed,
                "limits": constants.EVERY_DAY_OF_MAX_REDEEM_AMOUNT}
        return data

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
