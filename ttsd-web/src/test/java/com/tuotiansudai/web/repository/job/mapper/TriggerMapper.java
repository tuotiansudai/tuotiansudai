package com.tuotiansudai.web.repository.job.mapper;

import com.tuotiansudai.web.repository.job.model.TriggerModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TriggerMapper {
    TriggerModel findByKey(@Param("group") String group, @Param("name") String name);
}
