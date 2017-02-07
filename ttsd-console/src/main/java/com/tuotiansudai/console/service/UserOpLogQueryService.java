package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.log.repository.mapper.UserOpLogMapper;
import com.tuotiansudai.log.repository.model.UserOpLogModel;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.util.CalculateUtil;
import com.tuotiansudai.util.PaginationUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserOpLogQueryService {

    @Autowired
    private UserOpLogMapper userOpLogMapper;

    public BasePaginationDataDto<UserOpLogModel> getUserOpLogPaginationData(String mobile,
                                                                            UserOpType opType,
                                                                            Date startTime,
                                                                            Date endTime,
                                                                            int index,
                                                                            int pageSize) {
        if (startTime == null) {
            startTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }

        if (endTime == null) {
            endTime = CalculateUtil.calculateMaxDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        long count = userOpLogMapper.count(mobile, opType, startTime, endTime);

        List<UserOpLogModel> data = Lists.newArrayList();
        if (count > 0) {
            int totalPages = PaginationUtil.calculateMaxPage(count, pageSize);
            index = index > totalPages ? totalPages : index;
            data = userOpLogMapper.getPaginationData(mobile, opType, startTime, endTime, (index - 1) * pageSize, pageSize);
        }

        return new BasePaginationDataDto<>(index, pageSize, count, data);
    }

}
