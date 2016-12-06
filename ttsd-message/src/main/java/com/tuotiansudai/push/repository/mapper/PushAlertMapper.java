package com.tuotiansudai.push.repository.mapper;

import com.tuotiansudai.push.repository.model.PushAlertModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PushAlertMapper {

    void create(PushAlertModel pushAlertModel);

    void update(PushAlertModel pushAlertModel);

    PushAlertModel findById(@Param("id") Long id);

    void deleteById(@Param("id") Long id);
}
