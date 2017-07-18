# coding=utf-8
import re

from django import forms
from django.core.validators import RegexValidator

from current_rest import constants


class LoanForm(forms.Form):
    debtor = forms.RegexField(regex=re.compile('[A-Za-z0-9]{6,25}'), required=True)
    debtor_identity_card = forms.CharField(required=True, max_length=18)
    agent = forms.IntegerField(required=True)
    amount = forms.IntegerField(min_value=0, max_value=99999, required=True)
    type = forms.ChoiceField(choices=constants.LOAN_TYPE_CHOICES, required=True)
    effective_date = forms.DateTimeField(required=True)
    expiration_date = forms.DateTimeField(required=True)
