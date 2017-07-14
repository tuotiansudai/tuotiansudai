# -*- coding: utf-8 -*-
from django.http import Http404
from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView

from current_rest import constants
from current_rest.biz.services import operation_log_service
from current_rest.models import Loan
from current_rest.serializers import LoanSerializer


class LoanList(APIView):
    def get(self, request):
        pk = request.GET.get('pk', None)
        if pk is None:
            return Response(status=status.HTTP_400_BAD_REQUEST)
        loan = self.get_loan(pk)
        serializer = LoanSerializer(loan)
        return Response(serializer.data, status=status.HTTP_200_OK)

    def post(self, request):
        serializer = LoanSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            operation_log_service.log_contract_operation(serializer.data['id'],
                                                         'operation',
                                                         constants.OperationType.LOAN_ADD,
                                                         '提交创建债权申请'
                                                         )
            return Response(serializer.data, status=status.HTTP_200_OK)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def put(self, request, pk):
        loan = self.get_loan(pk)
        serializer = LoanSerializer(loan, data=request.DATA)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_200_OK)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def get_loan(self, pk):
        try:
            return Loan.objects.get(id=pk)
        except Loan.DoesNotExist:
            raise Http404
