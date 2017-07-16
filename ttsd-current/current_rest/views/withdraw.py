# -*- coding: utf-8 -*-
import logging

from rest_framework.decorators import api_view
from rest_framework.response import Response

from current_rest.biz.withdraw import Withdraw


@api_view(['POST'])
def withdraw_create(request):
    if request.method == 'POST':
        print request.data
        return Response(Withdraw().withdraw_create(request.data))


@api_view(['GET'])
def withdraw_get_by_id(request,pk):
    if request.method == 'GET':
        return Response(Withdraw().withdraw_by_id(pk))


@api_view(['GET'])
def withraw_get_all(request):
    if request.method == 'GET':
        return Response(Withdraw().withdraw_all_list())
