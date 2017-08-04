from math import floor

import datetime

from django.db.models import Sum

from current_rest import models, constants

INTEREST_RATE_STEPS = [
    0.050,  # balance < 1000
    0.052,  # balance < 5000
    0.055,  # balance < 10000
    0.059,  # balance < 50000
    0.064,  # balance < 100000
    0.069,  # balance < 300000
    0.075,  # balance < 500000
]

YEAR_OF_DAYS = 365


def calculate_interest(balance):
    rate = pickup_rate_by_balance(balance)
    return int(floor(balance * rate / YEAR_OF_DAYS))


def pickup_rate_by_balance(balance):
    if balance < 100000:
        step = 0
    elif balance < 500000:
        step = 1
    elif balance < 1000000:
        step = 2
    elif balance < 5000000:
        step = 3
    elif balance < 10000000:
        step = 4
    elif balance < 30000000:
        step = 5
    else:
        step = 6
    return INTEREST_RATE_STEPS[step]
