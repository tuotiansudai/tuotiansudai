# -*- coding: utf-8 -*-
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APITestCase, APIClient
from current_rest import constants
from current_rest.models import CurrentWithdraw, CurrentAccount


class RedeemTestCase(APITestCase):
    def setUp(self):
        self.client = APIClient()
        self.login_name = 'fakeuser'

    def test_create_withdraw_create(self):
        CurrentAccount.objects.create(login_name=self.login_name, balance=1000)

        url = reverse('post_redeem')
        data = {'login_name': self.login_name,
                'amount': 1000,
                'source': constants.SOURCE_IOS,
                'status': constants.STATUS_WAITING}

        response = self.client.post(url, data, format='json')
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(CurrentWithdraw.objects.count(), 1)
        self.assertEqual(CurrentWithdraw.objects.get().amount, 1000)

    def test_get_limits_when_current_balance_is_enough(self):
        CurrentAccount.objects.create(login_name=self.login_name, balance=10000000)
        max_amoount = constants.EVERY_DAY_OF_MAX_REDEEM_AMOUNT

        url = reverse('post_redeem')
        data1 = {'login_name': self.login_name,
                 'amount': 100000,
                 'source': constants.SOURCE_IOS,
                 'status': constants.STATUS_WAITING}
        self.client.post(url, data1, format='json')

        data2 = {'login_name': self.login_name,
                 'amount': 200000,
                 'source': constants.SOURCE_IOS,
                 'status': constants.STATUS_WAITING}
        self.client.post(url, data2, format='json')

        response = self.client.get('/rest/account/{}'.format(self.login_name))

        self.assertEqual(response.data['personal_max_redeem'], max_amoount)
        self.assertEqual(response.data['personal_available_redeem'], 9700000)

    def test_get_limits_when_current_balance_is_not_enough(self):
        CurrentAccount.objects.create(login_name=self.login_name, balance=30000)
        max_amoount = constants.EVERY_DAY_OF_MAX_REDEEM_AMOUNT

        url = reverse('post_redeem')
        data1 = {'login_name': self.login_name,
                 'amount': 100000,
                 'source': constants.SOURCE_IOS,
                 'status': constants.STATUS_WAITING}
        self.client.post(url, data1, format='json')

        data2 = {'login_name': self.login_name,
                 'amount': 200000,
                 'source': constants.SOURCE_IOS,
                 'status': constants.STATUS_WAITING}
        self.client.post(url, data2, format='json')

        response = self.client.get('/rest/account/{}'.format(self.login_name))

        self.assertEqual(response.data['personal_max_redeem'], max_amoount)
        self.assertEqual(response.data['personal_available_redeem'], 30000)

