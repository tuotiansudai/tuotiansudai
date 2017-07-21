# -*- coding: utf-8 -*-
from datetime import datetime

from current_rest import constants
from current_rest.models import OperationLog


def log(refer_type, refer_pk, operator, operation_type, content):
    return OperationLog.objects.create(
        refer_type=refer_type,
        refer_pk=refer_pk,
        operator=operator,
        operation_type=operation_type,
        content=content,
        timestamp=datetime.now()
    )


def log_contract_operation(refer_id, operator, operation_type, content):
    return log(constants.OperationTarget.LOAN, refer_id, operator, operation_type, content)
