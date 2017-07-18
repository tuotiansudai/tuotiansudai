from django.conf.urls import url

from current_rest import views
from current_rest.views import deposit
from current_rest.views import loan

urlpatterns = [
    url(r'^post-loan$', loan.post_loan, name="post-loan"),
    url(r'^audit-loan$', loan.audit_loan, name="audit-loan"),
    url(r'^get-loan$', loan.get_loan, name="get-loan"),
    url('^deposit-with-password$', deposit.deposit_with_password, name='deposit_with_password'),
    url('^deposit-with-no-password$', deposit.deposit_with_no_password, name='deposit_with_no_password'),
    url('^deposit-callback$', deposit.deposit_callback, name='deposit_callback'),
]
