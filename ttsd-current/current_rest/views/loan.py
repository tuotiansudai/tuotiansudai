# -*- coding: utf-8 -*-
import logging
from datetime import datetime, timedelta

from django.db import transaction
from django.db.models import Sum
from rest_framework import mixins
from rest_framework import status
from rest_framework import viewsets
from rest_framework.response import Response

from current_rest import constants
from current_rest import models
from current_rest import serializers
from current_rest.models import OperationLog

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
                                    content='创建通过债权申请')

        return Response(response.data, status=status.HTTP_201_CREATED)

    @transaction.atomic
    def update(self, request, *args, **kwargs):
        audit = kwargs.pop('audit', False)

        response = super(LoanViewSet, self).update(request, *args, **kwargs)

        if audit:
            OperationLog.objects.create(refer_type=constants.OperationTarget.LOAN,
                                        refer_pk=response.data['id'],
                                        operator=response.data['auditor'],
                                        operation_type=constants.OperationType.LOAN_AUDIT,
                                        content='审核通过债权申请')
        return Response(response.data, status=status.HTTP_201_CREATED)

    @staticmethod
    def get_limits_today(request):
        print(datetime.now())
        loan_amount_sum = models.Loan.objects.filter(status=constants.LOAN_STATUS_APPROVED,
                                                     effective_date__lte=datetime.now(),
                                                     expiration_date__gte=datetime.now()).aggregate(
            Sum('amount')).get('amount__sum')

        account_balance_sum = models.CurrentAccount.objects.all().aggregate(
            Sum('balance')) \
            .get('balance__sum')

        loan_amount_sum = int(loan_amount_sum) if loan_amount_sum is not None else 0
        account_balance_sum = int(account_balance_sum) if account_balance_sum is not None else 0
        available_invest_amount = loan_amount_sum - account_balance_sum if loan_amount_sum - account_balance_sum >= 0 else 0

        return Response(available_invest_amount, status=status.HTTP_200_OK)
