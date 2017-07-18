# -*- coding: utf-8 -*-
import logging
from datetime import datetime

from django.db import transaction

from current_rest import constants
from current_rest.biz.services import operation_log_service
from current_rest.models import Loan, Agent

logger = logging.getLogger(__name__)


class LoanService(object):
    def get(self, id):
        try:
            return Loan.objects.get(id=id)
        except Loan.DoesNotExist as ld:
            raise ld

    @transaction.atomic
    def post_loan(self, validated_data):
        loan = Loan.objects.create(
            serial_number=validated_data.get('serial_number'),
            amount=validated_data.get('amount'),
            type=validated_data.get('type'),
            agent=validated_data.get('agent'),
            debtor=validated_data.get('debtor'),
            debtor_identity_card=validated_data.get('debtor_identity_card'),
            effective_date=validated_data.get('effective_date'),
            expiration_date=validated_data.get('expiration_date'),
            create_time=datetime.now(),
            update_time=datetime.now(),
            creator=validated_data.get('creator'),
            status=constants.LOAN_STATUS_APPROVING
        )

        operation_log_service.log_contract_operation(loan.id,
                                                     validated_data.get('creator'),
                                                     constants.OperationType.LOAN_ADD,
                                                     '提交创建债权申请'
                                                     )

        return loan

    @transaction.atomic
    def put_loan(self, validated_data):
        loan = Loan.objects.filter(id=validated_data.get('id')).update(
            amount=validated_data.get('amount'),
            type=validated_data.get('type'),
            agent=validated_data.get('agent'),
            debtor=validated_data.get('debtor'),
            debtor_identity_card=validated_data.get('debtor_identity_card'),
            effective_date=validated_data.get('effective_date'),
            expiration_date=validated_data.get('expiration_date'),
            update_time=datetime.now(),
            auditor=validated_data.get('auditor'),
            status=validated_data.get('status')
        )

        return loan

    @transaction.atomic
    def audit(self, validated_data):
        loan = self.put_loan(validated_data)

        operation_log_service.log_contract_operation(validated_data.get('id'),
                                                     validated_data.get('auditor'),
                                                     constants.OperationType.LOAN_AUDIT,
                                                     '审核通过债权申请'
                                                     )
