# coding=utf-8

from rest_framework import serializers

from current_rest import models
from current_rest.models import Loan


class DepositSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.CurrentDeposit
        fields = '__all__'


class DepositSuccessSerializer(serializers.Serializer):
    order_id = serializers.IntegerField(min_value=0, required=True)
    success = serializers.BooleanField(required=True)


class LoanSerializer(serializers.ModelSerializer):
    class Meta:
        model = Loan
