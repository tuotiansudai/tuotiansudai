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

    def __unicode__(self):
        return self.approver
