package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.service.InvestRepayService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BindBankCardServiceImpl implements BindBankCardService {

    static Logger logger = Logger.getLogger(BindBankCardServiceImpl.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestRepayService investRepayService;

    @Autowired
    private UserOpLogService userOpLogService;

    @Override
    public BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        if (accountModel == null) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setMessage("您尚未进行实名认证");
            payFormDataDto.setStatus(false);
            baseDto.setData(payFormDataDto);
            return baseDto;
        }

        BaseDto<PayFormDataDto> baseDto = payWrapperClient.bindBankCard(dto);

        // 发送用户行为日志 MQ消息
        userOpLogService.sendUserOpLogMQ(dto.getLoginName(), dto.getIp(), dto.getSource().name(), dto.getDeviceId(),
                UserOpType.BIND_CARD, null);

        return baseDto;
    }

    @Override
    public BaseDto<PayFormDataDto> replaceBankCard(BindBankCardDto dto) {
        BaseDto<PayFormDataDto> retDto = payWrapperClient.replaceBankCard(dto);

        // 发送用户行为日志 MQ消息
        userOpLogService.sendUserOpLogMQ(dto.getLoginName(), dto.getIp(), dto.getSource().name(), dto.getDeviceId(),
                UserOpType.REPLACE_CARD, null);

        return retDto;
    }

    @Override
    public BankCardModel getPassedBankCard(String loginName) {
        return bankCardMapper.findPassedBankCardByLoginName(loginName);
    }

    @Override
    public boolean isReplacing(String loginName) {
        return CollectionUtils.isNotEmpty(bankCardMapper.findApplyBankCardByLoginName(loginName));
    }

    @Override
    public boolean isManual(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        return accountModel.getFreeze() > 0 || accountModel.getBalance() > 0 || investRepayService.findSumRepayingCorpusByLoginName(loginName) > 0 || investRepayService.findSumRepayingInterestByLoginName(loginName) > 0;

    }

    @Override
    public BankCardModel getBankCardById(long id) {
        return bankCardMapper.findById(id);
    }
}
