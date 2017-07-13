# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response

from models import CurrentWithdraw
from serializers import CurrentWithdrawSerializer


@api_view(['GET'])
def hello(request):
    return Response('hello')


@api_view(['GET', 'POST'])
def withdraw_list(request):
    if request.method == 'GET':
        withdraws = CurrentWithdraw.objects.all()
        serializer = CurrentWithdrawSerializer(withdraws, many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        serializer = CurrentWithdrawSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET', 'PUT', 'DELETE'])
def withdraw_detail(request, pk):
    try:
        withdraw = CurrentWithdraw.objects.get(pk=pk)
    except CurrentWithdraw.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = CurrentWithdrawSerializer(withdraw)
        return Response(serializer.data)
    elif request.method == 'PUT':
        serializer = CurrentWithdrawSerializer(withdraw, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    elif request.method == 'DELETE':
        withdraw.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
