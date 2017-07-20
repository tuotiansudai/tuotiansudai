# -*- coding: utf-8 -*-
import logging

from rest_framework import mixins
from rest_framework import viewsets

from current_rest import models
from current_rest import serializers

logger = logging.getLogger(__name__)


# @api_view(['POST'])
# @json_validation_required(serializers.LoanSerializer)
# def post_loan(request, validated_data):
#     try:
#         LoanService().post_loan(validated_data=validated_data)
#         return Response({}, status=status.HTTP_200_OK)
#     except ValueError as ve:
#         return Response(status=status.HTTP_400_BAD_REQUEST)
#
#
# @api_view(['PUT'])
# @json_validation_required(serializers.LoanSerializer)
# def audit_loan(request, validated_data):
#     try:
#         LoanService().audit(validated_data=validated_data)
#         return Response({}, status=status.HTTP_200_OK)
#     except ValueError as ve:
#         return Response(status=status.HTTP_400_BAD_REQUEST)
#
#
# @api_view(['GET'])
# def get_loan(request):
#     id = request.GET.get('id', None)
#     if id is None:
#         Response(status=status.HTTP_400_BAD_REQUEST)
#     loan = LoanService().get(id)
#     return Response(LoanSerializer(loan).data, status=status.HTTP_200_OK)

class LoanViewSet(mixins.RetrieveModelMixin,
                  mixins.CreateModelMixin,
                  mixins.UpdateModelMixin,
                  viewsets.GenericViewSet):
    serializer_class = serializers.LoanSerializer
    queryset = models.Loan.objects.all()
