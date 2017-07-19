from django.conf.urls import url

from current_rest.views.deposit import DepositViewSet

post_deposit = DepositViewSet.as_view({'post': 'create'})
get_put_deposit = DepositViewSet.as_view({'get': 'retrieve', 'put': 'update'})

urlpatterns = [
    url(r'^deposit$', post_deposit, name='post_deposit'),
    url(r'^deposit/(?P<pk>[0-9]+)$', get_put_deposit, name="get_put_deposit", kwargs={'partial': True}),

    # url('^deposit/(?P<pk>\d+)$', deposit.get_deposit, name='deposit_detail'),
    # url('^user/(?P<login_name>[A-Za-z0-9_]{6,25})/personal-max-deposit$', deposit.personal_max_deposit,
    #     name='personal_max_deposit'),
    # url('^deposit-with-password$', deposit.deposit_with_password, name='deposit_with_password'),
    # url('^deposit-with-no-password$', deposit.deposit_with_no_password, name='deposit_with_no_password'),
    # url('^deposit-callback$', deposit.deposit_callback, name='deposit_callback'),
]
