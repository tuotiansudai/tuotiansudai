package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.DemoModel;
import org.apache.ibatis.annotations.Select;

public interface DemoMapper {

    @Select("select * from user where id=#{id}")
    DemoModel getDemoById(String id);
}
