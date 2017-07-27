package com.tuotiansudai.mq.consumer.amount.service;

import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillMessageType;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.repository.mapper.SystemBillMapper;
import com.tuotiansudai.repository.model.SystemBillModel;
import com.tuotiansudai.repository.model.SystemBillOperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SystemBillService {

    private static final Logger logger = LoggerFactory.getLogger(SystemBillService.class);

    @Autowired
    private SystemBillMapper systemBillMapper;

    public void systemBillProcess(SystemBillMessage message) {
        logger.info("start system bill process, message");

        SystemBillMessageType messageType = message.getMessageType();

        logger.info("system bill message type:{}", messageType);

        long orderId = message.getOrderId();
        long amount = message.getAmount();
        SystemBillBusinessType businessType = message.getBusinessType();
        String detail = messageType.getDescription();

        switch (messageType) {
            case TRANSFER_IN:
                transferIn(orderId, amount, businessType, detail);
                break;
            case TRANSFER_OUT:
                transferOut(orderId, amount, businessType, detail);
                break;
            default:
                logger.error("system bill message type incorrect. message type:{0}", messageType);
                break;
        }
    }

    @Transactional
    private void transferOut(long orderId, long amount, SystemBillBusinessType businessType, String detail) {
        SystemBillModel systemBillModel = new SystemBillModel(orderId, amount, SystemBillOperationType.OUT, businessType, detail);
        systemBillMapper.create(systemBillModel);
    }

    @Transactional
    private void transferIn(long orderId, long amount, SystemBillBusinessType businessType, String detail) {
        SystemBillModel systemBillModel = new SystemBillModel(orderId, amount, SystemBillOperationType.IN, businessType, detail);
        systemBillMapper.create(systemBillModel);
    }
}
