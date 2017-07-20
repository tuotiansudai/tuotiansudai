from django.conf.urls import url

from current_rest import views
from current_rest.views import deposit
from current_rest.views import loan
from current_rest.views.loan import LoanViewSet

post_loan = LoanViewSet.as_view({'post': 'create'})
get_loan = LoanViewSet.as_view({'get': 'retrieve'})
audit_loan = LoanViewSet.as_view({'put': 'update'})

urlpatterns = [
    url(r'^loan$', post_loan, name='post_deposit'),
    url(r'^audit-loan/(?P<pk>[0-9]+)$', audit_loan, name='audit_loan', kwargs={'partial': True, 'audit': True}),
    url(r'^loan/(?P<pk>[0-9]+)$', get_loan, name="get_loan"),
    url('^deposit-with-password$', deposit.deposit_with_password, name='deposit_with_password'),
    url('^deposit-with-no-password$', deposit.deposit_with_no_password, name='deposit_with_no_password'),
    url('^deposit-callback$', deposit.deposit_callback, name='deposit_callback'),
]
