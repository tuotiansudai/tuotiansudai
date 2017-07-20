# -*- coding: utf-8 -*-
import logging

from django.db import transaction
from rest_framework import mixins
from rest_framework import status
from rest_framework import viewsets
from rest_framework.response import Response

from current_rest import constants
from current_rest import models
from current_rest import serializers
from current_rest.biz.services import operation_log_service

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

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        self.perform_create(serializer)

        operation_log_service.log_contract_operation(serializer.data['id'],
                                                     serializer.data['creator'],
                                                     constants.OperationType.LOAN_ADD,
                                                     '提交创建债权申请'
                                                     )
        headers = self.get_success_headers(serializer.data)
        return Response(status=status.HTTP_201_CREATED, headers=headers)

    @transaction.atomic
    def update(self, request, *args, **kwargs):
        partial = kwargs.pop('partial', False)
        audit = kwargs.pop('audit', False)

        instance = self.get_object()
        serializer = self.get_serializer(instance, data=request.data, partial=partial)
        serializer.is_valid(raise_exception=True)
        self.perform_update(serializer)

        if audit:
            operation_log_service.log_contract_operation(serializer.data['id'],
                                                         serializer.data['auditor'],
                                                         constants.OperationType.LOAN_AUDIT,
                                                         '审核通过债权申请'
                                                         )
        return Response(serializer.data)
