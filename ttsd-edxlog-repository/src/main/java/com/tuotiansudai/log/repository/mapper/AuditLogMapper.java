package com.tuotiansudai.log.repository.mapper;

import com.tuotiansudai.log.repository.model.AuditLogModel;
import com.tuotiansudai.log.repository.model.OperationType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AuditLogMapper {

    void create(AuditLogModel auditLogModel);

    AuditLogModel findById(@Param("id") long id);

    long count(@Param("operationType") OperationType operationType,
               @Param("targetId") String targetId,
               @Param("operatorMobile") String operatorMobile,
               @Param("auditorMobile") String auditorMobile,
               @Param("startTime") Date startTime,
               @Param("endTime") Date endTime);

    List<AuditLogModel> getPaginationData(@Param("operationType") OperationType operationType,
                                          @Param("targetId") String targetId,
                                          @Param("operatorMobile") String operatorMobile,
                                          @Param("auditorMobile") String auditorMobile,
                                          @Param("startTime") Date startTime,
                                          @Param("endTime") Date endTime,
                                          @Param("index") int index,
                                          @Param("pageSize") int pageSize);

}
