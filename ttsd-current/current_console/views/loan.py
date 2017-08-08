# coding=utf-8
import datetime
from django.http import HttpResponseRedirect
from django.http import JsonResponse
from django.shortcuts import render
from django.urls import reverse
from django.views.decorators.http import require_http_methods
from rest_framework import status
from rest_framework.response import Response
from current_console import constants
from current_console.decorators import user_roles_check
from current_console.forms import LoanForm
from current_console.forms import LoanForm, ApprovedLoanForm
from current_console.rest_client import RestClient


@require_http_methods(["GET"])
@user_roles_check(['ADMIN', 'OPERATOR', 'OPERATOR_ADMIN'])
def show_loan(request):
    return render(request, 'console/loan/loan.html', {'types': constants.LOAN_TYPE_CHOICES})


@require_http_methods(["POST"])
@user_roles_check(['ADMIN', 'OPERATOR', 'OPERATOR_ADMIN'])
def create_loan(request):
    form = LoanForm(request.POST)
    if form.is_valid():
        loan_dict = form.data.dict()
        loan_dict['serial_number'] = 1
        loan_dict['status'] = constants.LOAN_STATUS_APPROVING
        loan_dict['creator'] = 'creator'
        response = RestClient('loan').post(data=loan_dict)
        if response:
            return render(request, 'console/loan/list.html')
    else:
        return render(request, 'console/loan/loan.html', {'form': form, 'types': constants.LOAN_TYPE_CHOICES})


@require_http_methods(["PUT"])
@user_roles_check(['ADMIN', 'OPERATOR_ADMIN'])
def audit_reject_loan(request, category, pk):
    data = {
        'status': constants.LOAN_STATUS_APPROVED if category == 'audit' else constants.LOAN_STATUS_REJECT,
        'auditor': 'auditor'
    }

    response = RestClient('audit-reject-loan/{}/{}'.format(pk, category)).put(data=data)

    if response:
        return JsonResponse({'message': response[1]}, status=status.HTTP_200_OK)

    return JsonResponse({'message', '內部服务器错误'}, status=status.HTTP_200_OK)


@require_http_methods(["GET"])
@user_roles_check(['ADMIN', 'OPERATOR', 'OPERATOR_ADMIN'])
def loan_list(request):
    return render(request, 'console/loan/list.html',
                  {'loans': RestClient('loan-list').get(),
                   'statuses': constants.LOAN_STATUS_CHOICES,
                   'types': constants.LOAN_TYPE_CHOICES,
                   })


@require_http_methods(["GET"])
@user_roles_check(['ADMIN', 'OPERATOR', 'OPERATOR_ADMIN'])
def show_edit_loan(request, pk):
    loan = RestClient('loan/{}'.format(pk)).get()

    if loan is None:
        return Response(status=status.HTTP_400_BAD_REQUEST)

    return render(request, 'console/loan/edit.html', {'loan': loan, 'types': constants.LOAN_TYPE_CHOICES})


@require_http_methods(["POST"])
@user_roles_check(['ADMIN', 'OPERATOR', 'OPERATOR_ADMIN'])
def edit_loan(request, pk):
    form = LoanForm(request.POST)
    if form.is_valid():
        loan = RestClient('loan/{}'.format(pk)).get()

        if loan is None:
            return Response(status=status.HTTP_400_BAD_REQUEST)

        loan = _modify_loan(loan, form)
        response = RestClient('loan/{}'.format(pk)).put(data=loan)
        if response:
            return HttpResponseRedirect(reverse('loan_list'))

    return render(request, 'console/loan/edit.html', {'form': form, 'types': constants.LOAN_TYPE_CHOICES})


def _modify_loan(old_loan, form):
    old_loan['amount'] = form.cleaned_data['amount']
    old_loan['loan_type'] = form.cleaned_data['loan_type']
    old_loan['debtor'] = form.cleaned_data['debtor']
    old_loan['debtor_identity_card'] = form.cleaned_data['debtor_identity_card']
    old_loan['effective_date'] = form.cleaned_data['effective_date']
    old_loan['expiration_date'] = form.cleaned_data['expiration_date']
    old_loan['status'] = constants.LOAN_STATUS_APPROVING

    return old_loan


@require_http_methods(["GET"])
@user_roles_check(['ADMIN', 'OPERATOR', 'OPERATOR_ADMIN'])
def approved_loan_list(request):
    form = ApprovedLoanForm(request.GET)
    if form.is_valid():
        request_dict = form.cleaned_data
        response = RestClient('approved-loan-list').get(params=request_dict)
        if response['results']:
            for list in response['results']:
                if list['loan_matching_status'] == constants.LOAN_MATCHING_STATUS_DOING:
                    list['balance'] = list['amount']-list['loan_matching_amount']['amount__sum']
                elif list['loan_matching_status'] == constants.LOAN_MATCHING_STATUS_WAITING:
                    list['balance'] = list['amount']
                else:
                    list['balance'] = 0

        return render(request, 'console/loan/approved_loan_list.html',
                      {'approved_loan_list': response,
                       'types': constants.LOAN_TYPE_CHOICES,
                       'matching_status': constants.LOAN_MATCHING_STATUS_CHOICES})
    else:
        return render(request, 'console/loan/approved_loan_list.html',
                      {'types': constants.LOAN_TYPE_CHOICES,
                       'matching_status': constants.LOAN_MATCHING_STATUS_CHOICES,
                       'form': form})


@require_http_methods(["GET"])
@user_roles_check(['ADMIN', 'OPERATOR', 'OPERATOR_ADMIN'])
def query_loan_by_id(request):
    loan = RestClient('loan/{}'.format(request.GET['loan_id'])).get()
    return render(request, 'console/repay/repay.html',
                  {'loan': loan,
                   'loan_type': constants.LOAN_TYPE_CHOICES,
                   'repay_time': datetime.datetime.now()})
