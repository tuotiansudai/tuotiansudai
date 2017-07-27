# -*- coding: utf-8 -*-

from rest_framework import mixins
from rest_framework import viewsets

from current_rest import serializers


class RedeemViewSet(mixins.CreateModelMixin,
                    viewsets.GenericViewSet):
    serializer_class = serializers.CurrentRedeemSerializer
