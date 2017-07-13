# -*- coding: utf-8 -*-
import mock
from django.test import TestCase
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIClient

from current_rest import constants
from current_rest.models import CurrentAccount, CurrentDeposit


class DepositTestCase(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.login_name = 'fakeuser'

    @mock.patch('requests.post')
    def test_should_return_200_when_deposit_success(self, fake_requests):
        pay_response = 'pay response'
        fake_requests.return_value.status_code = 200
        fake_requests.return_value.json = mock.Mock(return_value=pay_response)
        response = self.client.post(path=reverse('deposit_with_password', kwargs={'login_name': self.login_name}),
                                    data={'amount': 1}, format='json')

        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data, pay_response)

        self.assertTrue(CurrentAccount.objects.filter(login_name=self.login_name).exists())
        deposit = CurrentDeposit.objects.get(login_name=self.login_name)
        self.assertEqual(deposit.amount, 1)
        self.assertEqual(deposit.status, constants.DEPOSIT_WAITING_PAY)

    def test_should_return_400_when_amount_is_not_int(self):
        response = self.client.post(path=reverse('deposit_with_password', kwargs={'login_name': self.login_name}),
                                    data={'amount': 'amount'}, format='json')

        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
