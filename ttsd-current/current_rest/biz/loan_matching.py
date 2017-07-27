# -*- coding: utf-8 -*-
from datetime import datetime

from current_rest import constants
from current_rest.models import Loan

loan_weight_map = [{'id': 111, 'amount': 100, 'left_amount': 11, 'weight': 20},
                   {'id': 222, 'amount': 200, 'left_amount': 0, 'weight': 20},
                   {'id': 333, 'amount': 300, 'left_amount': 11, 'weight': 20},
                   {'id': 444, 'amount': 400, 'left_amount': 0, 'weight': 20},
                   {'id': 555, 'amount': 500, 'left_amount': 0, 'weight': 20}]


class LoanMatching(object):
    def __init__(self, account_id, balance):
        self.account_id = account_id
        self.balance = balance

    def _calculate_loan_weight(self):

        sum_amount = self._calculate_sum_amount()
        sum_weight = 0

        for i, loan_weight in enumerate(loan_weight_map):
            if loan_weight['left_amount'] > 0:
                # todo:保留两位有效数字
                loan_weight['weight'] = loan_weight['amount'] / sum_amount
                sum_weight += loan_weight['weight']
            else:
                loan_weight['weight'] = 1 - sum_weight

    def _calculate_sum_amount(self):
        return sum(loan['amount'] for loan in loan_weight_map if loan['left_amount'] > 0)

    def _init_loan_weight_map(self):
        if len(loan_weight_map) == 0:
            valid_loan = Loan.objects.filter(effective_date__lte=datetime.today(),
                                             expiration_date__gte=datetime.today(),
                                             headline__exact=constants.LOAN_STATUS_APPROVED)
            for loan in valid_loan:
                loan_weight_map.append({
                    'id': loan.id,
                    'amount': loan.amout,
                    'left_amount': loan.amout,
                    'weight': 0,
                })
        else:
            self._clear_full_amount_loan()

    def _clear_full_amount_loan(self):
        global loan_weight_map
        loan_weight_map = [loan_weight for loan_weight in loan_weight_map if loan_weight['left_amount'] > 0]
