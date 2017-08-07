# -*- coding: utf-8 -*-
import datetime

from django.db import connection
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
        self.now = datetime.datetime.now()
        self.cursor = connection.cursor()
        self.fake_account_1 = CurrentAccount.objects.create(login_name='fakeuser1', username='fakeusername1',
                                                            mobile='fakemobile1')
        self.fake_account_2 = CurrentAccount.objects.create(login_name='fakeuser2', username='fakeusername2',
                                                            mobile='fakemobile2')

    def test_should_list(self):
        deposit_1 = CurrentDeposit.objects.create(current_account=self.fake_account_1,
                                                  login_name=self.fake_account_1.login_name,
                                                  amount=0,
                                                  source=constants.SOURCE_ANDROID,
                                                  status=constants.DEPOSIT_SUCCESS)

        deposit_2 = CurrentDeposit.objects.create(current_account=self.fake_account_2,
                                                  login_name=self.fake_account_2.login_name,
                                                  amount=0,
                                                  source=constants.SOURCE_IOS,
                                                  status=constants.DEPOSIT_FAIL)

        self.cursor.execute(
            'update current_deposit set updated_time=%s where id=%s',
            [(self.now.date()).strftime('%Y-%m-%d %H:%M:%S'), deposit_1.pk])

        self.cursor.execute(
            'update current_deposit set updated_time=%s where id=%s',
            [(self.now.date() - datetime.timedelta(days=1)).strftime('%Y-%m-%d %H:%M:%S'), deposit_2.pk])

        response = self.client.get(path=reverse('list_deposit'),
                                   data={'current_account__mobile': self.fake_account_1.mobile})
        response_data = response.data
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response_data.get('count'), 1)
        self.assertEqual(response_data.get('results')[0]['current_account']['mobile'],
                         self.fake_account_1.mobile)

        response = self.client.get(path=reverse('list_deposit'),
                                   data={'source': constants.SOURCE_IOS})
        response_data = response.data
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response_data.get('count'), 1)
        self.assertEqual(response_data.get('results')[0]['source'],
                         constants.SOURCE_IOS)

        response = self.client.get(path=reverse('list_deposit'),
                                   data={'status': constants.DEPOSIT_FAIL})
        response_data = response.data
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response_data.get('count'), 1)
        self.assertEqual(response_data.get('results')[0]['status'],
                         constants.DEPOSIT_FAIL)

        response = self.client.get(path=reverse('list_deposit'),
                                   data={'updated_time_0': self.now.strftime('%Y-%m-%d'),
                                         'updated_time_1': self.now.strftime('%Y-%m-%d')})
        response_data = response.data
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response_data.get('count'), 1)
        self.assertEqual(response_data.get('results')[0]['status'],
                         constants.DEPOSIT_SUCCESS)
