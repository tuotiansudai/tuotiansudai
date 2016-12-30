package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.log.repository.mapper.AuditLogMapper;
import com.tuotiansudai.log.repository.model.AuditLogModel;
import com.tuotiansudai.log.repository.model.OperationType;
import com.tuotiansudai.util.PaginationUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuditLogQueryService {

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    public BasePaginationDataDto<AuditLogModel> getAuditLogPaginationData(OperationType operationType, String targetId, String operatorMobile, String auditorMobile, Date startTime, Date endTime, int index, int pageSize) {
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

        long count = auditLogMapper.count(operationType, targetId, operatorMobile, auditorMobile, startTime, endTime);

        List<AuditLogModel> data = Lists.newArrayList();
        if (count > 0) {
            int totalPages = PaginationUtil.calculateMaxPage(count, pageSize);
            index = index > totalPages ? totalPages : index;
            data = auditLogMapper.getPaginationData(operationType, targetId, operatorMobile, auditorMobile, startTime, endTime, (index - 1) * pageSize, pageSize);
        }

        return new BasePaginationDataDto<>(index, pageSize, count, data);
    }

    public String clearMybatisCache() {
        return redisWrapperClient.clearMybatisCache();
    }

}
