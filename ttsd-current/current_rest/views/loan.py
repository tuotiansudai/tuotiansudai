# -*- coding: utf-8 -*-
import logging
from datetime import datetime
from django.db import transaction
from rest_framework import mixins, filters
import django_filters
from django.db import transaction
from django.http import Http404
from rest_framework import status
from rest_framework import viewsets
from rest_framework.decorators import api_view
from rest_framework.response import Response

from current_rest import constants
from current_rest import models
from current_rest import serializers
from current_rest.models import OperationLog, Loan
from current_rest.serializers import LoanSerializer

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

        OperationLog.objects.create(refer_type=constants.OperationTarget.LOAN,
                                    refer_pk=response.data['id'],
                                    operator=response.data['creator'],
                                    operation_type=constants.OperationType.LOAN_ADD,
                                    content=u'创建通过债权申请')

        return response

    @transaction.atomic
    def update(self, request, *args, **kwargs):
        response = super(LoanViewSet, self).update(request, *args, **kwargs)

        OperationLog.objects.create(refer_type=constants.OperationTarget.LOAN,
                                    refer_pk=response.data['id'],
                                    operator=response.data['auditor'],
                                    operation_type=constants.OperationType.LOAN_EDIT,
                                    content='编辑了债权申请')
        return response


class LoanListViewSet(mixins.ListModelMixin, viewsets.GenericViewSet):
    serializer_class = serializers.LoanListSerializer
    queryset = models.Loan.objects.all()


@api_view(['PUT'])
@transaction.atomic
def audit_reject_loan(request, pk, category):
    loan = Loan.objects.filter(pk__in=pk)
    if loan.exists():
        loan.update(status=request.data['status'], auditor=request.data['auditor'], updated_time=datetime.now())
    else:
        return Response({'message', 'param is error'}, status=status.HTTP_201_CREATED)

    if category == 'audit':
        operation_type = constants.OperationType.LOAN_AUDIT
        content = u'{}审核通过债权申请'.format(request.data['auditor'])
    else:
        operation_type = constants.OperationType.LOAN_REJECT
        content = u'{}驳回债权申请'.format(request.data['auditor'])

    OperationLog.objects.create(refer_type=constants.OperationTarget.LOAN,
                                refer_pk=pk,
                                operator=request.data['auditor'],
                                operation_type=operation_type,
                                content=content)

    return Response({'message', 'success'}, status=status.HTTP_201_CREATED)


class ApprovedLoanListFilter(django_filters.FilterSet):
    start_time = django_filters.DateTimeFilter(name='created_time', lookup_expr='gte')
    end_time = django_filters.DateTimeFilter(name='created_time', lookup_expr='lte')

    class Meta:
        model = models.Loan
        fields = ['loan_type', 'start_time', 'end_time', 'status', 'agent__login_name']


class ApprovedLoanListViewSet(mixins.ListModelMixin, viewsets.GenericViewSet):
    serializer_class = serializers.ApprovedLoanListSerializer
    queryset = models.Loan.objects.all()
    filter_backends = (filters.DjangoFilterBackend,)
    filter_class = ApprovedLoanListFilter

