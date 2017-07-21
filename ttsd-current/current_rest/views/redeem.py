# -*- coding: utf-8 -*-
from datetime import datetime

from django.db import transaction
from django.db.models import Sum
from rest_framework import mixins
from rest_framework import status
from rest_framework import viewsets
from rest_framework.response import Response

from current_rest import constants
from current_rest import models
from current_rest import serializers
from current_rest.biz.current_account_manager import CurrentAccountManager


class RedeemViewSet(mixins.RetrieveModelMixin,
                    mixins.CreateModelMixin,
                    mixins.UpdateModelMixin,
                    viewsets.GenericViewSet):
    serializer_class = serializers.CurrentWithdrawSerializer
    queryset = models.CurrentWithdraw.objects.all()

    def __init__(self):
        super(RedeemViewSet, self).__init__()
        self.current_account_manager = CurrentAccountManager()

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        response = super(RedeemViewSet, self).create(request, *args, **kwargs)
        return Response(response.data, status=status.HTTP_201_CREATED)


