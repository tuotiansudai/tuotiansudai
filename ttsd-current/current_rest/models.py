# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

# Create your models here.
from current_rest import constants


class BaseModel(models.Model):
    created_time = models.DateTimeField(auto_now_add=True, blank=False, null=False)
    updated_time = models.DateTimeField(auto_now=True)

    class Meta:
        abstract = True


class Agent(BaseModel):
    login_name = models.CharField(null=False, max_length=60, default=None)
    mobile = models.CharField(null=False, max_length=20, default=None)
    active = models.BooleanField(default=False, null=False)

    class Meta:
        db_table = 'agent'


class Loan(BaseModel):
    serial_number = models.IntegerField()
    amount = models.FloatField(null=True)
    loan_type = models.CharField(choices=constants.LOAN_TYPE_CHOICES, null=False, max_length=40)
    agent = models.ForeignKey(Agent, on_delete=models.PROTECT, null=True, related_name='+')
    debtor = models.CharField(null=False, max_length=60, )
    debtor_identity_card = models.CharField(null=False, max_length=18)
    effective_date = models.DateTimeField(null=False)
    expiration_date = models.DateTimeField(null=False)
    creator = models.CharField(max_length=25, null=False)
    auditor = models.CharField(max_length=25, null=False, blank=False)
    status = models.CharField(choices=constants.LOAN_STATUS_CHOICES, null=False, max_length=20)

    class Meta:
        db_table = 'loan'


class OperationLog(models.Model):
    refer_type = models.CharField(choices=constants.OperationTarget.OPERATION_TARGET_TYPE, max_length=50)
    refer_pk = models.IntegerField()
    operator = models.CharField(null=False, max_length=50)
    operation_type = models.CharField(choices=constants.OperationType.OPERATION_TYPE_MAP, max_length=100, null=True)
    timestamp = models.DateTimeField(auto_now_add=True, null=False, blank=False)
    content = models.TextField()

    class Meta:
        db_table = 'operation_log'


class CurrentAccount(models.Model):
    login_name = models.CharField(max_length=25, unique=True, null=False, blank=False)
    balance = models.PositiveIntegerField(default=0, null=False, blank=False)
    created_time = models.DateTimeField(auto_now_add=True, null=False, blank=False)
    updated_time = models.DateTimeField(auto_now=True, null=False, blank=False)

    class Meta:
        db_table = 'current_account'


class CurrentDeposit(models.Model):
    current_account = models.ForeignKey(to=CurrentAccount,
                                        on_delete=models.CASCADE,
                                        related_name='current_deposits',
                                        related_query_name='current_deposit',
                                        null=True,
                                        blank=False)
    login_name = models.CharField(max_length=25, null=False, blank=False)
    amount = models.PositiveIntegerField(null=False, blank=False)
    status = models.CharField(choices=constants.DEPOSIT_STATUS_CHOICE, max_length=20, null=False, blank=False,
                              default=constants.DEPOSIT_WAITING_PAY)
    source = models.CharField(choices=constants.SOURCE_CHOICE, default=constants.SOURCE_WEB,
                              max_length=10, null=False, blank=False)
    no_password = models.BooleanField(default=False, null=False, blank=False)
    created_time = models.DateTimeField(auto_now_add=True, null=False, blank=False)
    updated_time = models.DateTimeField(auto_now=True, null=False, blank=False)

    class Meta:
        db_table = 'current_deposit'


class CurrentBill(models.Model):
    current_account = models.ForeignKey(to=CurrentAccount,
                                        on_delete=models.CASCADE,
                                        related_name='current_bills',
                                        related_query_name='current_bill',
                                        null=False,
                                        blank=False)
    login_name = models.CharField(max_length=25, null=False, blank=False)
    bill_date = models.DateTimeField(null=False, blank=False)
    bill_type = models.CharField(choices=constants.BILL_TYPE_CHOICE, max_length=10, null=False, blank=False)
    amount = models.PositiveIntegerField(null=False, blank=False)
    balance = models.PositiveIntegerField(null=False, blank=False)
    order_id = models.IntegerField(null=False, blank=False)
    created_time = models.DateTimeField(auto_now_add=True, null=False, blank=False)
    updated_time = models.DateTimeField(auto_now=True, null=False, blank=False)

    class Meta:
        db_table = 'current_bill'


class CurrentDailyFundInfo(BaseModel):
    date = models.DateField(null=False, blank=False)
    loan_remain_amount = models.PositiveIntegerField(default=0, null=False, blank=False)
    quota_amount = models.PositiveIntegerField(default=0, null=False, blank=False)
    config_quota_amount = models.PositiveIntegerField(null=True)
    config_quota_status = models.CharField(choices=constants.DAILY_QUOTA_STATUS_CHOICES, max_length=20,
                                           default=constants.DAILY_QUOTA_STATUS_UNSET, null=False, blank=False)
    invest_amount = models.PositiveIntegerField(null=True)

    class Meta:
        db_table = 'current_daily_fund_info'
