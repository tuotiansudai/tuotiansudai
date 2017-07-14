from django.conf.urls import url
from django.contrib import admin
from current_rest import views

urlpatterns = [
    url('^test$', views.hello),
    url(r'^loans$', views.LoanList.as_view(), name="loan_list"),
]
