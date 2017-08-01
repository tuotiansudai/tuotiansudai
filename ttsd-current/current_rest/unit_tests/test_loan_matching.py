# -*- coding: utf-8 -*-
from datetime import datetime, timedelta
from unittest import TestCase

from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIClient

from current_rest import constants, redis_client
from current_rest.biz.loan_service import ACCOUNT_LOAN_MATCHING_REDIS_KEY
from current_rest.models import Agent, Loan, CurrentAccount, FundAllocation
from current_rest.views.loan_matching import LOAN_MATCHING_REDIS_KEY

yesterday = (datetime.today() - timedelta(days=1)).strftime("%Y-%m-%d")


class LoanMatchingTests(TestCase):
    client = APIClient()

    @classmethod
    def setUpClass(cls):
        Agent.objects.create(id=9999999, login_name='login_name',
                             mobile='mobile',
                             active=True)

        Loan.objects.create(id=9999997,
                            created_time=yesterday,
                            updated_time=yesterday,
                            serial_number=111,
                            amount=11000,
                            loan_type=constants.LOAN_TYPE_HOUSE,
                            debtor='debtor1',
                            debtor_identity_card='123456789012345678',
                            effective_date=yesterday,
                            expiration_date=yesterday,
                            creator='creator',
                            status=constants.LOAN_STATUS_APPROVED,
                            agent_id=9999999
                            )

        Loan.objects.create(id=9999998,
                            created_time=yesterday,
                            updated_time=yesterday,
                            serial_number=222,
                            amount=10000,
                            loan_type=constants.LOAN_TYPE_HOUSE,
                            debtor='debtor1',
                            debtor_identity_card='123456789012345678',
                            effective_date=yesterday,
                            expiration_date=yesterday,
                            creator='creator',
                            status=constants.LOAN_STATUS_APPROVED,
                            agent_id=9999999
                            )

        Loan.objects.create(id=9999999,
                            created_time=yesterday,
                            updated_time=yesterday,
                            serial_number=333,
                            amount=12000,
                            loan_type=constants.LOAN_TYPE_HOUSE,
                            debtor='debtor1',
                            debtor_identity_card='123456789012345678',
                            effective_date=yesterday,
                            expiration_date=yesterday,
                            creator='creator',
                            status=constants.LOAN_STATUS_APPROVED,
                            agent_id=9999999
                            )

        CurrentAccount.objects.create(login_name='login_name',
                                      balance=30000,
                                      created_time=datetime.today())

    @classmethod
    def tearDownClass(cls):
        redis_client.delete("interest:{}".format(yesterday))
        redis_client.delete(LOAN_MATCHING_REDIS_KEY.format(date=yesterday))
        redis_client.delete(ACCOUNT_LOAN_MATCHING_REDIS_KEY.format(
            date=yesterday,
            account_id=CurrentAccount.objects.first().id))

    def test_single_account_loan_matching_success(self):
        redis_client.setex('interest:{}'.format(yesterday),
                           'success', 100)
        response = self.client.get(reverse("loan_matching"))
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        fund_allocations = FundAllocation.objects.all()
        for fund in fund_allocations:
            if fund.loan_id == 9999997:
                self.assertEqual(fund.amount, 9900)
            if fund.loan_id == 9999998:
                self.assertEqual(fund.amount, 9000)
            if fund.loan_id == 9999999:
                self.assertEqual(fund.amount, 11100)

    def test_should_return_200_when_calculate_interest_unfinished(self):
        response = self.client.get(reverse("loan_matching"))
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data['message'], 'calculate interest unfinished')
