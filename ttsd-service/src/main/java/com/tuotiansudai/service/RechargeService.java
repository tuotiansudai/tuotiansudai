package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.repository.model.RechargeSource;
import com.tuotiansudai.repository.model.RechargeStatus;

import java.util.Date;

public interface RechargeService {

    BaseDto<PayFormDataDto> recharge(RechargeDto rechargeDto);


    BaseDto<BasePaginationDataDto> findRechargePagination(String rechargeId,
                                                          String loginName,
                                                          RechargeSource source,
                                                          RechargeStatus status,
                                                          int index,
                                                          int pageSize,
                                                          Date startTime,
                                                          Date endTime);

    long sumSuccessRechargeAmount(String loginName);
}
