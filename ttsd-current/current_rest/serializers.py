from rest_framework import serializers
from models import CurrentWithdraw


class CurrentWithdrawSerializer(serializers.ModelSerializer):
    class Meta:
        model = CurrentWithdraw
        fields = ('id', 'account_id', 'amount', 'status', 'created_time', 'approve_time', 'approver')
