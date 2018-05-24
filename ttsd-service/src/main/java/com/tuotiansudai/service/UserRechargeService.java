package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.UserRechargeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRechargeService {

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    public BaseDto<PayFormDataDto> recharge(UserRechargeDto userRechargeDto){



    }


}
