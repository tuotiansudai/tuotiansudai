package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.request.UmpBindCardRequestDto;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.fudian.umpmessage.UmpBindCardMessage;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.UserFundMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UmpBindCardService {

    private static Logger logger = LoggerFactory.getLogger(UmpBindCardService.class);

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    private final BankCardMapper bankCardMapper;

    private final AccountMapper accountMapper;

    private final UserFundMapper userFundMapper;

    private final UserMapper userMapper;

    @Autowired
    public UmpBindCardService(BankCardMapper bankCardMapper, AccountMapper accountMapper, UserFundMapper userFundMapper, UserMapper userMapper) {
        this.bankCardMapper = bankCardMapper;
        this.accountMapper = accountMapper;
        this.userFundMapper = userFundMapper;
        this.userMapper = userMapper;
    }

    public UmpAsyncMessage bindBankCard(UmpBindCardRequestDto dto) {
        UserModel userModel = userMapper.findByLoginName(dto.getLoginName());

        BankCardModel passedBankCard = bankCardMapper.findPassedBankCardByLoginName(dto.getLoginName());

        if (passedBankCard != null) {
            return new UmpAsyncMessage(false, null, null, "已绑定银行卡，请勿重复绑定");
        }

        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        BankCardModel model = new BankCardModel(IdGenerator.generate(), dto);
        bankCardMapper.create(model);
        return bankWrapperClient.umpBindCard(dto.getLoginName(), accountModel.getPayUserId(), model.getId(), userModel.getUmpUserName(), userModel.getUmpIdentityNumber(), dto.getCardNumber(), false);
    }

    public UmpAsyncMessage replaceBankCard(UmpBindCardRequestDto dto) {
        BankCardModel model = new BankCardModel(IdGenerator.generate(), dto);
        model.setIsFastPayOn(bankCardMapper.findPassedBankCardByLoginName(dto.getLoginName()).isFastPayOn());
        bankCardMapper.create(model);
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        UserModel userModel = userMapper.findByLoginName(dto.getLoginName());
        return bankWrapperClient.umpBindCard(dto.getLoginName(), accountModel.getPayUserId(), model.getId(), userModel.getUmpUserName(), userModel.getUmpIdentityNumber(), dto.getCardNumber(), true);
    }

    @Transactional
    public void processBindCard(UmpBindCardMessage message) {
        BankCardModel model = bankCardMapper.findById(message.getBindCardModelId());
        if (model == null || !Lists.newArrayList(BankCardStatus.UNCHECKED, BankCardStatus.APPLY).contains(model.getStatus())) {
            logger.error("UmpBankCardModel not exist or status is error, bindCardModelId: {}", message.getBindCardModelId());
            return;
        }
        if (message.isReplaceCard()) {
            if (message.isStatus() && !message.isApply()) {
                BankCardModel previousBankCard = bankCardMapper.findPassedBankCardByLoginName(model.getLoginName());
                bankCardMapper.updateStatusAndBankCode(previousBankCard.getId(), BankCardStatus.REMOVED, previousBankCard.getBankCode());
                bankCardMapper.updateStatusAndBankCode(model.getId(), BankCardStatus.PASSED, message.getBankCode());
            } else {
                bankCardMapper.updateStatusAndBankCode(model.getId(), message.isStatus() ? BankCardStatus.APPLY : BankCardStatus.FAILED, message.getBankCode());
            }
        } else {
            bankCardMapper.updateStatusAndBankCode(model.getId(),
                    message.isStatus() ?
                            message.isApply() && model.getStatus() == BankCardStatus.UNCHECKED ? BankCardStatus.APPLY : BankCardStatus.PASSED :
                            BankCardStatus.FAILED,
                    message.getBankCode());
        }
    }

    public BankCardModel getPassedBankCard(String loginName) {
        return bankCardMapper.findPassedBankCardByLoginName(loginName);
    }

    public boolean isReplacing(String loginName) {
        return CollectionUtils.isNotEmpty(bankCardMapper.findApplyBankCardByLoginName(loginName));
    }

    public boolean isManual(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        UserFundView userFundView = userFundMapper.findUmpByLoginName(loginName);
        return accountModel.getFreeze() > 0 || accountModel.getBalance() > 0 || userFundView.getExpectedTotalCorpus() > 0 || userFundView.getExpectedTotalInterest() > 0;

    }
}
