# -*- coding: utf-8 -*-

from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response

from current_rest import serializers
from current_rest.biz.redeem import Redeem
from current_rest.serializers import json_validation_required


@api_view(['POST'])
@json_validation_required(serializers.CurrentWithdrawSerializer)
def redeem(request, validated_data):
    try:
        redeem_response = Redeem().redeem(validated_data=validated_data)
        return Response({"data": redeem_response}, status=status.HTTP_200_OK)
    except ValueError:
        return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET'])
def limits(request, login_name):
    try:
        redeem_response = Redeem().limits(request, login_name)
        return Response({"data": redeem_response}, status=status.HTTP_200_OK)
    except ValueError:
        return Response(status=status.HTTP_400_BAD_REQUEST)
