# -*- coding: utf-8 -*-
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APITestCase, APIClient

from current_rest import constants
from current_rest.biz.current_account_manager import CurrentAccountManager
from current_rest.models import CurrentRedeem, CurrentAccount


class RedeemTestCase(APITestCase):
    def setUp(self):
        self.client = APIClient()
        self.login_name = 'fakeuser'

    def test_create_redeem_create(self):
        CurrentAccount.objects.create(login_name=self.login_name, balance=1000)

        url = reverse('post_redeem')
        data = {'login_name': self.login_name,
                'amount': 1000}

        response = self.client.post(url, data, format='json')
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(CurrentRedeem.objects.count(), 1)
        self.assertEqual(CurrentRedeem.objects.get().amount, 1000)
        self.assertEqual(CurrentRedeem.objects.get().status, constants.REDEEM_WAITING)

    def test_should_return_404_when_redeem_does_not_exist(self):
        response = self.client.get(path=reverse('get_put_redeem', kwargs={'pk': 0}))
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)

    def test_should_return_200_when_deposit_exists(self):
        fake_account = CurrentAccountManager().fetch_account(self.login_name)
        fake_deposit = CurrentRedeem.objects.create(current_account=fake_account, login_name=self.login_name, amount=1)

        response = self.client.get(path=reverse('get_put_redeem', kwargs={'pk': fake_deposit.pk}))
        self.assertEqual(response.status_code, status.HTTP_200_OK)

    def test_get_limits_when_current_balance_is_enough(self):
        CurrentAccount.objects.create(login_name=self.login_name, balance=10000000)
        max_amount = constants.EVERY_DAY_OF_MAX_REDEEM_AMOUNT

        url = reverse('post_redeem')
        data1 = {'login_name': self.login_name,
                 'amount': 100000}
        self.client.post(url, data1, format='json')

        data2 = {'login_name': self.login_name,
                 'amount': 200000,
                 'source': constants.SOURCE_IOS}
        self.client.post(url, data2, format='json')

        response = self.client.get(path=reverse('get_account', kwargs={'login_name': self.login_name}))

        self.assertEqual(response.data['personal_max_redeem'], max_amount)
        self.assertEqual(response.data['personal_available_redeem'], 9700000)

    def test_get_limits_when_current_balance_is_not_enough(self):
        CurrentAccount.objects.create(login_name=self.login_name, balance=30000)
        max_amount = constants.EVERY_DAY_OF_MAX_REDEEM_AMOUNT

        url = reverse('post_redeem')
        data1 = {'login_name': self.login_name,
                 'amount': 100000,
                 'source': constants.SOURCE_IOS}
        self.client.post(url, data1, format='json')

        data2 = {'login_name': self.login_name,
                 'amount': 200000,
                 'source': constants.SOURCE_IOS}
        self.client.post(url, data2, format='json')

        response = self.client.get(path=reverse('get_account', kwargs={'login_name': self.login_name}))

        self.assertEqual(response.data['personal_max_redeem'], max_amount)
        self.assertEqual(response.data['personal_available_redeem'], 30000)

    # 赎回审核通过
    def test_audit_redeem_pass(self):
        account = CurrentAccount.objects.create(login_name=self.login_name, balance=1000)

        redeem = CurrentRedeem.objects.create(current_account=account, login_name=self.login_name, amount=1000,
                                              status=constants.REDEEM_WAITING, source=constants.SOURCE_ANDROID)

        response = self.client.put('/redeem-audit/{}/{}'.format(redeem.id, "pass"), dict(auditor="auditor"))

        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(CurrentRedeem.objects.get().status, constants.REDEEM_DOING)

    # 赎回审核驳回
    def test_audit_redeem_reject(self):
        account = CurrentAccount.objects.create(login_name=self.login_name, balance=1000)

        redeem = CurrentRedeem.objects.create(current_account=account, login_name=self.login_name, amount=1000,
                                              status=constants.REDEEM_WAITING, source=constants.SOURCE_ANDROID)

        response = self.client.put('/redeem-audit/{}/{}'.format(redeem.id, "reject"), dict(auditor="auditor"))

        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(CurrentRedeem.objects.get().status, constants.REDEEM_REJECT)
