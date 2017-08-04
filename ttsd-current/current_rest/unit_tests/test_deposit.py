# -*- coding: utf-8 -*-
import mock
from django.test import TestCase
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIClient

from current_rest import constants, serializers
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.models import CurrentAccount, CurrentDeposit, CurrentBill
from current_rest.views.deposit import DepositViewSet


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

        fake_requests.assert_called_once_with(url=DepositViewSet.pay_with_password_url,
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
        fake_requests.assert_called_once_with(url=DepositViewSet.pay_with_no_password_url,
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

    @mock.patch('current_rest.views.deposit.CurrentDailyManager')
    def test_should_return_200_when_deposit_callback_success(self, fake_manager):
        fake_account = CurrentAccountManager().fetch_account(self.login_name)
        fake_deposit = CurrentDeposit.objects.create(current_account=fake_account, login_name=self.login_name, amount=1)

        instance = fake_manager.return_value
        instance.get_current_daily_amount.return_value = 2

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

    def test_should_return_200_when_deposit_callback_fail(self):
        fake_account = CurrentAccountManager().fetch_account(self.login_name)
        fake_deposit = CurrentDeposit.objects.create(current_account=fake_account, login_name=self.login_name, amount=1)

        response = self.client.put(path=reverse('get_put_deposit', kwargs={'pk': fake_deposit.pk}),
                                   data={'status': constants.DEPOSIT_FAIL},
                                   format='json')

        self.assertEqual(response.status_code, status.HTTP_200_OK)

        updated_deposit = CurrentDeposit.objects.get(login_name=self.login_name)
        updated_account = CurrentAccount.objects.get(login_name=self.login_name)

        self.assertEqual(updated_deposit.status, constants.DEPOSIT_FAIL)
        self.assertEqual(updated_account.balance, fake_account.balance)
        self.assertFalse(CurrentBill.objects.filter(login_name=self.login_name).exists())

    def test_should_return_200_when_deposit_callback_success_and_over_personal_max_deposit(self):
        fake_account = CurrentAccount.objects.create(login_name=self.login_name, balance=constants.PERSONAL_MAX_DEPOSIT)
        fake_deposit = CurrentDeposit.objects.create(current_account=fake_account, login_name=self.login_name, amount=1)

        response = self.client.put(path=reverse('get_put_deposit', kwargs={'pk': fake_deposit.pk}),
                                   data={'status': constants.DEPOSIT_SUCCESS},
                                   format='json')

        self.assertEqual(response.status_code, status.HTTP_200_OK)

        updated_deposit = CurrentDeposit.objects.get(login_name=self.login_name)
        updated_account = CurrentAccount.objects.get(login_name=self.login_name)
        current_bill = CurrentBill.objects.get(login_name=self.login_name)

        self.assertEqual(updated_deposit.status, constants.DEPOSIT_OVER_PAY)
        self.assertEqual(updated_account.balance, fake_account.balance + fake_deposit.amount)
        self.assertEqual(current_bill.amount, fake_deposit.amount)
        self.assertEqual(current_bill.bill_type, constants.BILL_TYPE_DEPOSIT)

    @mock.patch('current_rest.views.deposit.sum_success_deposit_by_date')
    @mock.patch('current_rest.views.deposit.CurrentDailyManager')
    def test_should_return_200_when_deposit_callback_success_and_over_current_daily_max_amount(self, fake_manager,
                                                                                               sum_success_deposit_by_date):
        fake_account = CurrentAccount.objects.create(login_name=self.login_name, balance=constants.PERSONAL_MAX_DEPOSIT)
        fake_deposit = CurrentDeposit.objects.create(current_account=fake_account, login_name=self.login_name, amount=2)

        instance = fake_manager.return_value
        instance.get_current_daily_amount.return_value = 1
        sum_success_deposit_by_date.return_value = fake_deposit.amount

        response = self.client.put(path=reverse('get_put_deposit', kwargs={'pk': fake_deposit.pk}),
                                   data={'status': constants.DEPOSIT_SUCCESS},
                                   format='json')

        self.assertEqual(response.status_code, status.HTTP_200_OK)

        updated_deposit = CurrentDeposit.objects.get(login_name=self.login_name)
        updated_account = CurrentAccount.objects.get(login_name=self.login_name)
        current_bill = CurrentBill.objects.get(login_name=self.login_name)

        self.assertEqual(updated_deposit.status, constants.DEPOSIT_OVER_PAY)
        self.assertEqual(updated_account.balance, fake_account.balance + fake_deposit.amount)
        self.assertEqual(current_bill.amount, fake_deposit.amount)
        self.assertEqual(current_bill.bill_type, constants.BILL_TYPE_DEPOSIT)

    def test_should_return_404_when_deposit_does_not_exist(self):
        response = self.client.get(path=reverse('get_put_deposit', kwargs={'pk': 0}))
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)

    def test_should_return_200_when_deposit_exists(self):
        fake_account = CurrentAccountManager().fetch_account(self.login_name)
        fake_deposit = CurrentDeposit.objects.create(current_account=fake_account, login_name=self.login_name, amount=1)

        response = self.client.get(path=reverse('get_put_deposit', kwargs={'pk': fake_deposit.pk}))

        self.assertEqual(response.status_code, status.HTTP_200_OK)
