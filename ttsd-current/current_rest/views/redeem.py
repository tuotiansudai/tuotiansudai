# -*- coding: utf-8 -*-
import logging
from datetime import datetime

import django_filters
from django.conf import settings
from django.db import transaction
from django.shortcuts import get_object_or_404
from rest_framework import filters
from rest_framework import mixins
from rest_framework import status
from rest_framework import viewsets
from rest_framework.decorators import api_view
from rest_framework.response import Response

from current_rest import constants
from current_rest import serializers, models
from current_rest.biz.pay_manager import invoke_pay
from current_rest.models import CurrentRedeem, OperationLog

logger = logging.getLogger(__name__)


class RedeemListFilter(django_filters.FilterSet):
    start_time = django_filters.DateTimeFilter(name='created_time', lookup_expr='gte')
    end_time = django_filters.DateTimeFilter(name='created_time', lookup_expr='lte')
    start_amount = django_filters.CharFilter(name='amount', lookup_expr='gte')
    end_amount = django_filters.CharFilter(name='amount', lookup_expr='lte')

    class Meta:
        model = models.CurrentRedeem
        fields = ['login_name', 'current_account__mobile', 'start_time', 'end_time', 'start_amount', 'end_amount',
                  'status']


class RedeemViewSet(mixins.RetrieveModelMixin,
                    mixins.CreateModelMixin,
                    mixins.UpdateModelMixin,
                    mixins.ListModelMixin,
                    viewsets.GenericViewSet):
    serializer_class = serializers.CurrentRedeemSerializer
    queryset = models.CurrentRedeem.objects.all().order_by('-created_time')
    filter_backends = (filters.DjangoFilterBackend,)
    filter_class = RedeemListFilter


@api_view(['PUT'])
@transaction.atomic
def audit_redeem_pass(request, pk):
    redeem = get_object_or_404(CurrentRedeem, pk=pk)

    data = dict(login_name=redeem.login_name, amount=redeem.amount, source=redeem.source)
    url = '{}/redeem-to-loan/'.format(settings.PAY_WRAPPER_SERVER)
    invoke_pay(data, url)

    audit_redeem(redeem, request, constants.REDEEM_DOING)

    operation_type = constants.OperationType.REDEEM_AUDIT_PASS
    content = u'{}审核通过赎回申请'.format(request.data['auditor'])

    OperationLog.objects.create(refer_type=constants.OperationTarget.REDEEM, refer_pk=pk,
                                operator=request.data['auditor'],
                                operation_type=operation_type, content=content)
    return Response({'message', 'success'}, status=status.HTTP_200_OK)


@api_view(['PUT'])
@transaction.atomic
def audit_redeem_reject(request, pk):
    redeem = get_object_or_404(CurrentRedeem, pk=pk)

    audit_redeem(redeem, request, constants.REDEEM_REJECT)

    operation_type = constants.OperationType.REDEEM_AUDIT_REJECT
    content = u'{}驳回赎回申请'.format(request.data['auditor'])

    OperationLog.objects.create(refer_type=constants.OperationTarget.REDEEM, refer_pk=pk,
                                operator=request.data['auditor'],
                                operation_type=operation_type, content=content)
    return Response({'message', 'success'}, status=status.HTTP_200_OK)


def audit_redeem(redeem, request, redeem_status):
    redeem.status = redeem_status
    redeem.approver = request.data['auditor']
    redeem.approved_time = datetime.now()
    redeem.save()
