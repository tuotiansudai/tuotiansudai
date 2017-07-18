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
from django.conf.urls import url

from console.views import contract, applicant, system
from console.views import home
from console.views import merchant
from console.views import pay_log
from console.views import repay_manage
from console.views import staff, car
from console.views import upload
from console.views import user

home_urls = [
    url(r'^$', home.index, name='console_index'),
    url(r'^message/(?P<pk>\d+)$', home.message, name='console_message'),
]


urlpatterns = home_urls
