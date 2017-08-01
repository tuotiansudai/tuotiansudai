# -*- coding: utf-8 -*-

import logging
from datetime import datetime, timedelta

from django.db.models import Sum

from current_rest import redis_client, models, constants

logger = logging.getLogger(__name__)


class CurrentDailyManager(object):
    daily_amount_limit_key = 'current:daily:amount'

    def get_current_daily_amount(self):
        daily_amount = int(redis_client.get(self.daily_amount_limit_key))
        return daily_amount if daily_amount else 0


def calculate_success_deposit_today():
    today = datetime.now().date()
    tomorrow = today + timedelta(1)
    amount_sum = models.CurrentDeposit.objects.filter(status=constants.DEPOSIT_SUCCESS,
                                                      updated_time__range=(today, tomorrow)) \
        .all().aggregate(Sum('amount')) \
        .get('amount__sum', 0)
    return amount_sum if amount_sum else 0
