from django.conf.urls import url

from current_rest import views

urlpatterns = [
    url('^test$', views.hello),
    url('^withdraw/$', views.withdraw_list, name='withdraw_list'),
    url('^withdraw/(?P<pk>[0-9]+)$', views.withdraw_detail, name='withdraw_detail'),
]
