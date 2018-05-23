package com.tuotiansudai.mq.consumer.amount.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.*;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
public class AmountTransferService {

    private static final Logger logger = LoggerFactory.getLogger(AmountTransferService.class);

    private final BankAccountMapper bankAccountMapper;

    private final UserBillMapper userBillMapper;

    @Autowired
    public AmountTransferService(BankAccountMapper bankAccountMapper, UserBillMapper userBillMapper) {
        this.bankAccountMapper = bankAccountMapper;
        this.userBillMapper = userBillMapper;
    }

    @Transactional
    public void amountTransferProcess(AmountTransferMessage message) throws AmountTransferException {
        logger.info("start amount transfer linked messages process.");
        do {
            amountTransferProcessOne(message);
            message = message.getNext();
        } while (message != null);
        logger.info("end amount transfer linked messages process");
    }

    private void amountTransferProcessOne(AmountTransferMessage message) throws AmountTransferException {
        logger.info("amount transfer process one message. loginName: {}, orderId:{}, amount:{}, businessType:{}",
                message.getLoginName(), message.getOrderId(), message.getAmount(), message.getBusinessType());

        TransferType transferType = message.getTransferType();

        logger.info("transfer type:{}", transferType);

        if (!Lists.newArrayList(TransferType.TRANSFER_IN_BALANCE, TransferType.TRANSFER_OUT_BALANCE).contains(message.getTransferType())) {
            throw new AmountTransferException(MessageFormat.format("transfer type incorrect. transferType:{0}", transferType));
        }

        String loginName = message.getLoginName();

        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(loginName);
        if (bankAccountModel == null) {
            throw new TransferInAmountException(MessageFormat.format("{0} account is not exist", loginName));
        }

        long orderId = message.getOrderId();
        long amount = message.getAmount() * (transferType == TransferType.TRANSFER_IN_BALANCE ? 1 : -1);
        UserBillBusinessType businessType = message.getBusinessType();

        logger.info("start transferInBalance, loginName:{}, orderId:{}, amount:{}, businessType:{}", loginName, orderId, amount, businessType.getDescription());

        bankAccountMapper.updateBalance(loginName, amount);

        userBillMapper.create(new UserBillModel(loginName,
                orderId,
                message.getAmount(),
                bankAccountModel.getBalance() + amount,
                businessType,
                amount > 0 ? UserBillOperationType.TI_BALANCE : UserBillOperationType.TO_BALANCE));
    }
}
