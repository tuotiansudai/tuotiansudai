# -*- coding: utf-8 -*-
from datetime import datetime
from math import floor

from django.db.models import Sum

from current_rest import constants
from current_rest.models import Loan

loan_weight_map = []


class LoanMatching(object):
    def __init__(self, account_id, balance):
        self.account_id = account_id
        self.balance = balance

    @staticmethod
    def __calculate_loan_weight():
        if len(loan_weight_map) == 0:
            valid_loan = Loan.objects.filter(effective_date__lte=datetime.today(),
                                             expiration_date__gte=datetime.today(),
                                             headline__exact=constants.LOAN_STATUS_APPROVED)
            sum_amount = valid_loan.aggregate(Sum('amount'))
            sum_weight = 0
            for loan in valid_loan:
                # todo:保留两位有效数字
                weight = loan.amount / sum_amount
                sum_weight += weight
                loan_weight_map.append({
                    'id': loan.id,
                    'amount': loan.amout,
                    'left_amount': loan.amout,
                    'weight': weight,
                })
        else:
            pass
