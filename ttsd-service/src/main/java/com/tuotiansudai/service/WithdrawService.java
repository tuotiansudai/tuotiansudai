package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.repository.model.WithdrawStatus;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface WithdrawService {

    BaseDto<PayFormDataDto> withdraw(WithdrawDto withdrawDto);

    long findSumWithdrawByLoginName(String loginName);

    BaseDto<BasePaginationDataDto> findWithdrawPagination(String withdrawId,
                                                          String loginName,
                                                          WithdrawStatus status,
                                                          int index,
                                                          int pageSize,
                                                          Date startTime,
                                                          Date endTime);

    int findWithdrawCount(@Param(value = "rechargeId") String withdrawId,
                          @Param(value = "loginName") String loginName,
                          @Param(value = "status") WithdrawStatus status,
                          @Param(value = "startTime") Date startTime,
                          @Param(value = "endTime") Date endTime);
}
