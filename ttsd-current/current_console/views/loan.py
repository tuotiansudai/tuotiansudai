# coding=utf-8
from datetime import datetime

from django.shortcuts import render
from rest_framework import status

from current_console.rest_client import RestClient
from current_console.views.home import handler404
from current_rest import constants


def create_loan(request):
    if request.method == 'POST':
        form = request.POST.dict()
        form['serial_number'] = 1
        form['status'] = constants.LOAN_STATUS_APPROVING
        form['creator'] = 'creator'
        response = RestClient('post-loan').execute(data=form, method='POST')
        if response is None:
            return render(request, 'console/loan/loan.html', status=status.HTTP_400_BAD_REQUEST)

        return render(request, 'console/loan/loan-list.html')

    return render(request, 'console/loan/loan.html', {'types': constants.LOAN_TYPE_CHOICES})


def audit_loan(request):
    if request.method == 'PUT':
        params = {'id': request.GET.get('id')}
        loan = RestClient('get-loan').execute(params=params, method='GET')

        if loan is None:
            return handler404

        loan['status'] = constants.LOAN_STATUS_APPROVED
        loan['update_time'] = datetime.now()
        loan['auditor'] = 'auditor'

        response = RestClient('audit-loan').execute(data=loan, params=params, method='PUT')

        if response is None:
            return render(request, 'console/loan/loan.html', status=status.HTTP_400_BAD_REQUEST)

        return render(request, 'console/loan/loan.html', status=status.HTTP_200_OK)

    return handler404
