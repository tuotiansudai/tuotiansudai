from django.conf.urls import url

from current_rest import views
from current_rest.views import deposit
from current_rest.views import loan
from current_rest.views.loan import LoanViewSet

post_loan = LoanViewSet.as_view({'post': 'create'})

urlpatterns = [
    url(r'^loan$', post_loan, name='post_deposit'),
    url('^deposit-with-password$', deposit.deposit_with_password, name='deposit_with_password'),
    url('^deposit-with-no-password$', deposit.deposit_with_no_password, name='deposit_with_no_password'),
    url('^deposit-callback$', deposit.deposit_callback, name='deposit_callback'),
]
