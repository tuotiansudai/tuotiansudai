# coding=utf-8
import re

from django import forms

from current_console import constants


class LoanForm(forms.Form):
    debtor = forms.RegexField(regex=re.compile('[A-Za-z0-9]{6,25}'))
    debtor_identity_card = forms.CharField(max_length=18)
    agent = forms.IntegerField()
    amount = forms.IntegerField(min_value=0, max_value=99999)
    loan_type = forms.ChoiceField(choices=constants.LOAN_TYPE_CHOICES)
    effective_date = forms.DateTimeField()
    expiration_date = forms.DateTimeField()


class FundSettingHistoryQueryForm(forms.Form):
    begin_date = forms.DateField(input_formats=['%Y-%m-%d'])
    end_date = forms.DateField(input_formats=['%Y-%m-%d'])


class FundDistributionQueryForm(forms.Form):
    granularity = forms.CharField(max_length=10)
    begin_date = forms.DateField(input_formats=['%Y-%m-%d'])
    end_date = forms.DateField(input_formats=['%Y-%m-%d'])
