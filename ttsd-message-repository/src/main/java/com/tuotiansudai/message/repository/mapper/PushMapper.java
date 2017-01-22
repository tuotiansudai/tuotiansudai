package com.tuotiansudai.message.repository.mapper;

import com.tuotiansudai.message.repository.model.PushModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PushMapper {

    void create(PushModel pushModel);

    void update(PushModel pushModel);

    PushModel findById(@Param("id") Long id);

    void deleteById(@Param("id") Long id);
}
