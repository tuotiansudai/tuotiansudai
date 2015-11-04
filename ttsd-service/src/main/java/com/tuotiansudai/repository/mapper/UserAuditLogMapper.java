package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserAuditLogModel;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuditLogMapper {

    void create(UserAuditLogModel userAuditLogModel);

}
