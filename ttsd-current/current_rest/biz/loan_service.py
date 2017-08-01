# -*- coding: utf-8 -*-
from datetime import datetime, date, time, timedelta
import decimal
import logging

from django.db import transaction

from current_rest import constants
from current_rest import redis_client
from current_rest.constants import yesterday
from current_rest.models import FundAllocation, Loan

logger = logging.getLogger('current_rest.biz.services.loan_service')

unmatch_loan_cache = []
ACCOUNT_LOAN_MATCHING_REDIS_KEY = 'loan:matching:{date}:{account_id}'
LOAN_MATCHING_DATE = None


class LoanMatching(object):
    def __init__(self, account):
        self.account = account

    @transaction.atomic
    def split_balance(self):
        redis_client.setex(ACCOUNT_LOAN_MATCHING_REDIS_KEY.format(
            date=yesterday,
            account_id=self.account.id),
            'matching', 60 * 60 * 24 * 10)
        # 初始化债权缓存
        self._init_loan_cache()
        sum_balance = 0
        fund_allocation = []
        # 匹配债权
        for index, unmatch_loan in enumerate(unmatch_loan_cache):
            # 每次拆分债权金额
            each_loan_amount = self.account.balance - sum_balance if index == len(unmatch_loan_cache) - 1 else int(
                self.account.balance * unmatch_loan['weight'])
            sum_balance += each_loan_amount
            unmatch_loan['left_amount'] -= each_loan_amount
            fund_allocation.append({"loan": unmatch_loan['loan'],
                                    "account_id": self.account.id,
                                    "amount": each_loan_amount
                                    })

        # 债权入库
        for index, fund in enumerate(fund_allocation):
            logger.info(
                "[loan matching:{}] 用戶id:{},balance:{},匹配债权loan_id:{},金额:{}".format(
                    yesterday, fund['account_id'],
                    self.account.balance,
                    fund['loan'].id,
                    fund['amount']))
            FundAllocation.objects.create(account=self.account,
                                          loan=fund['loan'],
                                          amount=fund['amount'])

        redis_client.setex(ACCOUNT_LOAN_MATCHING_REDIS_KEY.format(
            date=yesterday,
            account_id=self.account.id),
            'success', 60 * 60 * 24 * 3)

    def _calculate_sum_amount(self):
        return sum(
            unmatch_loan['loan'].amount for unmatch_loan in unmatch_loan_cache if unmatch_loan['left_amount'] > 0)

    def _init_loan_cache(self):
        global unmatch_loan_cache
        global LOAN_MATCHING_DATE
        if LOAN_MATCHING_DATE != yesterday:
            unmatch_loan_cache = []
        LOAN_MATCHING_DATE = yesterday

        if len(unmatch_loan_cache) == 0:
            un_match_loans = valid_loan()
            for un_match_loan in un_match_loans:
                unmatch_loan_cache.append({
                    'loan': un_match_loan,
                    'left_amount': un_match_loan.amount,
                    'weight': 0,
                })
        else:
            unmatch_loan_cache = [un_match for un_match in unmatch_loan_cache if un_match['left_amount'] > 0]
        # 债权排序
        unmatch_loan_cache.sort(key=lambda l: l['loan'].amount)
        # 计算债权权重
        self._calculate_loan_weight()

    def _calculate_loan_weight(self):
        global unmatch_loan_cache

        sum_amount = self._calculate_sum_amount()
        sum_weight = 0

        for i, unmatch_loan in enumerate(unmatch_loan_cache):
            self._set_decimal_conf(2)
            unmatch_loan['weight'] = 1 - sum_weight if i == len(unmatch_loan_cache) - 1 else (decimal.Decimal(
                unmatch_loan[
                    'loan'].amount) / decimal.Decimal(sum_amount)).__float__()
            sum_weight += unmatch_loan['weight']

    def _set_decimal_conf(self, prec):
        context = decimal.getcontext()
        context.prec = prec
        context.rounding = decimal.ROUND_DOWN


def delete_history_data():
    query_set = FundAllocation.objects.filter(
        created_time__lt=datetime.combine(date.today(), time.min))
    delete_count = query_set.count()
    query_set.delete()
    return delete_count


def valid_loan():
    return Loan.objects.filter(effective_date__lte=datetime.combine((datetime.today() - timedelta(days=1)), time.min),
                               expiration_date__gte=datetime.combine((datetime.today() - timedelta(days=1)), time.min),
                               status__exact=constants.LOAN_STATUS_APPROVED)
