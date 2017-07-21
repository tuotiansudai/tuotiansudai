# -*- coding: utf-8 -*-
import logging

from rest_framework import viewsets, mixins

from current_rest import serializers, models

logger = logging.getLogger(__name__)


class AccountViewSet(mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    serializer_class = serializers.AccountSerializer
    queryset = models.CurrentAccount.objects.all()
    lookup_field = 'login_name'


