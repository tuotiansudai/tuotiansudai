# coding=utf-8
import json

import requests
from django.shortcuts import render

import settings
from current_rest import constants

CREATE_LOAN_REST_URL = u'http://{host}:{port}/{applicationContext}loans'


def create_loan(request):
    if request.method == 'POST':
        url = CREATE_LOAN_REST_URL \
            .replace('{host}', settings.REST_SERVICE_HOST) \
            .replace('{port}', settings.REST_SERVICE_PORT) \
            .replace('{applicationContext}', settings.REST_PATH)
        form = request.POST.dict()
        form['serial_number'] = 1
        form['status'] = constants.LOAN_STATUS_APPROVING
        response = requests.post(url, data=form)

        return render(request, 'console/loan/loan.html')

    return render(request, 'console/loan/loan.html', {'types': constants.LOAN_TYPE_CHOICES})
