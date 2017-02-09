package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AnxinSignPropertyModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinSignPropertyMapper {

    void create(AnxinSignPropertyModel anxinSignPropertyModel);

    void update(AnxinSignPropertyModel anxinSignPropertyModel);

    AnxinSignPropertyModel findById(@Param(value = "id") Long id);

    AnxinSignPropertyModel findByLoginName(@Param(value = "loginName") String loginName);
}
