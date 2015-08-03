package com.tuotiansudai.smswrapper.repository.mapper;

import com.tuotiansudai.smswrapper.repository.model.SmsModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseMapper {

    void create(SmsModel model);

    List<SmsModel> findByMobile(@Param("mobile") String mobile);
}
