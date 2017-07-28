# -*- coding: utf-8 -*-

from rest_framework import mixins
from rest_framework import viewsets

from current_rest import models
from current_rest import serializers


class RedeemViewSet(mixins.RetrieveModelMixin,
                    mixins.CreateModelMixin,
                    viewsets.GenericViewSet):
    serializer_class = serializers.CurrentRedeemSerializer
    queryset = models.CurrentRedeem.objects.all()

    def __init__(self):
        super(RedeemViewSet, self).__init__()