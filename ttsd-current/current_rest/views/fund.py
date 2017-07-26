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
    query_form = serializers.FundHistoryQueryForm(data=request.GET)
    if query_form.is_valid():
        histories = list_fund_history(query_form.validated_data['begin_date'], query_form.validated_data['end_date'])
        return Response(histories)
    else:
        raise Http404()


class TodayFundSettingViewSet(RetrieveAPIView, UpdateAPIView):
    serializer_class = serializers.CurrentDailyFundInfoSerializer

    def get_object(self):
        try:
            return CurrentDailyFundInfo.objects.get(date__exact=datetime.now().today())
        except CurrentDailyFundInfo.DoesNotExist:
            raise Http404()
