# -*- coding: utf-8 -*-
import mock
from django.test import TestCase
from django.urls import reverse
from rest_framework.test import APIClient

from current_rest.biz import PERSONAL_MAX_DEPOSIT
from current_rest.models import CurrentAccount


class DepositTestCase(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.login_name = 'fakeuser'

    @mock.patch('current_rest.serializers.CurrentDailyManager')
    def test_should_return_200_when_today_is_no_deposit(self, fake_manager):
        CurrentAccount.objects.create(login_name=self.login_name)

        current_daily_amount = 1
        instance = fake_manager.return_value
        instance.get_current_daily_amount.return_value = current_daily_amount

        response = self.client.get(path=reverse('get_account', kwargs={'login_name': self.login_name}))

        self.assertEqual(response.data.get('personal_max_deposit'), current_daily_amount)

    @mock.patch('current_rest.serializers.CurrentDailyManager')
    def test_should_return_200_when_today_is_no_deposit_and_user_has_deposited_1(self, fake_manager):
        CurrentAccount.objects.create(login_name=self.login_name, balance=1)

        current_daily_amount = 100000000

        instance = fake_manager.return_value
        instance.get_current_daily_amount.return_value = current_daily_amount

        response = self.client.get(path=reverse('get_account', kwargs={'login_name': self.login_name}))

        self.assertEqual(response.data.get('personal_max_deposit'), PERSONAL_MAX_DEPOSIT - 1)

    @mock.patch('current_rest.serializers.CurrentDailyManager')
    def test_should_return_200_when_today_current_limit_is_100_and_user_has_deposited_1(self, fake_manager):
        current_daily_amount = 100

        CurrentAccount.objects.create(login_name=self.login_name, balance=1)

        instance = fake_manager.return_value
        instance.get_current_daily_amount.return_value = current_daily_amount

        response = self.client.get(path=reverse('get_account', kwargs={'login_name': self.login_name}))

        self.assertEqual(response.data.get('personal_max_deposit'), current_daily_amount)
