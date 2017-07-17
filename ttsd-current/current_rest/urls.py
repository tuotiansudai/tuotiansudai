from django.conf.urls import url

from current_rest.views import deposit
from current_rest.views import redeem

urlpatterns = [
    url('^user/(?P<login_name>[A-Za-z0-9]{6,25})/deposit-with-password$',
        deposit.deposit_with_password,
        name='deposit_with_password'),
    url('^user/(?P<login_name>[A-Za-z0-9]{6,25})/deposit-with-no-password$',
        deposit.deposit_with_no_password,
        name='deposit_with_no_password'),
    url('^deposit-callback',
        deposit.deposit_with_password_callback,
        name='deposit_with_password_callback'),

    url('^user/(?P<login_name>[A-Za-z0-9]{6,25})/redeem$', redeem.redeem,
        name='redeem_create'),
    url('^user/redeem/(?P<pk>[0-9]+)$', redeem.redeem_get_by_id,
        name='redeem_detail'),
    url('^user/redeem_all', redeem.redeem_get_all,
        name='redeem_list'),
    url('^user/(?P<pk>[0-9]+)/(?P<st>[A-Z]+)/redeem_audit$', redeem.redeem_audit,
        name='redeem_audit'),
]
