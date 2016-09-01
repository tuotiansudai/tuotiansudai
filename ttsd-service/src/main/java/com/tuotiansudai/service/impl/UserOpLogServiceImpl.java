package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.UserOpLogMapper;
import com.tuotiansudai.repository.model.UserOpLogModel;
import com.tuotiansudai.repository.model.UserOpLogView;
import com.tuotiansudai.repository.model.UserOpType;
import com.tuotiansudai.service.UserOpLogService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserOpLogServiceImpl implements UserOpLogService {

    @Autowired
    private UserOpLogMapper userOpLogMapper;

    @Override
    public void create(UserOpLogModel model) {
        userOpLogMapper.create(model);
    }


    @Override
    public BasePaginationDataDto<UserOpLogView> getUserOpLogPaginationData(String mobile,
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
            endTime = new DateTime().withDate(9999, 12, 31).withTimeAtStartOfDay().toDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        long count = userOpLogMapper.count(mobile, opType, startTime, endTime);

        List<UserOpLogView> data = Lists.newArrayList();
        if (count > 0) {
            int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            data = userOpLogMapper.getPaginationData(mobile, opType, startTime, endTime, (index - 1) * pageSize, pageSize);
        }

        return new BasePaginationDataDto<>(index, pageSize, count, data);
    }

}
