# coding=utf-8
from datetime import datetime

from django.shortcuts import render
from django.views.decorators.http import require_http_methods
from rest_framework import status
from rest_framework.response import Response

from current_console.forms import LoanForm
from current_console.rest_client import RestClient
from current_console.views.home import handler404
from current_rest import constants


@require_http_methods(["GET"])
def show_loan(request):
    return render(request, 'console/loan/loan.html', {'types': constants.LOAN_TYPE_CHOICES})


@require_http_methods(["POST"])
def create_loan(request):
    form = LoanForm(request.POST)
    if form.is_valid():
        loan_dict = form.data.dict()
        loan_dict['serial_number'] = 1
        loan_dict['status'] = constants.LOAN_STATUS_APPROVING
        loan_dict['creator'] = 'creator'
        response = RestClient('loan').execute('POST', data=loan_dict)
        if response:
            return render(request, 'console/loan/list.html')
    else:
        return render(request, 'console/loan/loan.html', {'form': form, 'types': constants.LOAN_TYPE_CHOICES})


@require_http_methods(["PUT"])
def audit_loan(request):
    loan = RestClient('loan/{}'.format(request.GET.get('id'))).execute('GET')

    if loan is None:
        return Response(status=status.HTTP_400_BAD_REQUEST)

    loan['status'] = constants.LOAN_STATUS_APPROVED
    loan['update_time'] = datetime.now()
    loan['auditor'] = 'auditor'

    response = RestClient('audit-loan/{}'.format(request.GET.get('id'))).execute('PUT', data=loan)

    if response:
        return Response(status=status.HTTP_200_OK)

    return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)
