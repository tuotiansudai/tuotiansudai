# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

# Create your models here.
from current_rest import constants


class CurrentWithdraw(models.Model):
    account_id = models.IntegerField()
    amount = models.IntegerField()
    status = models.CharField(choices=constants.STATUS_CHOICES, default=constants.STATUS_WAITING, max_length=20)
    created_time = models.DateTimeField(auto_now_add=True)
    approve_time = models.DateTimeField(null=True, blank=True)
    approver = models.CharField(null=True, blank=True, max_length=30)

    class Meta:
        db_table = 'current_withdraw'


class CurrentAccount(models.Model):
    login_name = models.CharField(max_length=25, unique=True, null=False, blank=False)
    balance = models.PositiveIntegerField(default=0, null=False, blank=False)
    created_time = models.DateTimeField(auto_now_add=True, null=False, blank=False)
    updated_time = models.DateTimeField(auto_now_add=True, null=False, blank=False)

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
    updated_time = models.DateTimeField(auto_now_add=True, null=False, blank=False)

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
    updated_time = models.DateTimeField(auto_now_add=True, null=False, blank=False)

    class Meta:
        db_table = 'current_bill'
