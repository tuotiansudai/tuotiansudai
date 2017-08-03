# -*- coding: utf-8 -*-
import logging
from datetime import datetime

from django.db import transaction
from django.db.models import Sum
from django.http import Http404
from django.shortcuts import get_object_or_404
from rest_framework import mixins
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

    @staticmethod
    def get_limits_today(request):
        loan_amount_sum = models.Loan.objects.filter(status=constants.LOAN_STATUS_APPROVED,
                                                     effective_date__lte=datetime.now(),
                                                     expiration_date__gte=datetime.now()).aggregate(
            Sum('amount')).get('amount__sum', 0)

        account_balance_sum = models.CurrentAccount.objects.all().aggregate(
            Sum('balance')).get('balance__sum', 0)

        available_invest_amount = max(0, loan_amount_sum - account_balance_sum)
        return Response(available_invest_amount, status=status.HTTP_200_OK)


class LoanListViewSet(mixins.ListModelMixin, viewsets.GenericViewSet):
    serializer_class = serializers.LoanListSerializer
    queryset = models.Loan.objects.all()


@api_view(['PUT'])
@transaction.atomic
def audit_reject_loan(request, pk, category):
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

