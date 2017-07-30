# -*- coding: utf-8 -*-
import datetime
import logging

import math
from django.db import transaction

from current_rest import constants
from current_rest import redis_client
from current_rest.models import FundAllocation, Loan

logger = logging.getLogger(__name__)

unmatch_loan_cache = []
ACCOUNT_LOAN_MATCHING_REDIS_KEY = 'loan:matching:{date}:{account_id}'
LOAN_MATCHING_DATE = None


class LoanMatching(object):
    def __init__(self, account):
        self.account = account

    @transaction.atomic
    def split_balance(self):
        redis_client.psetex(ACCOUNT_LOAN_MATCHING_REDIS_KEY.format(
            date=(datetime.datetime.today() - datetime.timedelta(days=1)).strftime("%Y-%m-%d"),
            account_id=self.account.id),
            60 * 60 * 24 * 3, 'matching')
        # 初始化债权缓存
        self._init_loan_cache()
        sum_balance = 0
        fund_allocation = []
        # 匹配债权
        for unmatch_loan in unmatch_loan_cache:
            # todo:self.account.balance * loan['weight'] 保留正整数
            sum_balance += self.account.balance * unmatch_loan['weight']
            unmatch_loan['left_amount'] -= self.account.balance * unmatch_loan['weight']
            fund_allocation.append({"loan": unmatch_loan['loan'],
                                    "account_id": self.account.id,
                                    "amount": self.account.balance * unmatch_loan['weight']
                                    })

        calibration_balance = self.account.balance - sum_balance
        # 债权入库
        for index, fund in enumerate(fund_allocation):
            # 校准金额计入最大债权
            if index == 0:
                fund['amount'] += calibration_balance
                logger.info(
                    "[loan matching:] loan_id:{}校验金额:{} 匹入 account_id:{}账户".format(fund['loan'].id, fund['amount'],
                                                                                   fund['account_id']))
            # todo: 写入日志文件
            logger.info(
                "[loan matching:] 用戶id:{}匹配债权loan_id{}金额{}".format(fund['account_id'], fund['loan'].id,
                                                                   fund['amount']))
            FundAllocation.objects.create(account=self.account,
                                          loan=fund['loan'],
                                          amount=fund['amount'])

        redis_client.psetex(ACCOUNT_LOAN_MATCHING_REDIS_KEY.format(
            date=(datetime.datetime.today() - datetime.timedelta(days=1)).strftime("%Y-%m-%d"),
            account_id=self.account.id),
            60 * 60 * 24 * 3, 'success')

    def _calculate_sum_amount(self):
        return sum(
            unmatch_loan['loan'].amount for unmatch_loan in unmatch_loan_cache if unmatch_loan['left_amount'] > 0)

    def _init_loan_cache(self):
        global unmatch_loan_cache
        global LOAN_MATCHING_DATE
        if LOAN_MATCHING_DATE != (datetime.datetime.today() - datetime.timedelta(days=1)).strftime("%Y-%m-%d"):
            unmatch_loan_cache = []
        else:
            LOAN_MATCHING_DATE = (datetime.datetime.today() - datetime.timedelta(days=1)).strftime("%Y-%m-%d")

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
        # 计算债权权重
        self._calculate_loan_weight()
        # 债权排序
        unmatch_loan_cache.sort(key=lambda l: l['loan'].amount, reverse=True)

    def _calculate_loan_weight(self):
        global unmatch_loan_cache

        sum_amount = self._calculate_sum_amount()
        sum_weight = 0

        for i, unmatch_loan in enumerate(reversed(unmatch_loan_cache)):
            # todo:self.account.balance * loan['weight'] 保留正整数
            unmatch_loan['weight'] = 1 - sum_weight if i == len(unmatch_loan_cache) - 1 else unmatch_loan['loan'][
                                                                                                 'amount'] / sum_amount
            sum_weight += unmatch_loan['weight']


def delete_history_data():
    query_set = FundAllocation.objects.filter(
        created_time__lt=datetime.datetime.combine(datetime.date.today(), datetime.time.min))
    delete_count = query_set.count()
    query_set.delete()
    return delete_count


def valid_loan():
    return Loan.objects.filter(effective_date__lte=datetime.datetime.today(),
                               expiration_date__gte=datetime.datetime.today(),
                               status__exact=constants.LOAN_STATUS_APPROVED)
