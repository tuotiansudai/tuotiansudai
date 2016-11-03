package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.AnxinSignPropertyModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinSignPropertyMapper {

    void create(AnxinSignPropertyModel anxinSignPropertyModel);

    void update(AnxinSignPropertyModel anxinSignPropertyModel);

    AnxinSignPropertyModel findById(Long id);

    AnxinSignPropertyModel findById(@Param(value = "loginName") String loginName);
}
