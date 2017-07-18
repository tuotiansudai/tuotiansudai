# coding=utf-8
from datetime import datetime

from django.shortcuts import render
from rest_framework import status

from current_console.forms import LoanForm
from current_console.rest_client import RestClient
from current_console.views.home import handler404
from current_rest import constants


def create_loan(request):
    if request.method == 'POST':
        form = LoanForm(request.POST)
        if form.is_valid():
            loan_dict = form.data.dict()
            loan_dict['serial_number'] = 1
            loan_dict['status'] = constants.LOAN_STATUS_APPROVING
            loan_dict['creator'] = 'creator'
            response = RestClient('post-loan').execute(data=loan_dict, method='POST')
            if response is None:
                return render(request, 'console/loan/loan.html', {'form': form, 'types': constants.LOAN_TYPE_CHOICES}, )
            return render(request, 'console/loan/list.html')
        else:
            return render(request, 'console/loan/loan.html', {'form': form, 'types': constants.LOAN_TYPE_CHOICES})

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
