# coding=utf-8
import re

from django import forms

from common import constants as common_constants
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


class RedeemForm(forms.Form):
    login_name = forms.RegexField(regex=re.compile('[A-Za-z0-9]{6,25}'), required=False)
    current_account__mobile = forms.RegexField(regex=re.compile('0?(13|14|15|18)[0-9]{9}'), required=False)
    start_time = forms.DateTimeField(required=False)
    end_time = forms.DateTimeField(required=False)
    start_amount = forms.IntegerField(required=False)
    end_amount = forms.IntegerField(required=False)
    status = forms.ChoiceField(choices=constants.REDEEM_STATUS_CHOICE, required=False)


class DepositForm(forms.Form):
    page = forms.IntegerField(required=False)
    current_account__mobile = forms.RegexField(regex=re.compile('\d{11}'), required=False)
    status = forms.ChoiceField(choices=common_constants.DepositStatusType.DEPOSIT_STATUS_CHOICE, required=False)
    source = forms.ChoiceField(choices=common_constants.SourceType.SOURCE_CHOICE, required=False)
    updated_time_0 = forms.DateField(input_formats=['%Y-%m-%d'], required=False)
    updated_time_1 = forms.DateField(input_formats=['%Y-%m-%d'], required=False)


class FundDistributionQueryForm(forms.Form):
    granularity = forms.CharField(max_length=10)
    begin_date = forms.DateField(input_formats=['%Y-%m-%d'])
    end_date = forms.DateField(input_formats=['%Y-%m-%d'])
