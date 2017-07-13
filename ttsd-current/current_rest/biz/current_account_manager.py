# -*- coding: utf-8 -*-
from datetime import datetime

import logging
from django.db import transaction

from current_rest import constants
from current_rest.models import CurrentAccount, CurrentBill

logger = logging.getLogger(__name__)


class CurrentAccountManager(object):
    def __init__(self):
        self.in_or_out = {constants.BILL_TYPE_DEPOSIT: 1,
                          constants.BILL_TYPE_WITHDRAW: -1,
                          constants.BILL_TYPE_INTEREST: 1}

    @transaction.atomic
    def fetch_account(self, login_name):
        current_account, _ = CurrentAccount.objects.get_or_create(login_name=login_name)
        return current_account

    @transaction.atomic
    def update_current_account(self, login_name, amount, bill_type, order_id, updated_time):
        account = CurrentAccount.objects.select_for_update().get(login_name=login_name)

        if bill_type not in self.in_or_out.keys():
            logger.error('bill type {} does not exist'.format(bill_type))
            raise ValueError

        account.balance += self.in_or_out[bill_type] * amount
        account.updated_time = updated_time
        account.save()
        CurrentBill.objects.create(current_account=account,
                                   login_name=login_name,
                                   bill_date=updated_time,
                                   bill_type=bill_type,
                                   amount=amount,
                                   balance=account.balance,
                                   order_id=order_id)
