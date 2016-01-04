package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.RechargeSource;
import com.tuotiansudai.repository.model.RechargeStatus;

import java.util.Date;
import java.util.List;

public interface SystemRechargeService {

    BaseDto<PayFormDataDto> systemRecharge(SystemRechargeDto systemRechargeDto);
}
