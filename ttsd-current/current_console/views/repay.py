# coding=utf-8
from datetime import datetime
from django.http import HttpResponseRedirect
from django.http import JsonResponse
from django.shortcuts import render
from django.urls import reverse
from django.views.decorators.http import require_http_methods
from rest_framework import status
from rest_framework.response import Response

from current_console import constants
from current_console.decorators import user_roles_check
from current_console.forms import LoanRepayForm
from current_console.rest_client import RestClient


@require_http_methods(["POST"])
@user_roles_check(['ADMIN', 'OPERATOR', 'OPERATOR_ADMIN'])
def submit_loan_repay(request):
    form = LoanRepayForm(request.POST)
    if form.is_valid():
        request_dict = form.cleaned_data
        request_dict['submit_name'] = request.session['login_name']
        request_dict['status'] = constants.REPAY_STATUS_WAITING
        request_dict['repay_amount'] = request_dict['repay_amount']*1000000
        status_code, response = RestClient('submit-loan-repay').post_status_and_response(data=request_dict)

        if status_code == 201:
            return JsonResponse({'message': response['message']}, status=status.HTTP_200_OK)
        elif status_code == 400:
            return JsonResponse({'message': response['message']}, status=status.HTTP_400_BAD_REQUEST)

        return JsonResponse({'message', '內部服务器错误'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    else:
        return JsonResponse({'message': form.errors['repay_amount']}, status=status.HTTP_400_BAD_REQUEST)


@require_http_methods(["GET"])
@user_roles_check(['ADMIN', 'OPERATOR', 'OPERATOR_ADMIN'])
def loan_repay_retrieve(request, pk, task):
    loan_repay = RestClient('loan-repay-retrieve/{}'.format(pk)).get()
    loan_repay['loan']['amount'] = '{0:.2f}'.format(loan_repay['loan']['amount']/1000000.0)
    loan_repay['repay_amount'] = '{0:.2f}'.format(loan_repay['repay_amount']/1000000.0)
    if loan_repay:
        return render(request, 'console/repay/audit_repay.html',
                      {'loanRepay': loan_repay,
                       'types': constants.LOAN_TYPE_CHOICES,
                       'task_id': task})
    return Response(status=status.HTTP_400_BAD_REQUEST)


@require_http_methods(["GET"])
@user_roles_check(['ADMIN', 'OPERATOR', 'OPERATOR_ADMIN'])
def loan_repay_record(request):
    response = RestClient('loan-repay-record').get(params=request.GET)
    return render(request, 'console/repay/repay_record.html',
                  {'repayResult': response,
                   'status': constants.REPAY_STATUS_CHOICES})


@require_http_methods(["PUT"])
@user_roles_check(['ADMIN', 'OPERATOR', 'OPERATOR_ADMIN'])
def audit_loan_repay(request, pk, result):

    data = {
        'auditor': request.session['login_name'],
        'loan_id': eval(request.body)['loan_id'],
        'task_id': eval(request.body)['task_id'],
    }

    status_code, response = RestClient('audit-loan-repay/{}/{}'.format(pk, result)).put_status_and_response(data=data)

    if status_code == 201:
        return JsonResponse({'message': response['message']}, status=status.HTTP_200_OK)
    elif status_code == 400:
        return JsonResponse({'message': response['message']}, status=status.HTTP_400_BAD_REQUEST)
    else:
        return JsonResponse({'message', '內部服务器错误'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)