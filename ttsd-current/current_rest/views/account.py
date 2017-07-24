# -*- coding: utf-8 -*-
import logging

import datetime
from django.http import Http404
from rest_framework import viewsets, mixins
from rest_framework.response import Response

from current_rest import serializers, models

logger = logging.getLogger(__name__)


class AccountViewSet(mixins.RetrieveModelMixin,
                     viewsets.GenericViewSet):
    serializer_class = serializers.AccountSerializer
    queryset = models.CurrentAccount.objects.all()
    lookup_field = 'login_name'

    def retrieve(self, request, *args, **kwargs):
        instance = models.CurrentAccount(login_name=kwargs.get(self.lookup_field),
                                         balance=0,
                                         created_time=datetime.datetime.now(),
                                         updated_time=datetime.datetime.now())
        try:
            instance = self.get_object()
        except Http404:
            pass
        serializer = self.get_serializer(instance)

        return Response(serializer.data)
