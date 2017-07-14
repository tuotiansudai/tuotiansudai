# coding=utf-8
LOAN_STATUS_APPROVING = 'APPROVING'
LOAN_STATUS_APPROVED = 'APPROVED'
LOAN_STATUS_EXPIRED = 'EXPIRED'

LOAN_STATUS_CHOICES = (
    (LOAN_STATUS_APPROVING, u'待审核'),
    (LOAN_STATUS_APPROVED, u'已审核'),
    (LOAN_STATUS_EXPIRED, u'已过期')
)

LOAN_TYPE_HOUSE = 'HOUSE'
LOAN_TYPE_VEHICLE = 'VEHICLE'
LOAN_TYPE_FACTORING = 'FACTORING'
LOAN_TYPE_BILL = 'BILL'
LOAN_TYPE_HUIZU = 'HUIZU'
LOAN_TYPE_SHUIYI = 'SHUIYI'

LOAN_TYPE_CHOICES = (
    (LOAN_TYPE_HOUSE, u'房产抵押借款'),
    (LOAN_TYPE_VEHICLE, u'车辆抵押借款'),
    (LOAN_TYPE_FACTORING, u'企业经营性借款-保理'),
    (LOAN_TYPE_BILL, u'企业经营性借款-票据'),
    (LOAN_TYPE_SHUIYI, u'税易经营性借款'),
    (LOAN_TYPE_HUIZU, u'汽车融资租赁')
)


class OperationTarget(object):
    LOAN = 'loan'

    OPERATION_TARGET_TYPE = (
        (LOAN, u'债权'),
    )


class OperationType(object):
    LOAN_ADD = 'LOAN_ADD'
    LOAN_AUDIT = 'LOAN_AUDIT'
    OPERATION_TYPE_MAP = (
        (LOAN_ADD, u'增加债权信息'),
        (LOAN_AUDIT, u'审核债权信息'),
    )
