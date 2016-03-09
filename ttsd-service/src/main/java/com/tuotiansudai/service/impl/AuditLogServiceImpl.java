package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.AuditLogPaginationItemDataDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.AuditLogMapper;
import com.tuotiansudai.repository.model.AuditLogModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.AuditLogService;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AccountService accountService;

    @Override
    @Transactional
    public void createUserActiveLog(String loginName, String operatorLoginName, UserStatus userStatus, String userIp) {

        String operation = userStatus == UserStatus.ACTIVE ? " 解禁" : " 禁止";
        String description = operatorLoginName + operation + "了用户［" + accountService.getRealName(loginName) + "］。";

        AuditLogModel log = new AuditLogModel();
        log.setId(idGenerator.generate());
        log.setTargetId(loginName);
        log.setOperatorLoginName(operatorLoginName);
        log.setOperationType(OperationType.USER);
        log.setIp(userIp);
        log.setDescription(description);
        auditLogMapper.create(log);
    }

    @Override
    public void createAuditLog(String auditorLoginName, String operatorLoginName, OperationType operationType, String objectId, String description, String auditorIp) {
        AuditLogModel log = new AuditLogModel();
        log.setId(idGenerator.generate());
        log.setOperatorLoginName(operatorLoginName);
        log.setAuditorLoginName(auditorLoginName);
        log.setTargetId(objectId);
        log.setOperationType(operationType);
        log.setIp(auditorIp);
        log.setDescription(description);
        auditLogMapper.create(log);
    }

    @Override
    public BasePaginationDataDto<AuditLogPaginationItemDataDto> getAuditLogPaginationData(OperationType operationType, String targetId, String operatorLoginName, String auditorLoginName, Date startTime, Date endTime, int index, int pageSize) {
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

        long count = auditLogMapper.count(operationType, targetId, operatorLoginName, auditorLoginName, startTime, endTime);

        List<AuditLogModel> data = Lists.newArrayList();
        if (count > 0) {
            int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            data = auditLogMapper.getPaginationData(operationType, targetId, operatorLoginName, auditorLoginName, startTime, endTime, (index - 1) * pageSize, pageSize);
        }

        List<AuditLogPaginationItemDataDto> records = Lists.transform(data, new Function<AuditLogModel, AuditLogPaginationItemDataDto>() {
            @Override
            public AuditLogPaginationItemDataDto apply(AuditLogModel input) {
                return new AuditLogPaginationItemDataDto(input.getAuditorLoginName(), input.getOperatorLoginName(), input.getTargetId(), input.getOperationType(), input.getIp(), input.getDescription(), input.getOperationTime());
            }
        });

        return new BasePaginationDataDto<>(index, pageSize, count, records);
    }

}
