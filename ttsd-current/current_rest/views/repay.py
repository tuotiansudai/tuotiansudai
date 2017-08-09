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
        loan_repay = models.LoanRepay.objects.filter(loan_id=request.data['loan_id'], status=constants.REPAY_STATUS_WAITING)
        if loan_repay:
            return Response({'message': u'该资产正在还款'}, status=status.HTTP_400_BAD_REQUEST)
        elif loan.amount != float(request.data['repay_amount']):
            return Response({'message': u'还款本金应等于资产价值'}, status=status.HTTP_400_BAD_REQUEST)

        response = super(LoanRepayViewSet, self).create(request, *args, **kwargs)

        models.Task.objects.create(status=constants.TASK_PENDING,
                                   description=u'日息宝还款申请，请尽快审核',
                                   url=settings.TASK_CONSOLE_HOST+'/'+str(response.data['id']),
                                   sender=request.data['submit_name'],
                                   handler_role=constants.LOAN_REPAY_HANDLER_ROLE)

        return Response({'message': 'success'}, status=status.HTTP_201_CREATED)


@api_view(['PUT'])
@transaction.atomic
def audit_loan_repay_pass(request, pk):
    loan_repay = models.LoanRepay.objects.filter(pk=pk, status=constants.REPAY_STATUS_WAITING)
    if not loan_repay:
        return Response({'message': u'该还款已审核'}, status=status.HTTP_400_BAD_REQUEST)

    loan_repay_status = constants.REPAY_STATUS_APPROVED
    update_loan_repay_status(loan_repay, loan_repay_status, request.data['auditor'])

    # 已还款，债权到期
    loan = get_object_or_404(models.Loan, pk=request.data['loan_id'])
    loan.status = constants.LOAN_STATUS_EXPIRED
    loan.save()

    task = get_object_or_404(models.Task, pk=request.data['task_id'])
    update_task_status(task, request.data['auditor'])
    create_operationLog(constants.OperationTarget.LOAN_REPAY, pk, request.data['auditor'],
                        constants.OperationType.LOAN_REPAY_PASS, u'{}审核通过还款申请'.format(request.data['auditor']))
    return Response({'message': 'success'}, status=status.HTTP_201_CREATED)


@api_view(['PUT'])
@transaction.atomic
def audit_loan_repay_reject(request, pk):
    loan_repay = models.LoanRepay.objects.filter(pk=pk, status=constants.REPAY_STATUS_WAITING)
    if not loan_repay:
        return Response({'message': u'该还款已审核'}, status=status.HTTP_400_BAD_REQUEST)

    loan_repay_status = constants.REPAY_STATUS_DENIED
    update_loan_repay_status(loan_repay, loan_repay_status, request.data['auditor'])

    task = get_object_or_404(models.Task, pk=request.data['task_id'])
    update_task_status(task, request.data['auditor'])

    create_operationLog(constants.OperationTarget.LOAN_REPAY, pk, request.data['auditor'],
                        constants.OperationType.LOAN_REPAY_REJECT, u'{}驳回还款申请'.format(request.data['auditor']))
    return Response({'message': 'success'}, status=status.HTTP_201_CREATED)


def update_loan_repay_status(loan_repay, status, approver):
    loan_repay.update(status=status, approver=approver,
                      approved_time=datetime.now(), updated_time=datetime.now())


def update_task_status(task, handler_name):
    task.status = constants.TASK_DONE
    task.handler_name = handler_name
    task.handler_time = datetime.now()
    task.save()


def create_operationLog(refer_type, refer_pk, operator, operation_type, content):
    models.OperationLog.objects.create(refer_type=refer_type, refer_pk=refer_pk, operator=operator,
                                       operation_type=operation_type, content=content)