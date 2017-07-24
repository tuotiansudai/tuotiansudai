# -*- coding: utf-8 -*-
import logging
from datetime import datetime

from django.http import Http404
from rest_framework.decorators import api_view
from rest_framework.generics import RetrieveAPIView, UpdateAPIView
from rest_framework.response import Response

from current_rest import serializers
from current_rest.biz.fund import generate_fund_tendency, list_fund_history
from current_rest.models import CurrentDailyFundInfo

logger = logging.getLogger(__name__)


@api_view(['GET'])
def tendency(request):
    return Response(generate_fund_tendency())


@api_view(['GET'])
def history(request):
    begin_date = request.GET.get('begin_date', None)
    end_date = request.GET.get('end_date', None)
    histories = []
    if begin_date and end_date:
        begin_date = datetime.strptime(begin_date, '%Y-%m-%d')
        end_date = datetime.strptime(end_date, '%Y-%m-%d')
        histories = list_fund_history(begin_date, end_date)
    return Response(histories)


class TodayFundSettingViewSet(RetrieveAPIView, UpdateAPIView):
    serializer_class = serializers.CurrentDailyFundInfoSerializer

    def get_object(self):
        try:
            return CurrentDailyFundInfo.objects.get(date__exact=datetime.now().today())
        except CurrentDailyFundInfo.DoesNotExist:
            raise Http404()

    def retrieve(self, request, *args, **kwargs):
        instance = self.get_object()
        serializer = self.get_serializer(instance)
        return Response(serializer.data)

    def update(self, request, *args, **kwargs):
        instance = self.get_object()
        serializer = self.get_serializer(instance, data=request.data, partial=True)
        serializer.is_valid(raise_exception=True)
        self.perform_update(serializer)
        return Response(serializer.data)
