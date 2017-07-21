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


class LoanViewSet(mixins.RetrieveModelMixin,
                  mixins.CreateModelMixin,
                  mixins.UpdateModelMixin,
                  viewsets.GenericViewSet):
    serializer_class = serializers.LoanSerializer
    queryset = models.Loan.objects.all()

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        response = super(LoanViewSet, self).create(request, *args, **kwargs)

        operation_log_service.log_contract_operation(response.data['id'],
                                                     response.data['creator'],
                                                     constants.OperationType.LOAN_ADD,
                                                     '创建通过债权申请'
                                                     )
        headers = self.get_success_headers(response.data)
        return Response(response.data, status=status.HTTP_201_CREATED, headers=headers)

    @transaction.atomic
    def update(self, request, *args, **kwargs):
        audit = kwargs.pop('audit', False)

        response = super(LoanViewSet, self).update(request, *args, **kwargs)

        if audit:
            operation_log_service.log_contract_operation(response.data['id'],
                                                         response.data['auditor'],
                                                         constants.OperationType.LOAN_AUDIT,
                                                         '审核通过债权申请'
                                                         )
        return Response(response.data, status=status.HTTP_201_CREATED)
