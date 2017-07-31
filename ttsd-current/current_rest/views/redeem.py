# -*- coding: utf-8 -*-
import django_filters
from rest_framework import mixins
from rest_framework import viewsets
from rest_framework import filters

from current_rest import serializers
from current_rest import models


class RedeemViewSet(mixins.CreateModelMixin,
                    viewsets.GenericViewSet):
    serializer_class = serializers.CurrentRedeemSerializer


class RedeemListFilter(django_filters.FilterSet):
    start_time = django_filters.DateTimeFilter(name='created_time', lookup_expr='gte')
    end_time = django_filters.DateTimeFilter(name='created_time', lookup_expr='lte')
    start_amount = django_filters.CharFilter(name='amount', lookup_expr='gte')
    end_amount = django_filters.CharFilter(name='amount', lookup_expr='lte')

    class Meta:
        model = models.CurrentRedeem
        fields = ['login_name', 'current_account__mobile', 'start_time', 'end_time', 'start_amount', 'end_amount', 'status']


class RedeemListViewSet(mixins.ListModelMixin,
                        viewsets.GenericViewSet):

    serializer_class = serializers.CurrentRedeemListSerializer
    queryset = models.CurrentRedeem.objects.all().order_by('-created_time')
    filter_backends = (filters.DjangoFilterBackend,)
    filter_class = RedeemListFilter


