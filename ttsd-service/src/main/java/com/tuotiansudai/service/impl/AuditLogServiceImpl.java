package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.AuditLogPaginationItemDataDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.AuditLogMapper;
import com.tuotiansudai.repository.model.AuditLogModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.AuditLogService;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private static String AUDIT_LOG_TEMPLATE = "'{'loginName:{0}, mobile:{1}, email:{2}, referrer:{3}, status:{4}, roles:[{5}]'}'";

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    @Transactional
    public void generateAuditLog(String operatorLoginName, UserModel beforeUpdateUserModel, List<UserRoleModel> beforeUpdateUserRoleModels,
                                 UserModel afterUpdateUserModel, List<UserRoleModel> afterUpdateUserRoleModels,
                                 String userIp) {
        String beforeUpdate = MessageFormat.format(AUDIT_LOG_TEMPLATE,
                beforeUpdateUserModel.getLoginName(),
                beforeUpdateUserModel.getMobile(),
                beforeUpdateUserModel.getEmail(),
                beforeUpdateUserModel.getReferrer(),
                beforeUpdateUserModel.getStatus().name(),
                Joiner.on(",").join(Lists.transform(beforeUpdateUserRoleModels, new Function<UserRoleModel, String>() {
                    @Override
                    public String apply(UserRoleModel input) {
                        return input.getRole().name();
                    }
                })));

        String afterUpdate = MessageFormat.format(AUDIT_LOG_TEMPLATE,
                afterUpdateUserModel.getLoginName(),
                afterUpdateUserModel.getMobile(),
                afterUpdateUserModel.getEmail(),
                afterUpdateUserModel.getReferrer(),
                afterUpdateUserModel.getStatus().name(),
                Joiner.on(",").join(Lists.transform(afterUpdateUserRoleModels, new Function<UserRoleModel, String>() {
                    @Override
                    public String apply(UserRoleModel input) {
                        return input.getRole().name();
                    }
                })));

        AuditLogModel log = new AuditLogModel();
        log.setId(idGenerator.generate());
        log.setLoginName(beforeUpdateUserModel.getLoginName());
        log.setOperatorLoginName(operatorLoginName);
        log.setIp(userIp);
        log.setDescription(beforeUpdate + " => " + afterUpdate);
        auditLogMapper.create(log);
    }

    @Override
    public BasePaginationDataDto<AuditLogPaginationItemDataDto> getAuditLogPaginationData(String loginName, String operatorLoginName, Date startTime, Date endTime, int index, int pageSize) {
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

        long count = auditLogMapper.count(loginName, operatorLoginName, startTime, endTime);

        List<AuditLogModel> data = Lists.newArrayList();
        if (count > 0) {
            int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            data = auditLogMapper.getPaginationData(loginName, operatorLoginName, startTime, endTime, (index - 1) * pageSize, pageSize);
        }

        List<AuditLogPaginationItemDataDto> records = Lists.transform(data, new Function<AuditLogModel, AuditLogPaginationItemDataDto>() {
            @Override
            public AuditLogPaginationItemDataDto apply(AuditLogModel input) {
                return new AuditLogPaginationItemDataDto(input.getLoginName(), input.getOperatorLoginName(), input.getIp(), input.getDescription(), input.getOperationTime());
            }
        });

        return new BasePaginationDataDto<>(index, pageSize, count, records);
    }

}
