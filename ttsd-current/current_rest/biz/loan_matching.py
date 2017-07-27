# -*- coding: utf-8 -*-
from datetime import datetime

from django.db import transaction

from current_rest import constants
from current_rest.models import Loan, FundAllocation

loan_map = [{'id': 111, 'amount': 100, 'left_amount': 11, 'weight': 20},
            {'id': 222, 'amount': 200, 'left_amount': 0, 'weight': 20},
            {'id': 333, 'amount': 300, 'left_amount': 11, 'weight': 20},
            {'id': 444, 'amount': 400, 'left_amount': 0, 'weight': 20},
            {'id': 555, 'amount': 500, 'left_amount': 0, 'weight': 20}]


class LoanMatching(object):
    def __init__(self, account):
        self.account = account

    @transaction.atomic
    def _split_balance(self):
        sum_balance = 0
        # todo:self.account.balance * loan['weight'] 保留正整数
        for loan in loan_map:
            sum_balance += self.account.balance * loan['weight']
            FundAllocation.objects.create(account=self.account,
                                          loan_id=loan['id'],
                                          amount=self.account.balance * loan['weight'])

        calibration_balance = self.account['']

    def _calculate_loan_weight(self):

        sum_amount = self._calculate_sum_amount()
        sum_weight = 0

        for i, loan in enumerate(loan_map):
            if loan['left_amount'] > 0:
                # todo:保留两位有效数字
                loan['weight'] = loan['amount'] / sum_amount
                sum_weight += loan['weight']
            else:
                loan['weight'] = 1 - sum_weight

    def _calculate_sum_amount(self):
        return sum(loan['amount'] for loan in loan_map if loan['left_amount'] > 0)

    def _init_loan_map(self):
        if len(loan_map) == 0:
            valid_loan = Loan.objects.filter(effective_date__lte=datetime.today(),
                                             expiration_date__gte=datetime.today(),
                                             headline__exact=constants.LOAN_STATUS_APPROVED)
            for loan in valid_loan:
                loan_map.append({
                    'loan': loan.model_to,
                    'amount': loan.amout,
                    'left_amount': loan.amout,
                    'weight': 0,
                })
        else:
            self._clear_full_amount_loan()

    def _clear_full_amount_loan(self):
        global loan_map
        loan_map = [loan for loan in loan_map if loan['left_amount'] > 0]
