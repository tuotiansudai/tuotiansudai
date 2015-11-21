package com.tuotiansudai.util;

import com.tuotiansudai.exception.*;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
public class AmountTransfer {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserBillMapper userBillMapper;

    @Transactional
    public void freeze(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String interventionReason) throws AmountTransferException {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            throw new FreezeAmountException(MessageFormat.format("{0} account is not exist", loginName));
        }

        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();
        if (balance < amount) {
            String template = "Freeze Failed (orderId = {0}): {1} balance {2} is less than amount {3}";
            throw new FreezeAmountException(MessageFormat.format(template, String.valueOf(orderId), loginName, String.valueOf(balance), String.valueOf(amount)));
        }

        balance -= amount;
        freeze += amount;

        accountModel.setBalance(balance);
        accountModel.setFreeze(freeze);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel(loginName, orderId, amount, balance, freeze, businessType, UserBillOperationType.FREEZE, operatorLoginName, interventionReason);
        userBillMapper.create(userBillModel);
    }

    @Transactional
    public void unfreeze(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String interventionReason) throws AmountTransferException {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            throw new UnfreezeAmountException(MessageFormat.format("{0} account is not exist", loginName));
        }

        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();
        if (freeze < amount) {
            String template = "Unfreeze Failed (orderId = {0}): {1} freeze {2} is less than amount {3}";
            throw new UnfreezeAmountException(MessageFormat.format(template, String.valueOf(orderId), loginName, String.valueOf(balance), String.valueOf(amount)));
        }

        balance += amount;
        freeze -= amount;

        accountModel.setBalance(balance);
        accountModel.setFreeze(freeze);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel(loginName, orderId, amount, balance, freeze, businessType, UserBillOperationType.UNFREEZE, operatorLoginName, interventionReason);
        userBillMapper.create(userBillModel);
    }

    @Transactional
    public void transferInBalance(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String interventionReason) throws AmountTransferException {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            throw new TransferInAmountException(MessageFormat.format("{0} account is not exist", loginName));
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

    @Transactional
    public void transferOutBalance(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String interventionReason) throws AmountTransferException {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            throw new TransferOutAmountException(MessageFormat.format("{0} account is not exist", loginName));
        }

        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();

        if (balance < amount) {
            String template = "Transfer Out Balance Failed (orderId = {0}): {1} balance {2} is less than amount {3}";
            throw new TransferOutAmountException(MessageFormat.format(template, String.valueOf(orderId), loginName, String.valueOf(balance), String.valueOf(amount)));
        }

        balance -= amount;

        accountModel.setBalance(balance);
        accountModel.setFreeze(freeze);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel(loginName, orderId, amount, balance, freeze, businessType, UserBillOperationType.TO_BALANCE, operatorLoginName, interventionReason);
        userBillMapper.create(userBillModel);
    }

    @Transactional
    public void transferOutFreeze(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String interventionReason) throws AmountTransferException {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            throw new TransferOutFreezeAmountException(MessageFormat.format("{0} account is not exist", loginName));
        }

        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();

        if (freeze < amount) {
            String template = "Transfer Out Freeze Failed (orderId = {0}): {1} freeze {2} is less than amount {3}";
            throw new TransferOutFreezeAmountException(MessageFormat.format(template, String.valueOf(orderId), loginName, String.valueOf(freeze), String.valueOf(amount)));
        }

        freeze -= amount;

        accountModel.setBalance(balance);
        accountModel.setFreeze(freeze);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel(loginName, orderId, amount, balance, freeze, businessType, UserBillOperationType.TO_FREEZE, operatorLoginName, interventionReason);
        userBillMapper.create(userBillModel);
    }
}
