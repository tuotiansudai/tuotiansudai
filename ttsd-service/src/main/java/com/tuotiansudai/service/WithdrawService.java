package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WithdrawStatus;

import java.util.Date;

public interface WithdrawService {

    BaseDto<PayFormDataDto> withdraw(WithdrawDto withdrawDto);

    BaseDto<BasePaginationDataDto> findWithdrawPagination(String withdrawId,
                                                          String mobile,
                                                          WithdrawStatus status,
                                                          Source source,
                                                          int index,
                                                          int pageSize,
                                                          Date startTime,
                                                          Date endTime);

    long findSumWithdrawAmount(String withdrawId,
                               String mobile,
                               WithdrawStatus status,
                               Source source,
                               Date startTime,
                               Date endTime);

    long findSumWithdrawFee(String withdrawId,
                            String mobile,
                            WithdrawStatus status,
                            Source source,
                            Date startTime,
                            Date endTime);

    long sumSuccessWithdrawAmount(String loginName);


    int findWithdrawCount(String withdrawId,
                          String mobile,
                          WithdrawStatus status,
                          Source source,
                          Date startTime,
                          Date endTime);
}
