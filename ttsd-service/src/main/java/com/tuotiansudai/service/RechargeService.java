package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.RechargeSource;
import com.tuotiansudai.repository.model.RechargeStatus;

import java.util.Date;
import java.util.List;

public interface RechargeService {

    BaseDto<PayFormDataDto> recharge(RechargeDto rechargeDto);


    BaseDto<BasePaginationDataDto<RechargePaginationItemDataDto>> findRechargePagination(String rechargeId,
                                                                                         String mobile,
                                                                                         RechargeSource source,
                                                                                         RechargeStatus status,
                                                                                         String channel,
                                                                                         int index,
                                                                                         int pageSize,
                                                                                         Date startTime,
                                                                                         Date endTime);


    long findSumRechargeAmount(String rechargeId,
                               String mobile,
                               RechargeSource source,
                               RechargeStatus status,
                               String channel,
                               Date startTime,
                               Date endTime);

    long sumSuccessRechargeAmount(String loginName);

    List<String> findAllChannel();
}
