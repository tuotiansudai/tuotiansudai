# coding=utf-8
from django import forms
from django.core.validators import RegexValidator


# class LoanForm(forms.Form):
#     amount = forms.FloatField(max_value="99999")
#     effective_date = forms.DateTimeField(input_formats='%Y-%m-%d')
#     expiration_date = forms.DateTimeField(input_formats='%Y-%m-%d')
#     debtor = forms.CharField(max_length=24)
#     debtor_identity_card = forms.CharField(max_length=18)
