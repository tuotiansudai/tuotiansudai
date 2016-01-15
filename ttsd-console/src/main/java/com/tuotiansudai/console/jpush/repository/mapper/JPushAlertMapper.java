package com.tuotiansudai.console.jpush.repository.mapper;

import com.tuotiansudai.console.jpush.repository.model.JPushAlertModel;
import org.springframework.stereotype.Repository;

@Repository
public interface JPushAlertMapper {

    void create(JPushAlertModel jPushAlertModel);

}
