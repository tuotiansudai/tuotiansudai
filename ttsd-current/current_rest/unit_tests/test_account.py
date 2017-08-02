# -*- coding: utf-8 -*-
import mock
from django.test import TestCase
from django.urls import reverse
from rest_framework.test import APIClient

from current_rest import constants
from current_rest.models import CurrentAccount


class DepositTestCase(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.login_name = 'fakeuser'

    @mock.patch('current_rest.serializers.calculate_success_deposit_today')
    @mock.patch('current_rest.serializers.CurrentDailyManager')
    def test_should_return_200_when_today_is_no_deposit(self, fake_manager, calculate_success_deposit_today):
        CurrentAccount.objects.create(login_name=self.login_name)

        current_daily_amount = 1
        instance = fake_manager.return_value
        instance.get_current_daily_amount.return_value = current_daily_amount
        calculate_success_deposit_today.return_value = 0

        response = self.client.get(path=reverse('get_account', kwargs={'login_name': self.login_name}))

        self.assertEqual(response.data.get('personal_max_deposit'), current_daily_amount)

    @mock.patch('current_rest.serializers.calculate_success_deposit_today')
    @mock.patch('current_rest.serializers.CurrentDailyManager')
    def test_should_return_200_when_today_is_no_deposit_and_user_has_deposited_1(self, fake_manager,
                                                                                 calculate_success_deposit_today):
        CurrentAccount.objects.create(login_name=self.login_name, balance=1)
        current_daily_amount = constants.PERSONAL_MAX_DEPOSIT + 1

        instance = fake_manager.return_value
        instance.get_current_daily_amount.return_value = current_daily_amount
        calculate_success_deposit_today.return_value = 1

        response = self.client.get(path=reverse('get_account', kwargs={'login_name': self.login_name}))

        self.assertEqual(response.data.get('personal_max_deposit'), constants.PERSONAL_MAX_DEPOSIT - 1)

    @mock.patch('current_rest.serializers.calculate_success_deposit_today')
    @mock.patch('current_rest.serializers.CurrentDailyManager')
    def test_should_return_200_when_today_current_limit_is_100_and_user_had_deposited_1_before_today(self, fake_manager,
                                                                                                     calculate_success_deposit_today):
        CurrentAccount.objects.create(login_name=self.login_name, balance=1)
        current_daily_amount = 100

        instance = fake_manager.return_value
        instance.get_current_daily_amount.return_value = current_daily_amount
        calculate_success_deposit_today.return_value = 0

        response = self.client.get(path=reverse('get_account', kwargs={'login_name': self.login_name}))

        self.assertEqual(response.data.get('personal_max_deposit'), current_daily_amount)
