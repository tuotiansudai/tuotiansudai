# -*- coding: utf-8 -*-
# Generated by Django 1.10.1 on 2017-07-12 09:59
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('current_rest', '0002_auto_20170712_1753'),
    ]

    operations = [
        migrations.AlterField(
            model_name='loan',
            name='debtor_identity_card',
            field=models.CharField(max_length=30),
        ),
    ]
