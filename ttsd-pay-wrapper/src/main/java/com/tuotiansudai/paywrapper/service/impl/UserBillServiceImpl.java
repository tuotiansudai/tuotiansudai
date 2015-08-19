package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.service.UserBillService;
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
public class UserBillServiceImpl implements UserBillService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserBillMapper userBillMapper;

    @Override
    @Transactional
    public void freeze(String loginName, long orderId, long amount, UserBillBusinessType businessType) throws AmountTransferException {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();
        if (balance < amount) {
            String template = "Freeze Failed (orderId = {0}): {1} balance {2} is less than amount {3}";
            throw new AmountTransferException(MessageFormat.format(template, orderId, loginName, String.valueOf(balance), String.valueOf(amount)));
        }

        balance -= amount;
        freeze += amount;

        accountModel.setBalance(balance);
        accountModel.setFreeze(balance);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel();
        userBillModel.setLoginName(loginName);
        userBillModel.setOrderId(orderId);
        userBillModel.setAmount(amount);
        userBillModel.setBalance(balance);
        userBillModel.setFreeze(freeze);
        userBillModel.setBusinessType(businessType);
        userBillModel.setOperationType(UserBillOperationType.FREEZE);

        userBillMapper.create(userBillModel);
    }

    @Override
    @Transactional
    public void unfreeze(String loginName, long orderId, long amount, UserBillBusinessType businessType) throws AmountTransferException {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();
        if (freeze < amount) {
            String template = "Unfreeze Failed (orderId = {0}): {1} freeze {2} is less than amount {3}";
            throw new AmountTransferException(MessageFormat.format(template, orderId, loginName, String.valueOf(balance), String.valueOf(amount)));
        }

        balance += amount;
        freeze -= amount;


        accountModel.setBalance(balance);
        accountModel.setFreeze(balance);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel();
        userBillModel.setLoginName(loginName);
        userBillModel.setOrderId(orderId);
        userBillModel.setAmount(amount);
        userBillModel.setBalance(balance);
        userBillModel.setFreeze(freeze);
        userBillModel.setBusinessType(businessType);
        userBillModel.setOperationType(UserBillOperationType.UNFREEZE);

        userBillMapper.create(userBillModel);
    }

    @Override
    @Transactional
    public void transferInBalance(String loginName, long orderId, long amount, UserBillBusinessType businessType) {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();

        balance += amount;

        accountModel.setBalance(balance);
        accountModel.setFreeze(balance);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel();
        userBillModel.setLoginName(loginName);
        userBillModel.setOrderId(orderId);
        userBillModel.setAmount(amount);
        userBillModel.setBalance(balance);
        userBillModel.setFreeze(freeze);
        userBillModel.setBusinessType(businessType);
        userBillModel.setOperationType(UserBillOperationType.TI_BALANCE);

        userBillMapper.create(userBillModel);
    }

    @Override
    @Transactional
    public void transferOutBalance(String loginName, long orderId, long amount, UserBillBusinessType businessType) throws AmountTransferException {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();

        if (balance < amount) {
            String template = "Transfer Out Balance Failed (orderId = {0}): {1} balance {2} is less than amount {3}";
            throw new AmountTransferException(MessageFormat.format(template, orderId, loginName, String.valueOf(balance), String.valueOf(amount)));
        }

        balance -= amount;

        accountModel.setBalance(balance);
        accountModel.setFreeze(balance);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel();
        userBillModel.setLoginName(loginName);
        userBillModel.setOrderId(orderId);
        userBillModel.setAmount(amount);
        userBillModel.setBalance(balance);
        userBillModel.setFreeze(freeze);
        userBillModel.setBusinessType(businessType);
        userBillModel.setOperationType(UserBillOperationType.TO_BALANCE);

        userBillMapper.create(userBillModel);
    }

    @Transactional
    public void transferOutFreeze(String loginName, long orderId, long amount, UserBillBusinessType businessType) {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        long balance = accountModel.getBalance();
        long freeze = accountModel.getFreeze();

        freeze -= amount;

        accountModel.setBalance(balance);
        accountModel.setFreeze(balance);
        accountMapper.update(accountModel);

        UserBillModel userBillModel = new UserBillModel();
        userBillModel.setLoginName(loginName);
        userBillModel.setOrderId(orderId);
        userBillModel.setAmount(amount);
        userBillModel.setBalance(balance);
        userBillModel.setFreeze(freeze);
        userBillModel.setBusinessType(businessType);
        userBillModel.setOperationType(UserBillOperationType.TO_FREEZE);

        userBillMapper.create(userBillModel);
    }
}
