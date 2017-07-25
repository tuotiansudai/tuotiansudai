"""huizu URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.10/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""

# -*-coding:utf-8 -*-
from django.conf.urls import url

from current_console.views import home
from current_console.views import loan

urlpatterns = [
    url('^index$', home.index, name='index'),
    url('^show-loan$', loan.show_loan, name='show_loan'),
    url('^create-loan$', loan.create_loan, name='create_loan'),
    url('^audit-loan$', loan.audit_loan, name='audit_loan'),
    url('^loan-list$', loan.loan_list, name='loan_list')
]
