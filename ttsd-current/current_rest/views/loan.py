# -*- coding: utf-8 -*-
import logging
from datetime import datetime

from django.db import transaction
from django.db.models import Sum
from django.shortcuts import get_object_or_404
from rest_framework import mixins
from rest_framework import status
from rest_framework import viewsets
from rest_framework.decorators import api_view
from rest_framework.response import Response

from current_rest import constants
from current_rest import models
from current_rest import serializers
from current_rest.biz import pay_manager
from current_rest.biz.current_daily_manager import sum_interest_by_date, sum_success_deposit_by_date
from current_rest.exceptions import PayWrapperException
from current_rest.models import OperationLog, Loan
from current_rest.settings import PAY_WRAPPER_SERVER

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


class LoanOutViewSet(mixins.RetrieveModelMixin,
                     mixins.CreateModelMixin,
                     mixins.UpdateModelMixin,
                     viewsets.GenericViewSet):
    serializer_class = serializers.LoanOutHistorySerializer
    queryset = models.CurrentLoanOutHistory.objects.all()

    loan_out_url = '{}/loan-out/'.format(PAY_WRAPPER_SERVER)

    def create(self, request, *args, **kwargs):
        logger.info('loan out, request data:'.format(request.data))
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        bill_date = serializer.validated_data.get('bill_date')

        queryset_filter = self.queryset.filter(bill_date=bill_date)

        if queryset_filter.exists():
            current_loan_out = queryset_filter.first()
            logger.error("loan out {} is triggered and status is {}, "
                         "do not trigger repeatedly".format(bill_date, current_loan_out.status))
            return Response(status=status.HTTP_400_BAD_REQUEST)

        interest_by_date = sum_interest_by_date(bill_date)
        deposit_by_date = sum_success_deposit_by_date(bill_date)
        serializer.validated_data['interest_amount'] = interest_by_date
        serializer.validated_data['deposit_amount'] = deposit_by_date

        if interest_by_date == 0:
            serializer.validated_data['status'] = constants.LOAN_OUT_STATUS_WAITING_PAY
            if deposit_by_date == 0:
                serializer.validated_data['status'] = constants.LOAN_OUT_STATUS_SUCCESS

        instance = serializer.save()

        logger.info('loan out {}, interest_amount={} deposit_amount={} status={}'.format(instance.bill_date,
                                                                                         instance.interest_amount,
                                                                                         instance.deposit_amount,
                                                                                         instance.status))

        self.invoke_pay(instance=instance)

        headers = self.get_success_headers(serializer.data)
        return Response(serializer.data, status=status.HTTP_201_CREATED, headers=headers)

    def update(self, request, *args, **kwargs):
        logger.info('loan out, request data:'.format(request.data))
        partial = kwargs.pop('partial', False)
        instance = self.get_object()
        serializer = self.get_serializer(instance, data=request.data, partial=partial)
        serializer.is_valid(raise_exception=True)

        if not self.verify_update_status(instance, serializer.validated_data):
            return Response(status=status.HTTP_400_BAD_REQUEST)

        if serializer.validated_data.get('status') == constants.LOAN_OUT_STATUS_RESERVE_TRANSFER_SUCCESS:
            serializer.validated_data['status'] = constants.LOAN_OUT_STATUS_SUCCESS if instance.deposit_amount == 0 \
                else constants.LOAN_OUT_STATUS_WAITING_PAY

        updated_instance = serializer.save()

        if updated_instance.status in [constants.LOAN_OUT_STATUS_RESERVE_TRANSFER_FAIL, constants.LOAN_OUT_STATUS_FAIL]:
            logger.error('loan out {}, status is {}'.format(updated_instance.bill_date.strftime('%Y-%m-%d'),
                                                            updated_instance.status))

        self.invoke_pay(updated_instance)

        return Response(serializer.data)

    @staticmethod
    def verify_update_status(instance, validated_data):
        if instance.status not in [constants.LOAN_OUT_STATUS_RESERVE_TRANSFER_WAITING_PAY,
                                   constants.LOAN_OUT_STATUS_WAITING_PAY]:
            logger.error('loan out {} status is {}, can not update'.format(instance.bill_date.strftime('%Y-%m-%d'),
                                                                           instance.status))
            return False

        update_status = validated_data.get('status')
        reserve_update_status_check = (instance.status == constants.LOAN_OUT_STATUS_RESERVE_TRANSFER_WAITING_PAY
                                       and update_status in [constants.LOAN_OUT_STATUS_RESERVE_TRANSFER_SUCCESS,
                                                             constants.LOAN_OUT_STATUS_RESERVE_TRANSFER_FAIL])
        loan_out_status_check = (instance.status == constants.LOAN_OUT_STATUS_WAITING_PAY
                                 and update_status in [constants.LOAN_OUT_STATUS_SUCCESS,
                                                       constants.LOAN_OUT_STATUS_FAIL])

        if not reserve_update_status_check and not loan_out_status_check:
            logger.error(
                'loan out {} status is {}, can not update status to {}'.format(instance.bill_date.strftime('%Y-%m-%d'),
                                                                               instance.status,
                                                                               update_status))
            return False

        return True

    def invoke_pay(self, instance):
        if instance.status in [constants.LOAN_OUT_STATUS_RESERVE_TRANSFER_WAITING_PAY,
                               constants.LOAN_OUT_STATUS_WAITING_PAY]:
            try:
                serializer = self.serializer_class(instance=instance)
                pay_manager.invoke_pay(data=serializer.data, url=self.loan_out_url)
            except PayWrapperException, ex:
                logger.error(ex)
