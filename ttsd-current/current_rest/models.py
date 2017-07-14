# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

# Create your models here.
from current_rest import constants


class Agent(models.Model):
    login_name = models.CharField(null=False, max_length=60, default=None)
    mobile = models.CharField(null=False, max_length=20, default=None)
    active = models.BooleanField(default=False, null=False)
    create_time = models.DateTimeField()
    update_time = models.DateTimeField()

    class Meta:
        db_table = 'agent'


class Loan(models.Model):
    serial_number = models.IntegerField()
    amount = models.FloatField(null=True)
    type = models.CharField(choices=constants.LOAN_TYPE_CHOICES, null=False, max_length=40)
    agent = models.ForeignKey(Agent, on_delete=models.PROTECT, null=True, related_name='+')
    debtor = models.CharField(null=False, max_length=60, )
    debtor_identity_card = models.CharField(null=False, max_length=30)
    effective_date = models.DateTimeField(null=False)
    expiration_date = models.DateTimeField(null=False)
    create_time = models.DateTimeField(auto_now=True,blank=False, null=False)
    update_time = models.DateTimeField(auto_now=True)
    status = models.CharField(choices=constants.LOAN_STATUS_CHOICES, null=False, max_length=20)

    class Meta:
        db_table = 'loan'


class OperationLog(models.Model):
    refer_type = models.CharField(choices=constants.OperationTarget.OPERATION_TARGET_TYPE, max_length=50)
    refer_pk = models.IntegerField()
    operator = models.CharField(null=False, max_length=50)
    operation_type = models.CharField(choices=constants.OperationType.OPERATION_TYPE_MAP, max_length=100, null=True)
    timestamp = models.DateTimeField()
    content = models.TextField()

    class Meta:
        db_table = 'operation_log'
