INTEREST_RATE_STEPS = [
    0.050,  # balance < 1000
    0.052,  # balance < 5000
    0.055,  # balance < 10000
    0.059,  # balance < 50000
    0.064,  # balance < 100000
    0.069,  # balance < 300000
    0.075,  # balance < 500000
]


def calculate_interest(balance):
    rate = pickup_rate_by_balance(balance)
    return balance * rate


def pickup_rate_by_balance(balance):
    if balance < 1000:
        step = 0
    elif balance < 5000:
        step = 1
    elif balance < 10000:
        step = 2
    elif balance < 50000:
        step = 3
    elif balance < 100000:
        step = 4
    elif balance < 300000:
        step = 5
    else:
        step = 6
    return INTEREST_RATE_STEPS[step]
