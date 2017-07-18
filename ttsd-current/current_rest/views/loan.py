# -*- coding: utf-8 -*-
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response

from current_rest import serializers
from current_rest.biz.loan_service import LoanService
from current_rest.serializers import json_validation_required, LoanSerializer


@api_view(['POST'])
@json_validation_required(serializers.LoanSerializer)
def post_loan(request, validated_data):
    try:
        LoanService().post_loan(validated_data=validated_data)
        return Response({}, status=status.HTTP_200_OK)
    except ValueError as ve:
        return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['PUT'])
@json_validation_required(serializers.LoanSerializer)
def audit_loan(request, validated_data):
    try:
        LoanService().audit(validated_data=validated_data)
        return Response({}, status=status.HTTP_200_OK)
    except ValueError as ve:
        return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET'])
def get_loan(request):
    id = request.GET.get('id', None)
    if id is None:
        Response(status=status.HTTP_400_BAD_REQUEST)
    loan = LoanService().get(id)
    return Response(LoanSerializer(loan).data, status=status.HTTP_200_OK)

