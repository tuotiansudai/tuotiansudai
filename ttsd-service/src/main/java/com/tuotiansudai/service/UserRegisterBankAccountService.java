package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRegisterBankAccountService {

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    private final BankAccountMapper bankAccountMapper;

    private final UserOpLogService userOpLogService;

    @Autowired
    public UserRegisterBankAccountService(BankAccountMapper bankAccountMapper, UserOpLogService userOpLogService){
        this.bankAccountMapper = bankAccountMapper;
        this.userOpLogService = userOpLogService;
    }

    public BaseDto<PayFormDataDto> registerAccount(RegisterAccountDto registerAccountDto, Source source, String ip, String deviceId) {
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(registerAccountDto.getLoginName());
        if (bankAccountModel != null) {
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>(payFormDataDto);
            payFormDataDto.setMessage("已实名认证，请勿重复操作");
            payFormDataDto.setStatus(false);
            return baseDto;
        }
        userOpLogService.sendUserOpLogMQ(registerAccountDto.getLoginName(), ip, source.name(), deviceId, UserOpType.REGISTER, null);
        return bankWrapperClient.register(Source.WEB, registerAccountDto.getLoginName(), registerAccountDto.getUserName(), registerAccountDto.getIdentityNumber(), registerAccountDto.getMobile());
    }
}
