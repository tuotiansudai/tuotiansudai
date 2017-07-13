# -*- coding: utf-8 -*-
from rest_framework import serializers

from current_rest import models, constants


class DepositSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.CurrentDeposit
        fields = '__all__'


class DepositSuccessSerializer(serializers.Serializer):
    order_id = serializers.IntegerField(min_value=0, required=True)
    status = serializers.CharField(choices=constants.DEPOSIT_STATUS, required=True)
    created_time = serializers.DateTimeField(format='%Y-%m-%d %H:%M:%S')
