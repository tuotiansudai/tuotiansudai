# -*- coding: utf-8 -*-
from datetime import datetime, timedelta
import json

import mock
from django.test import TestCase
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIClient

from current_rest import constants
from current_rest import redis_client
from current_rest.models import CurrentAccount, Loan, Agent


class DepositTestCase(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.login_name = 'fakeuser'
        self.yesterday = (datetime.now().date() + timedelta(days=-1)).strftime('%Y-%m-%d')
        Agent.objects.create(id=9999999, login_name='login_name',
                             mobile='mobile',
                             active=True)

    def tearDown(self):
        redis_client.delete("interest:{0}".format(self.yesterday))

    @mock.patch('current_rest.serializers.sum_success_deposit_by_date')
    @mock.patch('current_rest.serializers.get_current_daily_amount')
    def test_should_return_200_when_today_is_no_deposit(self, get_current_daily_amount, sum_success_deposit_by_date):
        CurrentAccount.objects.create(login_name=self.login_name)

        current_daily_amount = 1
        get_current_daily_amount.return_value = current_daily_amount
        sum_success_deposit_by_date.return_value = 0

        response = self.client.get(path=reverse('get_account', kwargs={'login_name': self.login_name}))

        self.assertEqual(response.data.get('personal_max_deposit'), current_daily_amount)

    @mock.patch('current_rest.serializers.sum_success_deposit_by_date')
    @mock.patch('current_rest.serializers.get_current_daily_amount')
    def test_should_return_200_when_today_is_no_deposit_and_user_has_deposited_1(self,
                                                                                 get_current_daily_amount,
                                                                                 sum_success_deposit_by_date):
        CurrentAccount.objects.create(login_name=self.login_name, balance=1)
        current_daily_amount = constants.PERSONAL_MAX_DEPOSIT + 1

        get_current_daily_amount.return_value = current_daily_amount
        sum_success_deposit_by_date.return_value = 1

        response = self.client.get(path=reverse('get_account', kwargs={'login_name': self.login_name}))

        self.assertEqual(response.data.get('personal_max_deposit'), constants.PERSONAL_MAX_DEPOSIT - 1)

    @mock.patch('current_rest.serializers.sum_success_deposit_by_date')
    @mock.patch('current_rest.serializers.get_current_daily_amount')
    def test_should_return_200_when_today_current_limit_is_100_and_user_had_deposited_1_before_today(self,
                                                                                                     get_current_daily_amount,
                                                                                                     sum_success_deposit_by_date):
        CurrentAccount.objects.create(login_name=self.login_name, balance=1)
        current_daily_amount = 100

        get_current_daily_amount.return_value = current_daily_amount
        sum_success_deposit_by_date.return_value = 0

        response = self.client.get(path=reverse('get_account', kwargs={'login_name': self.login_name}))

        self.assertEqual(response.data.get('personal_max_deposit'), current_daily_amount)

    @mock.patch('requests.post')
    def test_should_return_200_when_not_calculate_interest(self, fake_requests):
        pay_response = 'pay response'
        fake_requests.return_value.status_code = 200
        fake_requests.return_value.json = mock.Mock(return_value=pay_response)
        data = {
            "serial_number": 1234,
            "amount": 60000.0,
            "loan_type": "HOUSE",
            "debtor": "debtor111",
            "debtor_identity_card": "444210221986010566",
            "effective_date": (datetime.now() + timedelta(days=-1)).strftime('%Y-%m-%d %H:%M:%S'),
            "expiration_date": (datetime.now() + timedelta(days=1)).strftime('%Y-%m-%d %H:%M:%S'),
            "status": "APPROVED",
            "agent": 9999999
        }
        self.client.post(reverse("post_loan"), json.dumps(data), content_type="application/json")
        CurrentAccount.objects.create(login_name=self.login_name, balance=400000)
        response = self.client.post(path=reverse('calculate_interest_yesterday'),
                                    data={'yesterday': self.yesterday},
                                    format='json')

        self.assertEqual(response.status_code, status.HTTP_200_OK)

    @mock.patch('requests.post')
    def test_should_return_200_when_already_calculate_interest(self, fake_requests):
        pay_response = 'pay response'
        fake_requests.return_value.status_code = 200
        fake_requests.return_value.json = mock.Mock(return_value=pay_response)
        data = {
            "serial_number": 1234,
            "amount": 60000.0,
            "loan_type": "HOUSE",
            "debtor": "debtor111",
            "debtor_identity_card": "444210221986010566",
            "effective_date": (datetime.now() + timedelta(days=-1)).strftime('%Y-%m-%d %H:%M:%S'),
            "expiration_date": (datetime.now() + timedelta(days=1)).strftime('%Y-%m-%d %H:%M:%S'),
            "status": "APPROVED",
            "agent": 9999999
        }
        self.client.post(reverse("post_loan"), json.dumps(data), content_type="application/json")
        CurrentAccount.objects.create(login_name=self.login_name, balance=400000)

        redis_client.setex("interest:{0}".format(self.yesterday), self.yesterday, 60)
        response = self.client.post(path=reverse('calculate_interest_yesterday'),
                                    data={'yesterday': self.yesterday},
                                    format='json')

        self.assertEqual(response.status_code, status.HTTP_429_TOO_MANY_REQUESTS)
