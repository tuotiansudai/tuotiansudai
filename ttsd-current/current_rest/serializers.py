# -*- coding: utf-8 -*-
from rest_framework import serializers
from models import CurrentWithdraw

from current_rest import models, constants


class DepositSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.CurrentDeposit
        fields = '__all__'


class DepositSuccessSerializer(serializers.Serializer):
    order_id = serializers.IntegerField(min_value=0, required=True)
    success = serializers.BooleanField(required=True)
    status = serializers.ChoiceField(choices=constants.DEPOSIT_STATUS_CHOICE, required=True)


class CurrentWithdrawSerializer(serializers.ModelSerializer):
    class Meta:
        model = CurrentWithdraw
        fields = '__all__'
