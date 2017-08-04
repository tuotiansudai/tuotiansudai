# -*- coding: utf-8 -*-

from django.db.models import Sum
from django.db import transaction
from rest_framework import mixins
from rest_framework import status
from rest_framework import viewsets
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
        loan = get_object_or_404(models.Loan, pk=request.data['loan_id'])
        amount = get_object_or_404(models.LoanRepay, pk=request.data['loan_id'], status=constants.REPAY_STATUS_WAITING)
        if not models.LoanRepay.objects.filter(loan_id=request.data['loan_id'], status=constants.REPAY_STATUS_WAITING).aggregate(Sum('repay_amount'))\
                and loan.amount == request.data['repay_amount']:
            response = super(LoanRepayViewSet, self).create(request, *args, **kwargs)
            # 发送消息 respnse.data['id']
            return Response({'message', 'success'}, status=status.HTTP_201_CREATED)
        else:
            return Response(status=status.HTTP_400_BAD_REQUEST)



