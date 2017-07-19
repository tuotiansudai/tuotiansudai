# -*- coding: utf-8 -*-
import logging
from datetime import datetime, timedelta

from django.db.models import Sum

from current_rest import constants
from current_rest.biz import PERSONAL_MAX_DEPOSIT
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.biz.current_daily_manager import CurrentDailyManager
from current_rest.models import CurrentAccount
from current_rest.models import CurrentDeposit
from settings import PAY_WRAPPER_HOST

logger = logging.getLogger(__name__)


class Deposit(object):
    pay_with_password_url = '{}/deposit-with-password/'.format(PAY_WRAPPER_HOST)
    pay_with_no_password_url = '{}/deposit-with-no-password/'.format(PAY_WRAPPER_HOST)

    def __init__(self):
        self.current_account_manager = CurrentAccountManager()
        self.current_daily_manager = CurrentDailyManager()

    def calculate_max_deposit(self, login_name):
        current_account_filter = CurrentAccount.objects.filter(login_name=login_name)
        balance = current_account_filter.first().balance if current_account_filter.exists() else 0
        user_max_deposit = PERSONAL_MAX_DEPOSIT - balance if PERSONAL_MAX_DEPOSIT - balance > 0 else 0
        today_sum_deposit = self.__calculate_success_deposit_today()
        current_daily_amount = self.current_daily_manager.get_current_daily_amount()
        return min(user_max_deposit, current_daily_amount - today_sum_deposit)

    @staticmethod
    def __calculate_success_deposit_today():
        today = datetime.now().date()
        tomorrow = today + timedelta(1)
        amount_sum = CurrentDeposit.objects.filter(status=constants.DEPOSIT_SUCCESS,
                                                   updated_time__range=(today, tomorrow)) \
            .all().aggregate(Sum('amount')) \
            .get('amount__sum', 0)
        return amount_sum if amount_sum else 0
