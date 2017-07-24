"""huizu URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.10/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url

from current_console.views import home, loan, fund

urlpatterns = [
    url('^index$', home.index, name='index'),
    url('^show-loan$', loan.show_loan, name='show_loan'),
    url('^create-loan$', loan.create_loan, name='create_loan'),
    url('^audit-loan$', loan.audit_loan, name='audit_loan'),
    url('^fund/setting$', fund.FundSettingView.as_view(), name='fund_setting'),
    url('^fund/setting-approve$', fund.FundSettingApproveView.as_view(), name='fund_setting_approve'),
    url('^fund/setting-history$', fund.fund_setting_history_page, name='fund_setting_history_page'),
    url('^fund/setting-history-query$', fund.fund_setting_history_query, name='fund_setting_history_query'),
]
