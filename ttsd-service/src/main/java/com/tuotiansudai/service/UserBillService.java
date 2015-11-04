package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.UserBillBusinessType;

import java.util.Date;
import java.util.List;

public interface UserBillService {

    BaseDto<BasePaginationDataDto> getUserBillData(int index,
                                                   int pageSize,
                                                   Date startTime,
                                                   Date endTime,
                                                   List<UserBillBusinessType> userBillBusinessType);

    long findSumRewardByLoginName(String loginName);
}
