# -*- coding: utf-8 -*-
# Generated by Django 1.11.3 on 2017-08-03 19:06
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('current_rest', '0004_auto_20170730_1508'),
    ]

    operations = [
        migrations.CreateModel(
            name='LoanRepay',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('created_time', models.DateTimeField(auto_now_add=True)),
                ('updated_time', models.DateTimeField(auto_now=True)),
                ('approver', models.CharField(max_length=25, null=True)),
                ('approved_time', models.DateTimeField(blank=True, null=True)),
                ('repay_amount', models.PositiveIntegerField(null=False)),
                ('submit_name', models.CharField(max_length=25, null=False)),
                ('status', models.CharField(choices=[(b'WAITING', '\u5f85\u5ba1\u6838'), (b'APPROVED', '\u5df2\u5ba1\u6838'), (b'DENIED', '\u5df2\u9a73\u56de')], max_length=20)),
                ('loan', models.ForeignKey(null=False, on_delete=django.db.models.deletion.PROTECT, related_name='+', to='current_rest.Loan')),
            ],
            options={
                'db_table': 'loan_repay',
            },
        ),
    ]
