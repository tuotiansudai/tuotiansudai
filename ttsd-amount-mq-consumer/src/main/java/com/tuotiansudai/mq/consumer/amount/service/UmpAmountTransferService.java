package com.tuotiansudai.mq.consumer.amount.service;

import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.UmpAmountTransferMessage;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
public class UmpAmountTransferService {

    private static final Logger logger = LoggerFactory.getLogger(UmpAmountTransferService.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserBillMapper userBillMapper;

    @Transactional
    public void process(List<UmpAmountTransferMessage> messages) {
        logger.info("start amount transfer linked messages process.");
        for (UmpAmountTransferMessage message : messages){
            AccountModel accountModel = accountMapper.findByLoginName(message.getLoginName());
            if (accountModel == null) {
                logger.error(MessageFormat.format("{0} account is not exist", message.getLoginName()));
                continue;
            }
            amountTransferProcess(message, accountModel);
        }
        logger.info("end amount transfer linked messages process");
    }

    private void amountTransferProcess(UmpAmountTransferMessage message, AccountModel accountModel) {
        logger.info("amount transfer process one message. loginName: {}, orderId:{}, amount:{}, businessType:{}",
                message.getLoginName(), message.getOrderId(), message.getAmount(), message.getBusinessType());

        String loginName = message.getLoginName();
        long balance = accountModel.getBalance();
        long freeze= accountModel.getFreeze();
        long orderId = message.getOrderId();
        long amount = message.getAmount();
        UserBillBusinessType businessType = message.getBusinessType();
        UserBillOperationType type;

        switch (message.getTransferType()) {
            case FREEZE:
                if (balance < amount) {
                    String template = "Freeze Failed (orderId = {0}): {1} balance {2} is less than amount {3}";
                    logger.error(MessageFormat.format(template, String.valueOf(orderId), loginName, String.valueOf(balance), String.valueOf(amount)));
                    return;
                }
                balance -= amount;
                freeze += amount;
                type = UserBillOperationType.FREEZE;
                break;
            case UNFREEZE:
                if (freeze < amount) {
                    String template = "Unfreeze Failed (orderId = {0}): {1} freeze {2} is less than amount {3}";
                    logger.error(MessageFormat.format(template, String.valueOf(orderId), loginName, String.valueOf(freeze), String.valueOf(amount)));
                    return;
                }
                balance += amount;
                freeze -= amount;
                type = UserBillOperationType.UNFREEZE;
                break;
            case TRANSFER_IN_BALANCE:
                balance += amount;
                type = UserBillOperationType.TI_BALANCE;
                break;
            case TRANSFER_OUT_BALANCE:
                if (balance < amount) {
                    String template = "Transfer Out Balance Failed (orderId = {0}): {1} balance {2} is less than amount {3}";
                    logger.error(MessageFormat.format(template, String.valueOf(orderId), loginName, String.valueOf(balance), String.valueOf(amount)));
                    return;
                }

                balance -= amount;
                type = UserBillOperationType.TO_BALANCE;
                break;
            case TRANSFER_OUT_FREEZE:
                if (freeze < amount) {
                    String template = "Transfer Out Freeze Failed (orderId = {0}): {1} freeze {2} is less than amount {3}";
                    logger.error(MessageFormat.format(template, String.valueOf(orderId), loginName, String.valueOf(freeze), String.valueOf(amount)));
                    return;
                }
                freeze -= amount;
                type = UserBillOperationType.TO_FREEZE;
                break;
            default:
                return;
        }
        accountMapper.updateBalanceAndFreeze(loginName, balance, freeze);
        UserBillModel userBillModel = new UserBillModel(loginName, orderId, amount, balance, freeze, businessType, type, null, null);
        userBillMapper.create(userBillModel);
    }
}
