package com.tuotiansudai.mq.consumer.amount.service;

import com.tuotiansudai.enums.BillOperationType;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.message.BankSystemBillMessage;
import com.tuotiansudai.repository.mapper.BankSystemBillMapper;
import com.tuotiansudai.repository.model.BankSystemBillModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BankSystemBillService {

    private static final Logger logger = LoggerFactory.getLogger(BankSystemBillService.class);


    @Autowired
    private BankSystemBillMapper bankSystemBillMapper;

    @Transactional
    public void systemBillProcess(BankSystemBillMessage message) {
        logger.info("start bank system bill process, message");

        BillOperationType operationType = message.getOperationType();

        logger.info("system bill message operationType:{}, bankOrderNo:{},bankOrderDate:{}, businessType:{}, amount:{}", operationType,
                message.getBankOrderNo(), message.getBankOrderDate(), message.getBusinessType(), String.valueOf(message.getAmount()));

        String bankOrderNo = message.getBankOrderNo();
        long amount = message.getAmount();
        SystemBillBusinessType businessType = message.getBusinessType();
        String detail = operationType.getDescription();
        String bankOrderDate = message.getBankOrderDate();
        long businuessId = message.getBusinessId();

        BankSystemBillModel bankSystemBillModel = new BankSystemBillModel(businuessId, bankOrderNo, bankOrderDate, amount, operationType, businessType, detail);
        bankSystemBillMapper.create(bankSystemBillModel);
    }

}
