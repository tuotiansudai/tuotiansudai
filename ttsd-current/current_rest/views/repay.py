# -*- coding: utf-8 -*-

from django.db.models import Sum
from datetime import datetime
from django.db import transaction
from rest_framework import mixins
from rest_framework import status
from rest_framework import viewsets
from rest_framework.decorators import api_view
from rest_framework.generics import get_object_or_404
from rest_framework.response import Response

from current_rest import constants
from current_rest import models
from current_rest import serializers


class LoanRepayViewSet(mixins.RetrieveModelMixin,
                       mixins.CreateModelMixin,
                       viewsets.GenericViewSet):
    serializer_class = serializers.LoanRepaySerializer
    queryset = models.Loan.objects.all()

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        loan = get_object_or_404(models.Loan, pk=request.data['loan'])
        amount = models.LoanRepay.objects.filter(loan_id=request.data['loan'], status=constants.REPAY_STATUS_WAITING)
        if not amount:
            response = super(LoanRepayViewSet, self).create(request, *args, **kwargs)
            # 发送消息 respnse.data['id']
            models.Task.objects.create(status=constants.TASK_PENDING,
                                       description=u'日息宝还款申请，请尽快审核',
                                       url=u'http://{0}:{1}/{2}{3}?'.format(),
                                       sender=request.data['submit_name'],
                                       handler_role='')

            return Response({'message', 'success'}, status=status.HTTP_201_CREATED)
        else:
            return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['PUT'])
@transaction.atomic
def audit_loan_repay(request, pk, result):

    loan_repay = models.LoanRepay.objects.filter(pk__in=pk)
    if loan_repay.exists():
        loan_repay.update(status=constants.REPAY_STATUS_APPROVED, approver=request.data['auditor'], approved_time=datetime.now(), updated_time=datetime.now())
    else:
        return Response({'message', 'param is error'}, status=status.HTTP_201_CREATED)

    if result == 'pass':
        operation_type = constants.OperationType.LOAN_REPAY_PASS
        content = u'{}审核通过还款申请'.format(request.data['auditor'])
        
    else:
        operation_type = constants.OperationType.LOAN_REPAY_REJECT
        content = u'{}驳回还款申请'.format(request.data['auditor'])

    models.OperationLog.objects.create(refer_type=constants.OperationTarget.LOAN_REPAY,
                                refer_pk=pk,
                                operator=request.data['auditor'],
                                operation_type=operation_type,
                                content=content)

    return Response({'message', 'success'}, status=status.HTTP_201_CREATED)

