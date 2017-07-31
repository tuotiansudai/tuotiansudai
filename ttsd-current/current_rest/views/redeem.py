# -*- coding: utf-8 -*-

from rest_framework import mixins
from rest_framework import viewsets

from current_rest import serializers, models


class RedeemViewSet(mixins.CreateModelMixin,
                    mixins.RetrieveModelMixin,
                    mixins.UpdateModelMixin,
                    viewsets.GenericViewSet):
    serializer_class = serializers.CurrentRedeemSerializer
    queryset = models.CurrentRedeem.objects.all()
