# -*- coding: utf-8 -*-

from datetime import datetime

from django.conf import settings
from django.db import transaction
from rest_framework import mixins, filters
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
                       mixins.UpdateModelMixin,
                       mixins.ListModelMixin,
                       viewsets.GenericViewSet):
    serializer_class = serializers.LoanRepaySerializer
    queryset = models.LoanRepay.objects.all()
    filter_backends = (filters.DjangoFilterBackend,)
    filter_fields = ('loan_id',)

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        loan = get_object_or_404(models.Loan, pk=request.data['loan_id'])
        amount = models.LoanRepay.objects.filter(loan_id=request.data['loan_id'], status=constants.REPAY_STATUS_WAITING)
        if amount:
            return Response({'message': u'该资产正在还款'}, status=status.HTTP_201_CREATED)
        elif loan.amount != float(request.data['repay_amount']):
            return Response({'message': u'还款本金应等于资产价值'}, status=status.HTTP_201_CREATED)

        response = super(LoanRepayViewSet, self).create(request, *args, **kwargs)

        models.Task.objects.create(status=constants.TASK_PENDING,
                                   description=u'日息宝还款申请，请尽快审核',
                                   url=settings.TASK_CONSOLE_HOST+'?id='+str(response.data['id']),
                                   sender=request.data['submit_name'],
                                   handler_role=constants.LOAN_REPAY_HANDLER_ROLE)

        return Response({'message': 'success'}, status=status.HTTP_201_CREATED)


@api_view(['PUT'])
@transaction.atomic
def audit_loan_repay(request, pk, result):
    loan_repay = models.LoanRepay.objects.filter(pk=pk, status=constants.REPAY_STATUS_WAITING)
    if not loan_repay:
        return Response({'message', u'该还款已审核'}, status=status.HTTP_201_CREATED)

    if result == 'pass':
        operation_type = constants.OperationType.LOAN_REPAY_PASS
        content = u'{}审核通过还款申请'.format(request.data['auditor'])
        loan_repay_status = constants.REPAY_STATUS_APPROVED
        # 已还款，债权到期
        loan = models.Loan.objects.filter(pk=request.data['loan_id'])
        if loan.exists():
            loan.update(status=constants.LOAN_STATUS_EXPIRED)

    elif result == 'reject':
        operation_type = constants.OperationType.LOAN_REPAY_REJECT
        content = u'{}驳回还款申请'.format(request.data['auditor'])
        loan_repay_status = constants.REPAY_STATUS_DENIED
    else:
        return Response({'message', 'param is error'}, status=status.HTTP_400_BAD_REQUEST)

    loan_repay.update(status=loan_repay_status, approver=request.data['auditor'],
                      approved_time=datetime.now(), updated_time=datetime.now())

    task = models.Task.objects.filter(pk=request.data['task_id'])
    if task.exists():
        task.update(status=constants.TASK_DONE, handler_name=request.data['auditor'],handler_time=datetime.now())

    models.OperationLog.objects.create(refer_type=constants.OperationTarget.LOAN_REPAY,
                                       refer_pk=pk,
                                       operator=request.data['auditor'],
                                       operation_type=operation_type,
                                       content=content)

    return Response({'message', 'success'}, status=status.HTTP_201_CREATED)
