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
from current_rest.views import loan_matching
from current_rest.views.account import AccountViewSet
from current_rest.views.deposit import DepositViewSet
from current_rest.views.loan import LoanListViewSet, audit_reject_loan
from current_rest.views.loan import LoanViewSet, ApprovedLoanListViewSet
from current_rest.views.repay import LoanRepayViewSet, audit_loan_repay
from current_rest.views.task import TaskViewSet
from current_rest.views.redeem import RedeemViewSet, audit_redeem_pass, audit_redeem_reject

post_deposit = DepositViewSet.as_view({'post': 'create'})
get_put_deposit = DepositViewSet.as_view({'get': 'retrieve', 'put': 'update'})
get_account = AccountViewSet.as_view({'get': 'retrieve'})
calculate_interest_yesterday = AccountViewSet.as_view({'post': 'calculate_interest_yesterday'})
post_loan = LoanViewSet.as_view({'post': 'create'})
get_loan = LoanViewSet.as_view({'get': 'retrieve'})
audit_loan = LoanViewSet.as_view({'put': 'update'})
audit_reject_loan = LoanListViewSet.as_view({'put', 'audit_reject_loan'})
get_edit_loan = LoanViewSet.as_view({'get': 'retrieve', 'put': 'update'})
loan_list = LoanListViewSet.as_view({'get': 'list'})

redeem_list = RedeemViewSet.as_view({'get': 'list'})
redeem = RedeemViewSet.as_view({'post': 'create', 'get': 'retrieve', 'put': 'update'})

approves_loan_list = ApprovedLoanListViewSet.as_view({'get': 'list'})
post_loan_repay = LoanRepayViewSet.as_view({'post': 'create'})
loan_repay_record = LoanRepayViewSet.as_view({'get': 'list'})
get_put_loan_repay = LoanRepayViewSet.as_view({'get': 'retrieve', 'put': 'update'})
task_list = TaskViewSet.as_view({'get': 'list'})

urlpatterns = [
    url(r'^loan$', post_loan, name='post_loan'),
    url(r'^audit-loan/(?P<pk>[0-9]+)$', audit_loan, name='audit_loan', kwargs={'partial': True, 'audit': True}),
    url(r'^loan/(?P<pk>[0-9]+)$', get_loan, name="get_loan"),
    url(r'^audit-reject-loan/(?P<pk>[0-9]+)/(?P<category>(audit|reject))$', audit_reject_loan,
        name='audit_reject_loan'),
    url(r'^loan/(?P<pk>[0-9]+)$', get_edit_loan, name="get_edit_loan", kwargs={'partial': True}),
    url('^loan-list$', loan_list, name='loan_list'),
    url(r'^deposit$', post_deposit, name='post_deposit'),
    url(r'^deposit/(?P<pk>[0-9]+)$', get_put_deposit, name="get_put_deposit", kwargs={'partial': True}),
    url(r'^account/(?P<login_name>[A-Za-z0-9_]{6,25})$', get_account, name="get_account"),
    url(r'^redeem$', redeem, name='post_redeem'),
    url(r'^redeem/(?P<pk>[0-9]+)$', redeem, name='get_put_redeem', kwargs={'partial': True}),
    url(r'^redeem-audit/(?P<pk>[0-9]+)/pass$', audit_redeem_pass, name='audit_redeem_pass'),
    url(r'^redeem-audit/(?P<pk>[0-9]+)/reject$', audit_redeem_reject, name='audit_redeem_reject'),
    url(r'^redeem-list', redeem_list, name='redeem_list'),
    url(r'^account/calculate_interest_yesterday$', calculate_interest_yesterday, name="calculate_interest_yesterday"),
    url(r'^fund-info/tendency$', fund.tendency, name="fund_info_tendency"),
    url(r'^fund-info/history$', fund.history, name="fund_info_history"),
    url(r'^fund-info/today$', fund.TodayFundSettingViewSet.as_view(), name="fund_info_today"),
    url(r'^loan-matching$', loan_matching.loan_matching, name="loan_matching"),
    url(r'^approved-loan-list$', approves_loan_list, name="approved_loan_list"),
    url(r'^submit-loan-repay$', post_loan_repay, name='submit_loan_repay'),
    url(r'^loan-repay-record', loan_repay_record, name='loan_repay_record'),
    url(r'^loan-repay-retrieve/(?P<pk>[0-9]+)$', get_put_loan_repay, name='loan_repay_retrieve', kwargs={'partial': True}),
    url(r'^audit-loan-repay/(?P<pk>[0-9]+)/(?P<result>(pass|reject))$', audit_loan_repay, name='audit_loan_repay'),
    url(r'^task$', task_list, name='task_list'),
    url(r'^fund/distribution$', fund.distribution, name="fund_distribution"),
]
