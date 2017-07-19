# -*- coding: utf-8 -*-

import logging

from current_rest import redis_client


class CurrentDailyManager(object):
    daily_amount_limit_key = 'current:daily:amount'

    def get_current_daily_amount(self):
        daily_amount = int(redis_client.get(self.daily_amount_limit_key))
        return daily_amount if daily_amount else 0
