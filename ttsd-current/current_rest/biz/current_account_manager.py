# -*- coding: utf-8 -*-

import logging

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
        self.__add_current_bill(account=account,
                                amount=amount,
                                bill_type=constants.BILL_TYPE_DEPOSIT,
                                order_id=order_id)

    @transaction.atomic
    def update_current_account_for_withdraw(self, login_name, amount, order_id):
        account = CurrentAccount.objects.select_for_update().get(login_name=login_name)

        account.balance -= amount
        account.save()
        self.__add_current_bill(account=account,
                                amount=amount,
                                bill_type=constants.BILL_TYPE_WITHDRAW,
                                order_id=order_id)

    @transaction.atomic
    def update_current_account_for_interest(self, login_name, amount, order_id):
        account = CurrentAccount.objects.select_for_update().get(login_name=login_name)

        account.balance += amount
        account.save()
        self.__add_current_bill(account=account,
                                amount=amount,
                                bill_type=constants.BILL_TYPE_INTEREST,
                                order_id=order_id)

    @staticmethod
    def __add_current_bill(account, amount, bill_type, order_id):
        CurrentBill.objects.create(current_account=account,
                                   login_name=account.login_name,
                                   bill_date=account.updated_time,
                                   bill_type=bill_type,
                                   amount=amount,
                                   balance=account.balance,
                                   order_id=order_id)
