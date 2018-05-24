package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.RechargeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRechargeService {

    private final BankWrapperClient bankWrapperClient;

    private final BankAccountMapper bankAccountMapper;

    private final UserOpLogService userOpLogService;

    @Autowired
    public UserRechargeService(BankWrapperClient bankWrapperClient, BankAccountMapper bankAccountMapper, UserOpLogService userOpLogService){
        this.bankAccountMapper = bankAccountMapper;
        this.bankWrapperClient = bankWrapperClient;
        this.userOpLogService = userOpLogService;
    }

    public BaseDto<PayFormDataDto> recharge() {
//        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(registerAccountDto.getLoginName());
//        if (bankAccountModel == null) {
//            PayFormDataDto payFormDataDto = new PayFormDataDto();
//            BaseDto<PayFormDataDto> baseDto = new BaseDto<>(payFormDataDto);
//            payFormDataDto.setMessage("未实名认证");
//            payFormDataDto.setStatus(false);
//            return baseDto;
//        }
        return bankWrapperClient.recharge("1", "s30Ntsvw", "18895730992", "UU02615960791461001", "UA02615960791501001", "100.00", "FAST_PAY");
    }

}
