from django.conf.urls import url

from current_rest.views import deposit

urlpatterns = [
    url('^deposit/(?P<pk>\d+)$', deposit.get_deposit, name='deposit_detail'),
    url('^user/(?P<login_name>[A-Za-z0-9_]{6,25})/personal-max-deposit$', deposit.personal_max_deposit,
        name='personal_max_deposit'),
    url('^deposit-with-password$', deposit.deposit_with_password, name='deposit_with_password'),
    url('^deposit-with-no-password$', deposit.deposit_with_no_password, name='deposit_with_no_password'),
    url('^deposit-callback$', deposit.deposit_callback, name='deposit_callback'),
]
