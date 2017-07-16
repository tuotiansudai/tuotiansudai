from django.conf.urls import url

from current_rest.views import deposit
from current_rest.views import withdraw

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
    url('^withdraw/create$', withdraw.withdraw_create,
        name='withdraw_create'),
    url('^withdraw/(?P<pk>[0-9]+)$', withdraw.withdraw_get_by_id,
        name='withdraw_detail'),
    url('^withdraw_all', withdraw.withraw_get_all,
        name='withdraw_list'),
]
