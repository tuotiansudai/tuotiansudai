# -*- coding: utf-8 -*-

from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response

from current_rest import serializers
from current_rest.biz.redeem import Redeem
from current_rest.serializers import json_validation_required


@api_view(['POST'])
@json_validation_required(serializers.CurrentWithdrawSerializer)
def redeem(request, login_name):
    data = request.data
    data.update({'login_name': login_name})
    try:
        redeem_response = Redeem().redeem(data)
        return Response(redeem_response, status=status.HTTP_200_OK)
    except ValueError:
        return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET'])
def redeem_get_by_id(request, pk):
    if request.method == 'GET':
        try:
            redeem_response = Redeem().redeem_by_id(pk)
            return Response(redeem_response, status=status.HTTP_200_OK)
        except ValueError:
            return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET'])
def redeem_get_all(request):
    if request.method == 'GET':
        try:
            redeem_response = Redeem().redeem_all_list()
            return Response(redeem_response, status=status.HTTP_200_OK)
        except ValueError:
            return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET'])
def redeem_audit(request, pk, st):
    if request.method == 'GET':
        try:
            Redeem().redeem_audit(pk, st)
            return Response(data={"message": "success"}, status=status.HTTP_200_OK)
        except ValueError:
            return Response(data={"message": "fail"}, status=status.HTTP_400_BAD_REQUEST)
