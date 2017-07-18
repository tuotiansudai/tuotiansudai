from django.conf.urls import url

from current_rest.views import deposit
from current_rest.views import redeem

urlpatterns = [

    url('^redeem/create$', redeem.redeem, name='redeem_create'),
    url('^redeem/(?P<pk>[0-9]+)$', redeem.redeem_get_by_id, name='redeem_detail'),
    url('^redeem/list$', redeem.redeem_get_all, name='redeem_list'),
    url('^redeem_audit/(?P<pk>[0-9]+)/(?P<st>[A-Z]+)$', redeem.redeem_audit, name='redeem_audit'),

    url('^deposit-with-password$', deposit.deposit_with_password, name='deposit_with_password'),
    url('^deposit-with-no-password$', deposit.deposit_with_no_password, name='deposit_with_no_password'),
    url('^deposit-callback$', deposit.deposit_callback, name='deposit_callback'),
]
