package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.UserFundMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.UserFundView;
import com.tuotiansudai.service.BindBankCardService;
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
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private UserOpLogService userOpLogService;

    @Autowired
    private UserFundMapper userFundMapper;


    @Override
    public BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto) {
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>(payFormDataDto);
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(dto.getLoginName());
        if (bankAccountModel == null) {
            payFormDataDto.setMessage("您尚未进行实名认证");
            payFormDataDto.setStatus(false);
            return baseDto;
        }

        BankCardModel passedBankCard = bankCardMapper.findPassedBankCardByLoginName(dto.getLoginName());

        if (passedBankCard != null) {
            payFormDataDto.setMessage("已绑定银行卡，请勿重复绑定");
            payFormDataDto.setStatus(false);
            return baseDto;
        }

        baseDto = payWrapperClient.bindBankCard(dto);

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
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(loginName);
        UserFundView userFundView = userFundMapper.findByLoginName(loginName);
        return bankAccountModel.getBalance() > 0 || userFundView.getExpectedTotalCorpus() > 0 || userFundView.getExpectedTotalInterest() > 0;

    }

    @Override
    public BankCardModel getBankCardById(long id) {
        return bankCardMapper.findById(id);
    }
}
