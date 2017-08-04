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
        request_dict = form.data.dict()
        request_dict['submit_name'] = request.session['login_name']
        # request_dict['updated_date'] = datetime.now()
        request_dict['status'] = constants.REPAY_STATUS_WAITING
        response = RestClient('submit-repay').post(data=request_dict)
        return render(request,'console/loan/approved_loan_list.html')


@require_http_methods(["POST"])
@user_roles_check(['ADMIN', 'OPERATOR', 'OPERATOR_ADMIN'])
def audit_loan_repay(request, pk, result):
    return render(request,'console/loan/approved_loan_list.html')