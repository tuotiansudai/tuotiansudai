from django.conf.urls import url

from current_rest.views import deposit
from current_rest.views import redeem

urlpatterns = [

    url('^redeem/create$', redeem.redeem, name='redeem_create'),
    url('^redeem/([A-Za-z0-9]{6,25})/limits$', redeem.limits, name='redeem_limits'),

    url('^deposit-with-password$', deposit.deposit_with_password, name='deposit_with_password'),
    url('^deposit-with-no-password$', deposit.deposit_with_no_password, name='deposit_with_no_password'),
    url('^deposit-callback$', deposit.deposit_callback, name='deposit_callback'),
]
