# coding=utf-8
from collections import OrderedDict

from django.shortcuts import render
from django.views.decorators.http import require_http_methods

from common import constants as common_constants
from current_console.decorators import user_roles_check
from current_console.forms import DepositForm
from current_console.rest_client import RestClient


@require_http_methods(["GET"])
@user_roles_check(['ADMIN', 'OPERATOR', 'OPERATOR_ADMIN'])
def deposit_list(request):
    form = DepositForm(request.GET)

    context = {
        'status_choice': OrderedDict(common_constants.DepositStatusType.DEPOSIT_STATUS_CHOICE),
        'source_choice': OrderedDict(common_constants.SourceType.SOURCE_CHOICE)
    }
    if form.is_valid():
        response_data = RestClient('deposit-list').get(params=form.cleaned_data)
        if response_data:
            context['data'] = response_data
            return render(request=request,
                          template_name='console/deposit/list.html',
                          context=context)
    else:
        return render(request, 'console/deposit/list.html', context=context)
