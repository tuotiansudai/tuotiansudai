# coding=utf-8
from rest_framework import serializers

from current_rest.models import Loan


class LoanSerializer(serializers.ModelSerializer):
    class Meta:
        model = Loan
