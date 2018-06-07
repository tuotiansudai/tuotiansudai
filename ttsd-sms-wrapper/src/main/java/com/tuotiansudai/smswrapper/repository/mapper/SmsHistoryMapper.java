package com.tuotiansudai.smswrapper.repository.mapper;

import com.tuotiansudai.smswrapper.repository.model.SmsHistoryModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsHistoryMapper {

    void create(SmsHistoryModel model);

    void update(SmsHistoryModel model);
}
