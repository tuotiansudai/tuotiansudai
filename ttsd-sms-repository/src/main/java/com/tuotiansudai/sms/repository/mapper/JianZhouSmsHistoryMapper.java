package com.tuotiansudai.sms.repository.mapper;

import com.tuotiansudai.sms.repository.model.JianZhouSmsHistoryModel;
import org.springframework.stereotype.Repository;

@Repository
public interface JianZhouSmsHistoryMapper {

    void create(JianZhouSmsHistoryModel jianZhouSmsHistoryModel);

    void update(JianZhouSmsHistoryModel jianZhouSmsHistoryModel);
}
