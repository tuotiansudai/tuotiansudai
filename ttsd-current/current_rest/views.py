# -*- coding: utf-8 -*-
from __future__ import unicode_literals

# Create your views here.
from django.http import Http404
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework.views import APIView

from current_rest.models import Loan
from current_rest.serializers import LoanSerializer


@api_view(['GET'])
def hello(request):
    return Response('hello')


class LoanList(APIView):
    def get(self, request):
        loans = Loan.objects.all()
        serializer = LoanSerializer(loans, many=True)
        return Response(serializer.data)

    def post(self, request):
        serializer = LoanSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_200_OK)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class LoanDetail(APIView):
    def get_loan(self, pk):
        try:
            return Loan.objects.get(pk)
        except Loan.DoesNotExist:
            raise Http404

    def put(self, request, pk):
        loan = self.get_loan(pk)
        serializer = LoanSerializer(loan, data=request.DATA)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_200_OK)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
