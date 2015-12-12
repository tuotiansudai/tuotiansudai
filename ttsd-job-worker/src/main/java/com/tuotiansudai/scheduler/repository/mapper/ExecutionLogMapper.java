package com.tuotiansudai.scheduler.repository.mapper;

import com.tuotiansudai.scheduler.repository.model.ExecuteStatus;
import com.tuotiansudai.scheduler.repository.model.ExecutionLogModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionLogMapper {
    void create(ExecutionLogModel model);

    void update(@Param("id") long id,
                @Param("executeStatus") ExecuteStatus executeStatus,
                @Param("exception") String exception);
}
