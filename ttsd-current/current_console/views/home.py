# coding=utf-8
# -*-coding:utf-8 -*-
import datetime

from django.shortcuts import render


def index(request):
    return render(request, 'console/home.html', {'now': datetime.datetime.now()})


def handler404(request):
    return render(request, 'console/404.html', status=404)
