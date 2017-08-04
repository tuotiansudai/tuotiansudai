# -*- coding: utf-8 -*-
import logging
from datetime import datetime

from django.db.models import Sum
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response

from current_rest import redis_client
from current_rest.biz import loan_service
from current_rest.biz.loan_service import LoanMatching, ACCOUNT_LOAN_MATCHING_REDIS_KEY
from current_rest.biz.loan_service import delete_history_data
from current_rest.constants import yesterday
from current_rest.exceptions import LoanMatchingException
from current_rest.models import CurrentAccount

logger = logging.getLogger(__name__)
LOAN_MATCHING_REDIS_KEY = 'loan:matching:{date}'


@api_view(['GET'])
def loan_matching(request):
    try:

        # 校验利息计算是否完成
        if not _is_calculate_interest():
            logger.info("[loan matching:] date:{} calculate interest unfinished ".format(yesterday))
            return Response({'message': 'calculate interest unfinished'}, status=status.HTTP_200_OK)

        if _is_loan_matched():
            logger.info("[loan matching:] date:{} loan matching status matching or success  ".format(yesterday))
            return Response({'message': 'date:{} loan matching status matching or success'.format(yesterday)},
                            status=status.HTTP_200_OK)

        # 查询全部日息宝用户
        accounts = CurrentAccount.objects.all().order_by('id')
        if not _check_balance(accounts):
            logger.info("[loan matching:] date:{} 日息宝买入金额大于债权总金额 ".format(yesterday))
            raise LoanMatchingException("日息宝买入金额大于债权总金额")

        redis_client.setex(LOAN_MATCHING_REDIS_KEY.format(date=yesterday),
                           'matching', 60 * 60 * 24 * 10)
        # 删除历史数据
        delete_count = delete_history_data()
        logger.info(
            "[loan matching:] date:{} delete history data successfully,count:{} ".format(
                datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
                delete_count))
        for account in accounts:
            if _is_account_matched(account):
                logger.info("[loan matching:] login_name:{} matching begin".format(account.login_name))

                LoanMatching(account).split_balance()

                logger.info("[loan matching:] login_name:{} matching end".format(account.login_name))

        redis_client.setex(LOAN_MATCHING_REDIS_KEY.format(date=yesterday),
                           'success', 60 * 60 * 24 * 10)
        return Response({'message': 'success'}, status=status.HTTP_200_OK)
    except Exception as e:
        redis_client.setex(LOAN_MATCHING_REDIS_KEY.format(date=yesterday),
                           'fail', 60 * 60 * 24 * 10)
        logger.error("[loan matching:] exception: {}".format(e))
        return Response({'message': e.message}, status=status.HTTP_400_BAD_REQUEST)


def _is_calculate_interest():
    return redis_client.exists("interest:{}".format(yesterday))


def _is_account_matched(account):
    return not redis_client.exists(ACCOUNT_LOAN_MATCHING_REDIS_KEY.format(
        date=yesterday,
        account_id=account.id)) or redis_client.get(ACCOUNT_LOAN_MATCHING_REDIS_KEY.format(
        date=yesterday,
        account_id=account.id)) != 'success'


def _is_loan_matched():
    return redis_client.exists(
        LOAN_MATCHING_REDIS_KEY.format(date=yesterday)) and redis_client.get(
        LOAN_MATCHING_REDIS_KEY.format(date=yesterday)) in ['success', 'matching']


def _check_balance(accounts):
    sum_buy_current = accounts.aggregate(Sum('balance'))
    sum_loan_amount = loan_service.valid_loan().aggregate(Sum('amount'))
    return sum_buy_current['balance__sum'] <= sum_loan_amount['amount__sum']
