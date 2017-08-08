# -*- coding: utf-8 -*-
import datetime

from django.db import connection
from django.test import TestCase
from django.urls import reverse
from mock import mock
from rest_framework import status
from rest_framework.test import APIClient

from current_rest import constants, settings
from current_rest.models import CurrentAccount, CurrentBill, CurrentLoanOutHistory, CurrentDeposit
from current_rest.serializers import LoanOutHistorySerializer


class LoanOutTestCase(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.login_name = 'fakeuser'
        self.now = datetime.datetime.now()
        self.cursor = connection.cursor()

    @mock.patch('current_rest.views.loan.pay_manager')
    def test_should_return_200_when_loan_out_and_sum_interest_is_more_than_0(self, pay_manager):
        pay_manager.invoke_pay = mock.Mock(return_value={})
        account = CurrentAccount.objects.create(login_name=self.login_name)

        CurrentBill.objects.create(current_account=account,
                                   login_name=self.login_name,
                                   bill_date=self.now.date() - datetime.timedelta(days=1),
                                   bill_type=constants.BILL_TYPE_INTEREST,
                                   balance=1, amount=1, order_id=1)

        CurrentBill.objects.create(current_account=account,
                                   login_name=self.login_name,
                                   bill_date=self.now.date(),
                                   bill_type=constants.BILL_TYPE_INTEREST,
                                   balance=3, amount=2, order_id=2)

        CurrentBill.objects.create(current_account=account,
                                   login_name=self.login_name,
                                   bill_date=self.now.date() + datetime.timedelta(days=1),
                                   bill_type=constants.BILL_TYPE_INTEREST,
                                   balance=6, amount=3, order_id=3)

        deposit_1 = CurrentDeposit.objects.create(current_account=account, login_name=self.login_name, amount=1,
                                                  status=constants.DEPOSIT_SUCCESS)

        deposit_2 = CurrentDeposit.objects.create(current_account=account, login_name=self.login_name, amount=2,
                                                  status=constants.DEPOSIT_SUCCESS)

        deposit_3 = CurrentDeposit.objects.create(current_account=account, login_name=self.login_name, amount=3,
                                                  status=constants.DEPOSIT_SUCCESS)

        self.cursor.execute(
            'update current_deposit set updated_time=%s where id=%s',
            [(self.now.date() - datetime.timedelta(days=1)).strftime('%Y-%m-%d %H:%M:%S'), deposit_1.pk])
        self.cursor.execute(
            'update current_deposit set updated_time=%s where id=%s',
            [self.now.strftime('%Y-%m-%d %H:%M:%S'), deposit_2.pk])
        self.cursor.execute(
            'update current_deposit set updated_time=%s where id=%s',
            [(self.now.date() + datetime.timedelta(days=1)).strftime('%Y-%m-%d %H:%M:%S'), deposit_3.pk])

        response = self.client.post(path=reverse('post_loan_out'), data={'bill_date': self.now.strftime('%Y-%m-%d')},
                                    format='json')

        serializer = LoanOutHistorySerializer(CurrentLoanOutHistory.objects.get(bill_date=self.now.date()))
        pay_manager.invoke_pay.assert_called_once_with(url='{}/loan-out/'.format(settings.PAY_WRAPPER_SERVER),
                                                       data=serializer.data)

        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(response.data.get('reserve_account'), settings.RESERVE_ACCOUNT)
        self.assertEqual(response.data.get('agent_account'), settings.AGENT_ACCOUNT)
        self.assertEqual(response.data.get('interest_amount'), 2)
        self.assertEqual(response.data.get('deposit_amount'), 2)
        self.assertEqual(response.data.get('bill_date'), self.now.strftime('%Y-%m-%d'))
        self.assertEqual(response.data.get('status'), constants.LOAN_OUT_STATUS_RESERVE_TRANSFER_WAITING_PAY)

    @mock.patch('current_rest.views.loan.pay_manager')
    def test_should_return_200_when_loan_out_and_sum_interest_is_0(self, pay_manager):
        pay_manager.invoke_pay = mock.Mock(return_value={})
        account = CurrentAccount.objects.create(login_name=self.login_name)

        deposit_1 = CurrentDeposit.objects.create(current_account=account, login_name=self.login_name, amount=1,
                                                  status=constants.DEPOSIT_SUCCESS)

        deposit_2 = CurrentDeposit.objects.create(current_account=account, login_name=self.login_name, amount=2,
                                                  status=constants.DEPOSIT_SUCCESS)

        deposit_3 = CurrentDeposit.objects.create(current_account=account, login_name=self.login_name, amount=3,
                                                  status=constants.DEPOSIT_SUCCESS)

        self.cursor.execute(
            'update current_deposit set updated_time=%s where id=%s',
            [(self.now.date() - datetime.timedelta(days=1)).strftime('%Y-%m-%d %H:%M:%S'), deposit_1.pk])
        self.cursor.execute(
            'update current_deposit set updated_time=%s where id=%s',
            [self.now.strftime('%Y-%m-%d %H:%M:%S'), deposit_2.pk])
        self.cursor.execute(
            'update current_deposit set updated_time=%s where id=%s',
            [(self.now.date() + datetime.timedelta(days=1)).strftime('%Y-%m-%d %H:%M:%S'), deposit_3.pk])

        response = self.client.post(path=reverse('post_loan_out'), data={'bill_date': self.now.strftime('%Y-%m-%d')},
                                    format='json')

        serializer = LoanOutHistorySerializer(CurrentLoanOutHistory.objects.get(bill_date=self.now.date()))
        pay_manager.invoke_pay.assert_called_once_with(url='{}/loan-out/'.format(settings.PAY_WRAPPER_SERVER),
                                                       data=serializer.data)

        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(response.data.get('reserve_account'), settings.RESERVE_ACCOUNT)
        self.assertEqual(response.data.get('agent_account'), settings.AGENT_ACCOUNT)
        self.assertEqual(response.data.get('interest_amount'), 0)
        self.assertEqual(response.data.get('deposit_amount'), 2)
        self.assertEqual(response.data.get('bill_date'), self.now.strftime('%Y-%m-%d'))
        self.assertEqual(response.data.get('status'), constants.LOAN_OUT_STATUS_WAITING_PAY)

    def test_should_return_200_when_loan_out_and_interest_and_deposit_are_both_0(self):
        response = self.client.post(path=reverse('post_loan_out'), data={'bill_date': self.now.strftime('%Y-%m-%d')},
                                    format='json')

        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(response.data.get('reserve_account'), settings.RESERVE_ACCOUNT)
        self.assertEqual(response.data.get('agent_account'), settings.AGENT_ACCOUNT)
        self.assertEqual(response.data.get('interest_amount'), 0)
        self.assertEqual(response.data.get('deposit_amount'), 0)
        self.assertEqual(response.data.get('bill_date'), self.now.strftime('%Y-%m-%d'))
        self.assertEqual(response.data.get('status'), constants.LOAN_OUT_STATUS_SUCCESS)

    def test_should_return_400_when_loan_out_is_in_progress(self):
        CurrentLoanOutHistory.objects.create(bill_date=datetime.date(year=2015, month=1, day=1),
                                             status=constants.LOAN_OUT_STATUS_SUCCESS)

        response = self.client.post(path=reverse('post_loan_out'), data={'bill_date': '2015-01-01'}, format='json')

        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)

    def test_should_return_400_when_loan_out_history_not_found(self):
        response = self.client.put(path=reverse('put_loan_out', kwargs={'pk': 0}),
                                   data={'status': constants.LOAN_OUT_STATUS_RESERVE_TRANSFER_SUCCESS},
                                   format='json')

        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)

    @mock.patch('current_rest.views.loan.pay_manager')
    def test_should_return_200_when_reverse_transfer_is_success_and_sum_deposit_is_0(self, pay_manager):
        pay_manager.invoke_pay = mock.Mock(return_value={})

        loan_out_history = CurrentLoanOutHistory.objects.create(bill_date=self.now.date(), deposit_amount=0)

        response = self.client.put(path=reverse('put_loan_out', kwargs={'pk': loan_out_history.id}),
                                   data={'status': constants.LOAN_OUT_STATUS_RESERVE_TRANSFER_SUCCESS},
                                   format='json')

        pay_manager.invoke_pay.assert_not_called()
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data.get('bill_date'), self.now.strftime('%Y-%m-%d'))
        self.assertEqual(response.data.get('status'), constants.LOAN_OUT_STATUS_SUCCESS)

    @mock.patch('current_rest.views.loan.pay_manager')
    def test_should_return_200_when_reverse_transfer_is_success_and_sum_deposit_is_not_0(self, pay_manager):
        pay_manager.invoke_pay = mock.Mock(return_value={})

        loan_out_history = CurrentLoanOutHistory.objects.create(bill_date=self.now.date(), deposit_amount=1)

        response = self.client.put(path=reverse('put_loan_out', kwargs={'pk': loan_out_history.id}),
                                   data={'status': constants.LOAN_OUT_STATUS_RESERVE_TRANSFER_SUCCESS},
                                   format='json')

        serializer = LoanOutHistorySerializer(CurrentLoanOutHistory.objects.get(bill_date=self.now.date()))
        pay_manager.invoke_pay.assert_called_once_with(url='{}/loan-out/'.format(settings.PAY_WRAPPER_SERVER),
                                                       data=serializer.data)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data.get('bill_date'), self.now.strftime('%Y-%m-%d'))
        self.assertEqual(response.data.get('status'), constants.LOAN_OUT_STATUS_WAITING_PAY)
