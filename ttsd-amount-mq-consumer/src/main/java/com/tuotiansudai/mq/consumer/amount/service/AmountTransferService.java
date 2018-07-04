package com.tuotiansudai.mq.consumer.amount.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.enums.TransferType;
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

import java.util.List;

@Service
public class AmountTransferService {

    private static final Logger logger = LoggerFactory.getLogger(AmountTransferService.class);

    private final BankAccountMapper bankAccountMapper;

    private final UserBillMapper userBillMapper;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public AmountTransferService(BankAccountMapper bankAccountMapper, UserBillMapper userBillMapper) {
        this.bankAccountMapper = bankAccountMapper;
        this.userBillMapper = userBillMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public void process(List<AmountTransferMessage> messages) {
        logger.info("start amount transfer messages process.");

        for (AmountTransferMessage message : messages) {
            BankAccountModel bankAccountModel = bankAccountMapper.findInvestorByLoginName(message.getLoginName());
            if (bankAccountModel == null) {
                logger.error("user bank account is not found, user: {}", message.getLoginName());
                continue;
            }
            logger.info("start transferInBalance, message: {}", gson.toJson(message));

            long amount = message.getAmount() * (message.getTransferType() == TransferType.TRANSFER_IN_BALANCE ? 1 : -1);

            bankAccountMapper.updateBalance(message.getLoginName(), amount);

            userBillMapper.create(new UserBillModel(message.getLoginName(),
                    message.getOrderId(),
                    message.getBankOrderNo(),
                    message.getBankOrderDate(),
                    message.getAmount(),
                    bankAccountModel.getBalance() + amount,
                    message.getBusinessType(),
                    amount > 0 ? UserBillOperationType.TI_BALANCE : UserBillOperationType.TO_BALANCE));

        }

        logger.info("end amount transfer messages process");
    }
}
