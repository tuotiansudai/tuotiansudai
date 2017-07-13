# coding=utf-8
import requests
from django.shortcuts import render

from current_console.forms import LoanForm
from current_rest import constants


def create_loan(request):
    if request.method == 'POST':
        # form = LoanForm(date=request.POST)
        # if form.is_valid():
        requests.post()

        return render(request, 'console/loan/loan.html')

    return render(request, 'console/loan/loan.html', {'types': constants.LOAN_TYPE_CHOICES})
