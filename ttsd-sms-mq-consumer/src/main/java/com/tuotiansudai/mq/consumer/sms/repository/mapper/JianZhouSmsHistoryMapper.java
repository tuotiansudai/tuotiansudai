package com.tuotiansudai.mq.consumer.sms.repository.mapper;

import com.tuotiansudai.mq.consumer.sms.repository.model.JianZhouSmsHistoryModel;
import org.springframework.stereotype.Repository;

@Repository
public interface JianZhouSmsHistoryMapper {

    void create(JianZhouSmsHistoryModel jianZhouSmsHistoryModel);

    void update(JianZhouSmsHistoryModel jianZhouSmsHistoryModel);
}
