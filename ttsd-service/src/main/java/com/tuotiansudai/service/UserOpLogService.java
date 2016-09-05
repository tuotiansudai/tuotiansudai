package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.UserOpLogModel;
import com.tuotiansudai.repository.model.UserOpLogView;
import com.tuotiansudai.repository.model.UserOpType;

import java.util.Date;

public interface UserOpLogService {
    void create(UserOpLogModel model);

    BasePaginationDataDto<UserOpLogView> getUserOpLogPaginationData(String mobile,
                                                                    UserOpType opType,
                                                                    Date startTime,
                                                                    Date endTime,
                                                                    int index,
                                                                    int pageSize);
}

