package com.tuotiansudai.mq.consumer.amount.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.enums.BankUserBillOperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.BankUserBillMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.BankUserBillModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AmountTransferService {

    private static final Logger logger = LoggerFactory.getLogger(AmountTransferService.class);

    private final UserMapper userMapper;

    private final BankAccountMapper bankAccountMapper;

    private final BankUserBillMapper bankUserBillMapper;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public AmountTransferService(UserMapper userMapper, BankAccountMapper bankAccountMapper, BankUserBillMapper bankUserBillMapper) {
        this.userMapper = userMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.bankUserBillMapper = bankUserBillMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public void process(List<AmountTransferMessage> messages) {
        logger.info("start amount transfer messages process.");

        for (AmountTransferMessage message : messages) {
            BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(message.getLoginName(), Role.INVESTOR);
            if (bankAccountModel == null) {
                logger.error("user bank account is not found, user: {}", message.getLoginName());
                continue;
            }
            logger.info("start transferInBalance, message: {}", gson.toJson(message));

            long amount = message.getAmount() * (message.getOperationType() == BankUserBillOperationType.IN ? 1 : -1);

            bankAccountMapper.updateInvestorBalance(message.getLoginName(), amount);

            UserModel userModel = userMapper.findByLoginName(message.getLoginName());

            bankUserBillMapper.create(new BankUserBillModel(message.getBusinessId(),
                    message.getLoginName(),
                    userModel.getMobile(),
                    userModel.getUserName(),
                    message.getRole(),
                    message.getAmount(),
                    bankAccountModel.getBalance() + amount,
                    message.getBankOrderNo(),
                    message.getBankOrderDate(),
                    message.getBusinessType(),
                    message.getOperationType()));

        }

        logger.info("end amount transfer messages process");
    }
}
