from django.conf.urls import url

from current_rest.views import deposit

urlpatterns = [
    url('^login-name/(?P<login_name>[A-Za-z0-9]{6,25})/deposit-with-password$',
        deposit.deposit_with_password,
        name='deposit_with_password'),

    url('^deposit-with-password-callback',
        deposit.deposit_with_password_callback,
        name='deposit_with_password_callback'),
]
