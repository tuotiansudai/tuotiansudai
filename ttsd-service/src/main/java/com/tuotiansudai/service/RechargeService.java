package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.RechargeSource;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.repository.model.RepayStatus;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RechargeService {

    BaseDto<PayFormDataDto> recharge(RechargeDto rechargeDto);

    long findSumRechargeByLoginName(String loginName);

    BaseDto<BasePaginationDataDto> findRechargePagination(String rechargeId,
                                                          String loginName,
                                                          RechargeSource source,
                                                          RechargeStatus status,
                                                          int index,
                                                          int pageSize,
                                                          Date startTime,
                                                          Date endTime);

    int findRechargeCount(@Param(value = "rechargeId") String rechargeId,
                          @Param(value = "loginName") String loginName,
                          @Param(value = "source") RechargeSource source,
                          @Param(value = "status") RechargeStatus status,
                          @Param(value = "startTime") Date startTime,
                          @Param(value = "endTime") Date endTime);
}
