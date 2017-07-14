# coding=utf-8
import json
from datetime import datetime

import requests
from django.shortcuts import render
from rest_framework import status

import settings
from current_console import rest_client
from current_console.rest_client import RestClient
from current_console.views.home import handler404
from current_rest import constants


def create_loan(request):
    if request.method == 'POST':
        form = request.POST.dict()
        form['serial_number'] = 1
        form['status'] = constants.LOAN_STATUS_APPROVING
        response = RestClient('loans').execute(data=form, method='POST')
        if response is None:
            return render(request, 'console/loan/loan.html', status=status.HTTP_400_BAD_REQUEST)

        return render(request, 'console/loan/loan.html')

    return render(request, 'console/loan/loan.html', {'types': constants.LOAN_TYPE_CHOICES})


def audit_loan(request):
    if request.method == 'PUT':
        loan = RestClient('loans').execute(params={'pk': request.GET.get('pk')}, method='GET')
        if loan is None:
            return handler404

        loan['status'] = constants.LOAN_STATUS_APPROVED
        loan['update_time'] = datetime.now()

        response = RestClient('loans').execute(data=loan, method='PUT')

        if response is None:
            return render(request, 'console/loan/loan.html', status=status.HTTP_400_BAD_REQUEST)

        return render(request, 'console/loan/loan.html', status=status.HTTP_200_OK)

    return handler404
