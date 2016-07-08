package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AuditLogModel;
import com.tuotiansudai.repository.model.AuditLogView;
import com.tuotiansudai.task.OperationType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AuditLogMapper {

    void create(AuditLogModel auditLogModel);

    long count(@Param("operationType") OperationType operationType,
               @Param("targetId") String targetId,
               @Param("operatorMobile") String operatorMobile,
               @Param("auditorMobile") String auditorMobile,
               @Param("startTime") Date startTime,
               @Param("endTime") Date endTime);

    List<AuditLogView> getPaginationData(@Param("operationType") OperationType operationType,
                                          @Param("targetId") String targetId,
                                         @Param("operatorMobile") String operatorMobile,
                                         @Param("auditorMobile") String auditorMobile,
                                          @Param("startTime") Date startTime,
                                          @Param("endTime") Date endTime,
                                          @Param("index") int index,
                                          @Param("pageSize") int pageSize);

}
