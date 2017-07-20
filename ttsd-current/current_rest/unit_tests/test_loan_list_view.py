import datetime
import json

from django.test import Client
from django.test import TestCase
from django.test.utils import setup_test_environment
from rest_framework import status
from rest_framework.reverse import reverse

from current_rest.models import Agent, Loan
from current_rest.serializers import LoanSerializer

setup_test_environment()
client = Client()


class LoanListViewTests(TestCase):
    def setUp(self):
        Agent.objects.create(id=9999999, login_name='login_name',
                             mobile='mobile',
                             active=True,
                             create_time=datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
                             update_time=datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S'), )

    def test_loan_list_view_is_success(self):
        data = {
            "serial_number": 1234,
            "amount": 600.0,
            "loan_type": "HOUSE",
            "debtor": "debtor111",
            "debtor_identity_card": "44421022198601056653",
            "effective_date": "2017-09-09T00:00:00Z",
            "expiration_date": "2017-10-09T00:00:00Z",
            "status": "APPROVED",
            "agent": 9999999
        }
        response = client.post(reverse("loan_list"), json.dumps(data), content_type="application/json")
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertIsNotNone(response.data)
        self.assertIsNotNone(response.data['id'])
        self.assertEqual(response.data['serial_number'], data['serial_number'])
        self.assertEqual(response.data['amount'], data['amount'])
        self.assertEqual(response.data['loan_type'], data['loan_type'])
        self.assertEqual(response.data['debtor'], data['debtor'])
        self.assertEqual(response.data['debtor_identity_card'], data['debtor_identity_card'])
        self.assertEqual(response.data['effective_date'], data['effective_date'])
        self.assertEqual(response.data['expiration_date'], data['expiration_date'])
        self.assertEqual(response.data['status'], data['status'])
        self.assertEqual(response.data['agent'], data['agent'])

        return_dict = LoanSerializer(Loan.objects.get(id=response.data['id'])).data
        self.assertEqual(return_dict['serial_number'], data['serial_number'])
        self.assertEqual(return_dict['amount'], data['amount'])
        self.assertEqual(return_dict['type'], data['type'])
        self.assertEqual(return_dict['debtor'], data['debtor'])
        self.assertEqual(return_dict['debtor_identity_card'], data['debtor_identity_card'])
        self.assertEqual(return_dict['effective_date'], data['effective_date'])
        self.assertEqual(return_dict['expiration_date'], data['expiration_date'])
        self.assertEqual(return_dict['status'], data['status'])
        self.assertEqual(return_dict['agent'], data['agent'])
