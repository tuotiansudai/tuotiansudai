# -*- coding: utf-8 -*-
class SourceType(object):
    SOURCE_WEB = 'WEB'
    SOURCE_WE_CHAT = 'WE_CHAT'
    SOURCE_IOS = 'IOS'
    SOURCE_ANDROID = 'ANDROID'
    SOURCE_CHOICE = (
        (SOURCE_WEB, u'网站'),
        (SOURCE_WE_CHAT, u'微信'),
        (SOURCE_IOS, u'iOS'),
        (SOURCE_ANDROID, u'Android')
    )


class DepositStatusType(object):
    DEPOSIT_WAITING_PAY = 'WAITING_PAY'
    DEPOSIT_SUCCESS = 'SUCCESS'
    DEPOSIT_FAIL = 'FAIL'
    DEPOSIT_OVER_PAY = 'OVER_PAY'
    DEPOSIT_APPLYING_PAYBACK = 'APPLYING_PAYBACK'
    DEPOSIT_PAYBACK_SUCCESS = 'PAYBACK_SUCCESS'
    DEPOSIT_PAYBACK_FAIL = 'PAYBACK_FAIL'
    DEPOSIT_STATUS_CHOICE = (
        (DEPOSIT_WAITING_PAY, u'待支付'),
        (DEPOSIT_SUCCESS, u'买入成功'),
        (DEPOSIT_FAIL, u'买入失败'),
        (DEPOSIT_OVER_PAY, u'超投'),
        (DEPOSIT_APPLYING_PAYBACK, u'超投返款申请中'),
        (DEPOSIT_PAYBACK_SUCCESS, u'超投返款成功'),
        (DEPOSIT_PAYBACK_FAIL, u'超投返款失败'),
    )
