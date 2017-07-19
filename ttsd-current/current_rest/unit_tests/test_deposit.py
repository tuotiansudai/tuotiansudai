# -*- coding: utf-8 -*-
import mock
from django.test import TestCase
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIClient

from current_rest import constants, serializers
from current_rest.biz import PERSONAL_MAX_DEPOSIT
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
        response = self.client.post(path=reverse('post_deposit'),
                                    data={'login_name': self.login_name,
                                          'amount': 1,
                                          'source': constants.SOURCE_IOS,
                                          'no_password': False},
                                    format='json')

        deposit = CurrentDeposit.objects.get(login_name=self.login_name)

        fake_requests.assert_called_once_with(url=Deposit.pay_with_password_url,
                                              json=serializers.DepositSerializer(instance=deposit).data,
                                              timeout=10)
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
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
        response = self.client.post(path=reverse('post_deposit'),
                                    data={'login_name': self.login_name,
                                          'amount': 1,
                                          'source': constants.SOURCE_IOS,
                                          'no_password': True},
                                    format='json')

        deposit = CurrentDeposit.objects.get(login_name=self.login_name)
        fake_requests.assert_called_once_with(url=Deposit.pay_with_no_password_url,
                                              json=serializers.DepositSerializer(instance=deposit).data,
                                              timeout=10)

        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(response.data, pay_response)

        self.assertTrue(CurrentAccount.objects.filter(login_name=self.login_name).exists())
        self.assertEqual(deposit.amount, 1)
        self.assertEqual(deposit.status, constants.DEPOSIT_WAITING_PAY)
        self.assertEqual(deposit.source, constants.SOURCE_IOS)
        self.assertTrue(deposit.no_password)

    def test_should_return_400_when_deposit_post_data_is_illegal(self):
        response = self.client.post(path=reverse('post_deposit'),
                                    data={'login_name': u'名字是汉字',
                                          'amount': 1,
                                          'source': constants.SOURCE_IOS,
                                          'no_password': True},
                                    format='json')
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)

        response = self.client.post(path=reverse('post_deposit'),
                                    data={'login_name': self.login_name,
                                          'amount': u'不是数字',
                                          'source': constants.SOURCE_IOS,
                                          'no_password': True},
                                    format='json')
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)

        response = self.client.post(path=reverse('post_deposit'),
                                    data={'login_name': self.login_name,
                                          'amount': 1,
                                          'source': u'fake',
                                          'no_password': True},
                                    format='json')
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)

        response = self.client.post(path=reverse('post_deposit'),
                                    data={'login_name': self.login_name,
                                          'amount': 1,
                                          'source': constants.SOURCE_IOS,
                                          'no_password': u'fake'},
                                    format='json')
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)

    def test_should_return_400_when_deposit_callback_data_is_illegal(self):
        fake_account = CurrentAccountManager().fetch_account(self.login_name)
        fake_deposit = CurrentDeposit.objects.create(current_account=fake_account, login_name=self.login_name, amount=1)

        response = self.client.put(path=reverse('get_put_deposit', kwargs={'pk': 0}),
                                   data={'status': constants.DEPOSIT_SUCCESS},
                                   format='json')
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)

        response = self.client.put(path=reverse('get_put_deposit', kwargs={'pk': fake_deposit.pk}),
                                   data={'status': u'不存在状态'},
                                   format='json')
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)

    def test_should_return_200_when_deposit_callback_data_is_legal(self):
        fake_account = CurrentAccountManager().fetch_account(self.login_name)
        fake_deposit = CurrentDeposit.objects.create(current_account=fake_account, login_name=self.login_name, amount=1)

        response = self.client.put(path=reverse('get_put_deposit', kwargs={'pk': fake_deposit.pk}),
                                   data={'status': constants.DEPOSIT_SUCCESS},
                                   format='json')

        self.assertEqual(response.status_code, status.HTTP_200_OK)

        updated_deposit = CurrentDeposit.objects.get(login_name=self.login_name)
        updated_account = CurrentAccount.objects.get(login_name=self.login_name)
        current_bill = CurrentBill.objects.get(login_name=self.login_name)

        self.assertEqual(updated_deposit.status, constants.DEPOSIT_SUCCESS)
        self.assertEqual(updated_account.balance, fake_account.balance + fake_deposit.amount)
        self.assertEqual(current_bill.balance, fake_account.balance + fake_deposit.amount)
        self.assertEqual(current_bill.amount, fake_deposit.amount)
        self.assertEqual(current_bill.bill_type, constants.BILL_TYPE_DEPOSIT)

    # @mock.patch('current_rest.biz.deposit.CurrentDailyManager')
    # def test_should_return_200_when_today_is_no_deposit(self, fake_manager):
    #     current_daily_amount = 1
    #     instance = fake_manager.return_value
    #     instance.get_current_daily_amount.return_value = current_daily_amount
    #
    #     response = self.client.get(path=reverse('personal_max_deposit', kwargs={'login_name': self.login_name}))
    #
    #     self.assertEqual(response.data, {'amount': current_daily_amount})
    #
    # @mock.patch('current_rest.biz.deposit.CurrentDailyManager')
    # def test_should_return_200_when_today_is_no_deposit_and_user_has_deposited_1(self, fake_manager):
    #     current_daily_amount = 100000000
    #
    #     CurrentAccount.objects.create(login_name=self.login_name, balance=1)
    #
    #     instance = fake_manager.return_value
    #     instance.get_current_daily_amount.return_value = current_daily_amount
    #
    #     response = self.client.get(path=reverse('personal_max_deposit', kwargs={'login_name': self.login_name}))
    #
    #     self.assertEqual(response.data, {'amount': PERSONAL_MAX_DEPOSIT - 1})
    #
    # @mock.patch('current_rest.biz.deposit.CurrentDailyManager')
    # def test_should_return_200_when_today_current_limit_is_100_and_user_has_deposited_1(self, fake_manager):
    #     current_daily_amount = 100
    #
    #     CurrentAccount.objects.create(login_name=self.login_name, balance=1)
    #
    #     instance = fake_manager.return_value
    #     instance.get_current_daily_amount.return_value = current_daily_amount
    #
    #     response = self.client.get(path=reverse('personal_max_deposit', kwargs={'login_name': self.login_name}))
    #
    #     self.assertEqual(response.data, {'amount': current_daily_amount})

    def test_should_return_404_when_deposit_does_not_exist(self):
        response = self.client.get(path=reverse('get_put_deposit', kwargs={'pk': 0}))
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)

    def test_should_return_200_when_deposit_exists(self):
        fake_account = CurrentAccountManager().fetch_account(self.login_name)
        fake_deposit = CurrentDeposit.objects.create(current_account=fake_account, login_name=self.login_name, amount=1)

        response = self.client.get(path=reverse('get_put_deposit', kwargs={'pk': fake_deposit.pk}))

        self.assertEqual(response.status_code, status.HTTP_200_OK)
