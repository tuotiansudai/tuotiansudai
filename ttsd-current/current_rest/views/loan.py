# -*- coding: utf-8 -*-
import logging
import datetime
from django.db import transaction
from django.db.models import Q
from datetime import datetime
from django.shortcuts import get_object_or_404
from rest_framework import mixins
from django.db import transaction
from rest_framework import status
from rest_framework import viewsets
from rest_framework.decorators import api_view
from rest_framework.response import Response

from current_rest import constants
from current_rest import models
from current_rest import serializers
from current_rest.models import OperationLog, Loan

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


class LoanListViewSet(mixins.ListModelMixin, viewsets.GenericViewSet):
    serializer_class = serializers.LoanListSerializer
    queryset = models.Loan.objects.all()


@api_view(['PUT'])
@transaction.atomic
def audit_reject_loan(request, pk, category):
    loan = Loan.objects.filter(pk__in=pk)
    if loan.exists():
        loan.update(status=request.data['status'], auditor=request.data['auditor'], updated_time=datetime.datetime.now())
    else:
        return Response({'message', 'param is error'}, status=status.HTTP_201_CREATED)
    get_object_or_404(Loan, pk__in=pk).update(status=request.data['status'], auditor=request.data['auditor'])

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


class ApprovedLoanListViewSet(mixins.ListModelMixin, viewsets.GenericViewSet):
    serializer_class = serializers.ApprovedLoanListSerializer

    def get_queryset(self):
        queryset = models.Loan.objects.exclude(status=constants.LOAN_STATUS_APPROVING)
        params = self.request.query_params
        if params:
            if params['loan_type']:
                queryset = queryset.filter(loan_type=params['loan_type'])
            if params['agent__login_name']:
                queryset = queryset.filter(agent__login_name=params['agent__login_name'])
            if params.has_key('created_time'):
                end_time = datetime.datetime.strptime(str(params['created_time']), '%Y-%m-%d %H:%M:%S')+datetime.timedelta(days=1)
                queryset = queryset.filter(created_time__gte=params['created_time'], created_time__lte=end_time)
            if params['loan_matching_status'] and params['loan_matching_status'] == constants.LOAN_MATCHING_STATUS_WAITING:
                queryset = queryset.filter(effective_date__gte=datetime.datetime.now())
            if params['loan_matching_status'] and params['loan_matching_status'] == constants.LOAN_MATCHING_STATUS_DOING:
                queryset = queryset.filter(effective_date__lte=datetime.datetime.now(), expiration_date__gte=datetime.datetime.now())
            if params['loan_matching_status'] and params['loan_matching_status'] == constants.LOAN_MATCHING_STATUS_EXPIRED:
                queryset = queryset.filter(Q(expiration_date__lte=datetime.datetime.now()) | Q(status=constants.LOAN_STATUS_EXPIRED))

        return queryset
