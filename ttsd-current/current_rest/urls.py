"""current URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
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

from current_rest.views import fund
from current_rest.views.account import AccountViewSet
from current_rest.views.deposit import DepositViewSet
from current_rest.views.loan import LoanViewSet
from current_rest.views.redeem import RedeemViewSet

post_deposit = DepositViewSet.as_view({'post': 'create'})
get_put_deposit = DepositViewSet.as_view({'get': 'retrieve', 'put': 'update'})
get_account = AccountViewSet.as_view({'get': 'retrieve'})
post_loan = LoanViewSet.as_view({'post': 'create'})
get_loan = LoanViewSet.as_view({'get': 'retrieve'})
audit_loan = LoanViewSet.as_view({'put': 'update'})

redeem = RedeemViewSet.as_view({'post': 'create'})

urlpatterns = [
    url(r'^loan$', post_loan, name='post_loan'),
    url(r'^audit-loan/(?P<pk>[0-9]+)$', audit_loan, name='audit_loan', kwargs={'partial': True, 'audit': True}),
    url(r'^loan/(?P<pk>[0-9]+)$', get_loan, name="get_loan"),
    url(r'^deposit$', post_deposit, name='post_deposit'),
    url(r'^deposit/(?P<pk>[0-9]+)$', get_put_deposit, name="get_put_deposit", kwargs={'partial': True}),
    url(r'^account/(?P<login_name>[A-Za-z0-9_]{6,25})$', get_account, name="get_account"),
    url(r'^redeem/create$', redeem, name='post_redeem'),
    url(r'^fund-info/tendency$', fund.tendency, name="fund_info_tendency"),
    url(r'^fund-info/history$', fund.history, name="fund_info_history"),
    url(r'^fund-info/today$', fund.TodayFundSettingViewSet.as_view(), name="fund_info_today"),
]
