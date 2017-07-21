from django.conf.urls import url
from current_rest.views.account import AccountViewSet
from current_rest.views.deposit import DepositViewSet
from current_rest.views.redeem import RedeemViewSet

post_deposit = DepositViewSet.as_view({'post': 'create'})
get_put_deposit = DepositViewSet.as_view({'get': 'retrieve', 'put': 'update'})

get_account = AccountViewSet.as_view({'get': 'retrieve'})

redeem = RedeemViewSet.as_view({'post': 'create'})
get_redeem_limits = RedeemViewSet.as_view({'get': 'limits'})

urlpatterns = [
    url(r'^deposit$', post_deposit, name='post_deposit'),
    url(r'^deposit/(?P<pk>[0-9]+)$', get_put_deposit, name="get_put_deposit", kwargs={'partial': True}),
    url(r'^account/(?P<login_name>[A-Za-z0-9_]{6,25})$', get_account, name="get_account"),
    url('^redeem/create$', redeem, name='post_redeem'),
    url('^redeem/([A-Za-z0-9]{6,25})/limits$', get_redeem_limits, name='get_limits'),
]
