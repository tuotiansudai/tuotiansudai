# -*- coding: utf-8 -*-

from rest_framework import mixins, filters
from rest_framework import viewsets
from current_rest import models, constants
from current_rest import serializers


class TaskViewSet(mixins.RetrieveModelMixin,
                  mixins.CreateModelMixin,
                  mixins.ListModelMixin,
                  viewsets.GenericViewSet):
    serializer_class = serializers.TaskSerializer

    def get_queryset(self):
        queryset = models.Task.objects.filter(status=constants.TASK_PENDING)
        handler_role = self.request.query_params.getlist('handler_role')
        queryset = queryset.filter(handler_role__in=handler_role)
        return queryset

