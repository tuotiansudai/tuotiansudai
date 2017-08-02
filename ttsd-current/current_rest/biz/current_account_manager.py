# -*- coding: utf-8 -*-

import logging
from datetime import datetime, timedelta, time, date

from django.db import transaction

from current_rest import constants
from current_rest.models import CurrentAccount, CurrentBill

logger = logging.getLogger(__name__)


class CurrentAccountManager(object):
    @staticmethod
    def fetch_account(login_name):
        current_account, _ = CurrentAccount.objects.get_or_create(login_name=login_name)
        return current_account

    @transaction.atomic
    def update_current_account_for_deposit(self, login_name, amount, order_id):
        account = CurrentAccount.objects.select_for_update().get(login_name=login_name)

        account.balance += amount
        account.save()

        CurrentBill.objects.create(current_account=account,
                                   bill_type=constants.BILL_TYPE_DEPOSIT,
                                   amount=amount,
                                   order_id=order_id)

    @transaction.atomic
    def update_current_account_for_over_deposit(self, login_name, amount, order_id):
        account = CurrentAccount.objects.select_for_update().get(login_name=login_name)

        account.balance -= amount
        account.save()

        CurrentBill.objects.create(current_account=account,
                                   bill_type=constants.BILL_TYPE_PAYBACK,
                                   amount=amount,
                                   order_id=order_id)

    @transaction.atomic
    def update_current_account_for_withdraw(self, login_name, amount, order_id):
        account = CurrentAccount.objects.select_for_update().get(login_name=login_name)

        account.balance -= amount
        account.save()
        CurrentBill.objects.create(current_account=account,
                                   bill_type=constants.BILL_TYPE_WITHDRAW,
                                   amount=amount,
                                   order_id=order_id)

    @transaction.atomic
    def update_current_account_for_interest(self, login_name, amount, order_id):
        account = CurrentAccount.objects.select_for_update().get(login_name=login_name)

        account.balance += amount
        account.save()
        CurrentBill.objects.create(current_account=account,
                                   bill_type=constants.BILL_TYPE_INTEREST,
                                   bill_date=datetime.combine(date.today() - timedelta(days=1), time.max),
                                   amount=amount,
                                   order_id=order_id)
