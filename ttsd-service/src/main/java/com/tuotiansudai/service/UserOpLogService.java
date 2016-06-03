package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.UserOpLogModel;
import com.tuotiansudai.repository.model.UserOpType;

import java.util.Date;

public interface UserOpLogService {
    void create(UserOpLogModel model);

    BasePaginationDataDto<UserOpLogModel> getUserOpLogPaginationData(String loginName,
                                                                     UserOpType opType,
                                                                     Date startTime,
                                                                     Date endTime,
                                                                     int index,
                                                                     int pageSize);
}

