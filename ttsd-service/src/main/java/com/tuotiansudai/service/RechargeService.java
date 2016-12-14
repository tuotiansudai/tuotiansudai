package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.RechargeSource;
import com.tuotiansudai.repository.model.RechargeStatus;

import java.util.Date;
import java.util.List;

public interface RechargeService {

    BaseDto<PayFormDataDto> recharge(RechargeDto rechargeDto);

    long sumSuccessRechargeAmount(String loginName);

    RechargeModel findRechargeById(long id);
}
