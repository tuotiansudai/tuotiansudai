# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

# Create your models here.
from current_rest import constants


class Loan(models.Model):
    serial_number = models.IntegerField()
    amount = models.FloatField(null=True)
    type = models.CharField(choices=constants.LOAN_TYPE_CHOICES, null=False, max_length=40)
    agent = models.ForeignKey(Agent, on_delete=models.PROTECT, null=True, related_name='+')
    debtor = models.FloatField(null=False, max_length=60, )
    debtor_identity_card = models.FloatField(null=False, max_length=30)
    effective_date = models.DateTimeField(null=False)
    expiration_date = models.DateTimeField(null=False)
    status = models.CharField(choices=constants.LOAN_STATUS_CHOICES, null=False, max_length=20)


class Agent(models.Model):
    login_name = models.CharField(null=False, max_length=60, default=None)
    mobile = models.CharField(null=False, max_length=20, default=None)
    active = models.BooleanField(default=False, null=False)
    create_time = models.DateTimeField()
    update_time = models.DateTimeField()
