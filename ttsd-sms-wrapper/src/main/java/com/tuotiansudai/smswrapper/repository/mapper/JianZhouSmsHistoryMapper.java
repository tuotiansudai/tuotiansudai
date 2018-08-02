package com.tuotiansudai.smswrapper.repository.mapper;

import com.tuotiansudai.smswrapper.repository.model.JianZhouSmsHistoryModel;
import org.springframework.stereotype.Repository;

@Repository
public interface JianZhouSmsHistoryMapper {

    void create(JianZhouSmsHistoryModel jianZhouSmsHistoryModel);

    void update(JianZhouSmsHistoryModel jianZhouSmsHistoryModel);
}
