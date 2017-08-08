# -*- coding: utf-8 -*-

import logging

from datetime import timedelta, datetime
from django.db.models import Sum

from current_rest import redis_client, models, constants
from current_rest.models import CurrentDailyFundInfo

logger = logging.getLogger(__name__)


class CurrentDailyManager(object):
    daily_amount_limit_key = 'current:daily:amount'

    def get_current_daily_amount(self):
        daily_amount = int(redis_client.get(self.daily_amount_limit_key) or 0)
        return daily_amount if daily_amount else 0


def sum_success_deposit_by_date(date_for_sum):
    tomorrow = date_for_sum + timedelta(days=1)
    amount_sum = models.CurrentDeposit.objects.filter(status=constants.DEPOSIT_SUCCESS,
                                                      updated_time__gte=date_for_sum,
                                                      updated_time__lt=tomorrow) \
        .all().aggregate(Sum('amount')) \
        .get('amount__sum', 0)
    return amount_sum if amount_sum else 0


def sum_interest_by_date(date_for_sum):
    tomorrow = date_for_sum + timedelta(days=1)
    amount_sum = models.CurrentBill.objects.filter(bill_type=constants.BILL_TYPE_INTEREST,
                                                   bill_date__gte=date_for_sum,
                                                   bill_date__lt=tomorrow) \
        .all().aggregate(Sum('amount')) \
        .get('amount__sum') or 0
    return amount_sum


def calculate_loan_remain_amount_today():
    loan_amount_sum = models.Loan.objects.filter(status=constants.LOAN_STATUS_APPROVED,
                                                 effective_date__lte=datetime.now().date(),
                                                 expiration_date__gte=datetime.now().date()).aggregate(
        Sum('amount')).get('amount__sum') or 0

    account_balance_sum = models.CurrentAccount.objects.all().aggregate(
        Sum('balance')).get('balance__sum') or 0

    return account_balance_sum, max(0, loan_amount_sum - account_balance_sum)


def setting_quota_amount(loan_remain_amount, invest_amount):
    CurrentDailyFundInfo.objects.create(date=datetime.now().date(), loan_remain_amount=loan_remain_amount,
                                        invest_amount=invest_amount)
