package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AuditLogModel;
import com.tuotiansudai.repository.model.LoginLogModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AuditLogMapper {

    void create(AuditLogModel auditLogModel);

    long count(@Param("loginName") String loginName,
               @Param("operatorLoginName") String operatorLoginName,
               @Param("startTime") Date startTime,
               @Param("endTime") Date endTime);

    List<AuditLogModel> getPaginationData(@Param("loginName") String loginName,
                                          @Param("operatorLoginName") String operatorLoginName,
                                          @Param("startTime") Date startTime,
                                          @Param("endTime") Date endTime,
                                          @Param("index") int index,
                                          @Param("pageSize") int pageSize);
}
