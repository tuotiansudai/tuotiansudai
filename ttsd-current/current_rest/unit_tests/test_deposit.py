# -*- coding: utf-8 -*-
import mock
from django.test import TestCase
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIClient

from current_rest import constants, serializers
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.biz.deposit import Deposit
from current_rest.models import CurrentAccount, CurrentDeposit, CurrentBill


class DepositTestCase(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.login_name = 'fakeuser'

    @mock.patch('requests.post')
    def test_should_return_200_when_deposit_with_password(self, fake_requests):
        pay_response = 'pay response'
        fake_requests.return_value.status_code = 200
        fake_requests.return_value.json = mock.Mock(return_value=pay_response)
        response = self.client.post(path=reverse('deposit_with_password', kwargs={'login_name': self.login_name}),
                                    data={'amount': 1, 'source': 'IOS'},
                                    format='json')

        deposit = CurrentDeposit.objects.get(login_name=self.login_name)

        fake_requests.assert_called_once_with(url=Deposit.pay_with_password_url,
                                              json=serializers.DepositSerializer(instance=deposit).data,
                                              timeout=5)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data, pay_response)

        self.assertTrue(CurrentAccount.objects.filter(login_name=self.login_name).exists())
        self.assertEqual(deposit.amount, 1)
        self.assertEqual(deposit.status, constants.DEPOSIT_WAITING_PAY)
        self.assertEqual(deposit.source, constants.SOURCE_IOS)
        self.assertFalse(deposit.no_password)

    @mock.patch('requests.post')
    def test_should_return_200_when_deposit_with_no_password(self, fake_requests):
        pay_response = 'pay response'
        fake_requests.return_value.status_code = 200
        fake_requests.return_value.json = mock.Mock(return_value=pay_response)
        response = self.client.post(path=reverse('deposit_with_no_password', kwargs={'login_name': self.login_name}),
                                    data={'amount': 1, 'source': 'IOS'},
                                    format='json')

        deposit = CurrentDeposit.objects.get(login_name=self.login_name)
        fake_requests.assert_called_once_with(url=Deposit.pay_with_no_password_url,
                                              json=serializers.DepositSerializer(instance=deposit).data,
                                              timeout=5)

        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data, pay_response)

        self.assertTrue(CurrentAccount.objects.filter(login_name=self.login_name).exists())
        self.assertEqual(deposit.amount, 1)
        self.assertEqual(deposit.status, constants.DEPOSIT_WAITING_PAY)
        self.assertEqual(deposit.source, constants.SOURCE_IOS)
        self.assertTrue(deposit.no_password)

    def test_should_return_400_when_deposit_amount_is_not_int(self):
        response = self.client.post(path=reverse('deposit_with_password', kwargs={'login_name': self.login_name}),
                                    data={'amount': 'amount'},
                                    format='json')

        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)

        response = self.client.post(path=reverse('deposit_with_no_password', kwargs={'login_name': self.login_name}),
                                    data={'amount': 'amount'},
                                    format='json')

        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)

    def test_should_return_400_when_deposit_callback_data_is_illegal(self):
        response = self.client.post(path=reverse('deposit_with_password_callback'),
                                    data={'order_id': 'order_id',
                                          'status': constants.DEPOSIT_SUCCESS},
                                    format='json')

        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)

        response = self.client.post(path=reverse('deposit_with_password_callback'),
                                    data={'order_id': 0,
                                          'status': 'status'},
                                    format='json')

        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)

    def test_should_return_200_when_deposit_callback_data_is_legal(self):
        fake_account = CurrentAccountManager().fetch_account(self.login_name)
        fake_deposit = CurrentDeposit.objects.create(current_account=fake_account, login_name=self.login_name, amount=1)

        response = self.client.post(path=reverse('deposit_with_password_callback'),
                                    data={'order_id': fake_deposit.pk,
                                          'success': 'true'},
                                    format='json')

        self.assertEqual(response.status_code, status.HTTP_200_OK)

        updated_deposit = CurrentDeposit.objects.get(login_name=self.login_name)
        updated_account = CurrentAccount.objects.get(login_name=self.login_name)
        current_bill = CurrentBill.objects.get(login_name=self.login_name)

        self.assertEqual(updated_deposit.status, constants.DEPOSIT_SUCCESS)
        self.assertEqual(updated_account.balance, fake_account.balance + fake_deposit.amount)
        self.assertEqual(updated_account.updated_time, updated_deposit.updated_time)
        self.assertEqual(current_bill.balance, fake_account.balance + fake_deposit.amount)
        self.assertEqual(current_bill.amount,  fake_deposit.amount)
        self.assertEqual(current_bill.bill_type,  constants.BILL_TYPE_DEPOSIT)
        self.assertEqual(current_bill.bill_date,  updated_deposit.updated_time)
