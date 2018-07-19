package com.tuotiansudai.mq.consumer.amount.service;

import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillMessageType;
import com.tuotiansudai.message.BankSystemBillMessage;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.repository.mapper.BankSystemBillMapper;
import com.tuotiansudai.repository.mapper.SystemBillMapper;
import com.tuotiansudai.repository.model.BankSystemBillModel;
import com.tuotiansudai.repository.model.SystemBillModel;
import com.tuotiansudai.repository.model.SystemBillOperationType;
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

        SystemBillMessageType messageType = message.getMessageType();

        logger.info("system bill message type:{}, bankOrderNo:{},bankOrderDate:{}, businessType:{}, amount:{}", messageType,
                message.getBankOrderNo(), message.getBankOrderDate(), message.getBusinessType(), String.valueOf(message.getAmount()));

        String bankOrderNo = message.getBankOrderNo();
        long amount = message.getAmount();
        SystemBillBusinessType businessType = message.getBusinessType();
        String detail = messageType.getDescription();
        String bankOrderDate = message.getBankOrderDate();
        long businuessId = message.getBusinessId();
        switch (messageType) {
            case TRANSFER_IN:
                transfer(businuessId, bankOrderNo, bankOrderDate, amount, businessType, SystemBillOperationType.IN, detail);
                break;
            case TRANSFER_OUT:
                transfer(businuessId, bankOrderNo, bankOrderDate, amount, businessType, SystemBillOperationType.OUT, detail);
                break;
            default:
                logger.error("system bill message type incorrect. message type:{0}", messageType);
                break;
        }
    }

    private void transfer(long businessId, String bankOrderNo, String bankOrderDate, long amount, SystemBillBusinessType businessType, SystemBillOperationType operationType, String detail) {
        BankSystemBillModel bankSystemBillModel = new BankSystemBillModel(businessId, bankOrderNo, bankOrderDate, amount, operationType, businessType, detail);
        bankSystemBillMapper.create(bankSystemBillModel);
    }
}
