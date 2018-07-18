package com.tuotiansudai.mq.consumer.amount.service;

import com.tuotiansudai.enums.UmpTransferType;
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

@Service
public class UmpAmountTransferService {

    private static final Logger logger = LoggerFactory.getLogger(UmpAmountTransferService.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserBillMapper userBillMapper;

    @Transactional
    public void process(UmpAmountTransferMessage message) {
        logger.info("start amount transfer linked messages process.");
        do {
            amountTransferProcessOne(message);
            message = message.getNext();
        } while (message != null);
        logger.info("end amount transfer linked messages process");
    }

    private void amountTransferProcessOne(UmpAmountTransferMessage message) {
        logger.info("amount transfer process one message. loginName: {}, orderId:{}, amount:{}, businessType:{}",
                message.getLoginName(), message.getOrderId(), message.getAmount(), message.getBusinessType());

        UmpTransferType transferType = message.getTransferType();

        logger.info("transfer type:{}", transferType);

        String loginName = message.getLoginName();
        long orderId = message.getOrderId();
        long amount = message.getAmount();
        UserBillBusinessType businessType = message.getBusinessType();
        String operationLoginName = message.getOperatorLoginName();
        String interventionReason = message.getInterventionReason();

        switch (transferType) {
            case FREEZE:
                freeze(loginName, orderId, amount, businessType, operationLoginName, interventionReason);
                break;
            case UNFREEZE:
                unfreeze(loginName, orderId, amount, businessType, operationLoginName, interventionReason);
                break;
            case TRANSFER_IN_BALANCE:
                transferInBalance(loginName, orderId, amount, businessType, operationLoginName, interventionReason);
                break;
            case TRANSFER_OUT_BALANCE:
                transferOutBalance(loginName, orderId, amount, businessType, operationLoginName, interventionReason);
                break;
            case TRANSFER_OUT_FREEZE:
                transferOutFreeze(loginName, orderId, amount, businessType, operationLoginName, interventionReason);
                break;
            default:
                logger.error(MessageFormat.format("transfer type incorrect. transferType:{0}", transferType));
        }
    }


    private void freeze(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String interventionReason) {
        logger.info("start freeze, loginName:{}, orderId:{}, amount:{}, businessType:{}", loginName, orderId, amount, businessType.getDescription());

        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            logger.error(MessageFormat.format("{0} account is not exist", loginName));
        }

        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();
        if (balance < amount) {
            String template = "Freeze Failed (orderId = {0}): {1} balance {2} is less than amount {3}";
            logger.error(MessageFormat.format(template, String.valueOf(orderId), loginName, String.valueOf(balance), String.valueOf(amount)));
        }

        balance -= amount;
        freeze += amount;

        accountModel.setBalance(balance);
        accountModel.setFreeze(freeze);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel(loginName, orderId, amount, balance, freeze, businessType, UserBillOperationType.FREEZE, operatorLoginName, interventionReason);
        userBillMapper.create(userBillModel);
    }

    private void unfreeze(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String interventionReason) {
        logger.info("start unfreeze, loginName:{}, orderId:{}, amount:{}, businessType:{}", loginName, orderId, amount, businessType.getDescription());

        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            logger.error(MessageFormat.format("{0} account is not exist", loginName));
        }

        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();
        if (freeze < amount) {
            String template = "Unfreeze Failed (orderId = {0}): {1} freeze {2} is less than amount {3}";
            logger.error(MessageFormat.format(template, String.valueOf(orderId), loginName, String.valueOf(freeze), String.valueOf(amount)));
        }

        balance += amount;
        freeze -= amount;

        accountModel.setBalance(balance);
        accountModel.setFreeze(freeze);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel(loginName, orderId, amount, balance, freeze, businessType, UserBillOperationType.UNFREEZE, operatorLoginName, interventionReason);
        userBillMapper.create(userBillModel);
    }

    private void transferInBalance(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String interventionReason) {
        logger.info("start transferInBalance, loginName:{}, orderId:{}, amount:{}, businessType:{}", loginName, orderId, amount, businessType.getDescription());

        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            logger.error(MessageFormat.format("{0} account is not exist", loginName));
        }

        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();

        balance += amount;

        accountModel.setBalance(balance);
        accountModel.setFreeze(freeze);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel(loginName, orderId, amount, balance, freeze, businessType, UserBillOperationType.TI_BALANCE, operatorLoginName, interventionReason);
        userBillMapper.create(userBillModel);
    }

    private void transferOutBalance(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String interventionReason) {
        logger.info("start transferOutBalance, loginName:{}, orderId:{}, amount:{}, businessType:{}", loginName, orderId, amount, businessType.getDescription());

        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            logger.error(MessageFormat.format("{0} account is not exist", loginName));
        }

        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();

        if (balance < amount) {
            String template = "Transfer Out Balance Failed (orderId = {0}): {1} balance {2} is less than amount {3}";
            logger.error(MessageFormat.format(template, String.valueOf(orderId), loginName, String.valueOf(balance), String.valueOf(amount)));
        }

        balance -= amount;

        accountModel.setBalance(balance);
        accountModel.setFreeze(freeze);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel(loginName, orderId, amount, balance, freeze, businessType, UserBillOperationType.TO_BALANCE, operatorLoginName, interventionReason);
        userBillMapper.create(userBillModel);
    }

    private void transferOutFreeze(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String interventionReason) {
        logger.info("start transferOutFreeze, loginName:{}, orderId:{}, amount:{}, businessType:{}", loginName, orderId, amount, businessType.getDescription());

        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            logger.error(MessageFormat.format("{0} account is not exist", loginName));
        }

        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();

        if (freeze < amount) {
            String template = "Transfer Out Freeze Failed (orderId = {0}): {1} freeze {2} is less than amount {3}";
            logger.error(MessageFormat.format(template, String.valueOf(orderId), loginName, String.valueOf(freeze), String.valueOf(amount)));
        }

        freeze -= amount;

        accountModel.setBalance(balance);
        accountModel.setFreeze(freeze);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel(loginName, orderId, amount, balance, freeze, businessType, UserBillOperationType.TO_FREEZE, operatorLoginName, interventionReason);
        userBillMapper.create(userBillModel);
    }
}
