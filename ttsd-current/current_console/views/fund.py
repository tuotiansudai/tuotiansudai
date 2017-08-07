# coding=utf-8
import json
from datetime import datetime, timedelta

from django.http import JsonResponse, Http404, HttpResponseBadRequest
from django.shortcuts import render, redirect
from django.urls import reverse
from django.views import View

from current_console.forms import FundSettingHistoryQueryForm, FundDistributionQueryForm
from current_console.rest_client import RestClient


class FundSettingView(View):
    def _load_view_data(self):
        tendency = RestClient('fund-info/tendency').get()
        view_data = {'tendency': json.dumps(tendency)}

        today_fund_info = RestClient('fund-info/today').get()
        if today_fund_info:
            config_quota_status = today_fund_info['config_quota_status']
            is_configured = config_quota_status in ('APPROVING', 'APPROVED')
            quota_amount = today_fund_info['config_quota_amount'] if is_configured else today_fund_info['quota_amount']

            view_data['has_error'] = False
            view_data['quota_amount'] = quota_amount
            view_data['loan_remain_amount'] = today_fund_info['loan_remain_amount']
            view_data['allow_change_quota'] = today_fund_info['allow_change_quota']
            view_data['approved'] = today_fund_info['config_quota_status'] == 'APPROVED'
            view_data['approving'] = today_fund_info['config_quota_status'] == 'APPROVING'
        else:
            view_data['has_error'] = True
            view_data['error_message'] = u'昨日资金尚未清算完成'
        return view_data

    def get(self, request):
        view_data = self._load_view_data()
        return render(request, 'console/fund/setting.html', view_data)

    def post(self, request):
        quota_setting_data = {
            'config_quota_amount': request.POST.get('config_quota_amount'),
            'config_quota_status': 'APPROVING',
        }
        today_fund_info = RestClient('fund-info/today').put(quota_setting_data)
        if today_fund_info:
            return redirect(reverse('fund_setting'))
        else:
            return self.get()


class FundSettingApproveView(FundSettingView):
    def get(self, request):
        view_data = self._load_view_data()
        view_data['is_approve_page'] = True
        return render(request, 'console/fund/setting.html', view_data)

    def post(self, request):
        put_data = {
            'config_quota_status': request.POST.get('config_quota_status'),
            'config_quota_amount': request.POST.get('config_quota_amount')
        }
        today_fund_info = RestClient('fund-info/today').put(put_data)
        if today_fund_info:
            return redirect(reverse('fund_setting'))
        else:
            return self.get(request)


def fund_setting_history_page(request):
    end_date = datetime.now().today()
    begin_date = end_date - timedelta(days=7)
    return render(request, 'console/fund/setting_history.html', {'begin_date': begin_date, 'end_date': end_date})


def fund_setting_history_query(request):
    query_form = FundSettingHistoryQueryForm(request.GET)
    if query_form.is_valid():
        url = 'fund-info/history?begin_date={}&end_date={}'.format(query_form.data['begin_date'],
                                                                   query_form.data['end_date'])
        histories = RestClient(url).get()
        return JsonResponse(histories)
    else:
        return HttpResponseBadRequest(u'日期参数格式有误，请使用 Y-m-d 格式')


def fund_distribution_page(request):
    granularity = 'Daily'
    end_date = datetime.now().date()
    begin_date = end_date - timedelta(days=7)
    return render(request, 'console/fund/distribution.html',
                  {'granularity': granularity, 'begin_date': begin_date, 'end_date': end_date})


def fund_distribution_query(request):
    query_form = FundDistributionQueryForm(request.GET)
    if query_form.is_valid():
        url = 'fund/distribution?granularity={}&begin_date={}&end_date={}'.format(
            query_form.data['granularity'], query_form.data['begin_date'],
            query_form.data['end_date'])
        data_distribution = RestClient(url).get()
        return JsonResponse(data_distribution)
    else:
        return HttpResponseBadRequest(u'日期参数格式有误，请使用 Y-m-d 格式')
