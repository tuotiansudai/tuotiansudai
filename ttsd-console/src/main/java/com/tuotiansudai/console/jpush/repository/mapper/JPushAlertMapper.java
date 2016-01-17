package com.tuotiansudai.console.jpush.repository.mapper;

import com.tuotiansudai.console.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.console.jpush.repository.model.PushType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPushAlertMapper {

    void create(JPushAlertModel jPushAlertModel);

//    void update(JPushAlertModel jPushAlertModel);

    int findPushTypeCount(@Param("pushType") PushType pushType);

    int findPushAlertCount(@Param(value = "name") String name);

    List<JPushAlertModel> findPushAlerts(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize,
                           @Param(value = "name") String name);

    JPushAlertModel findJPushAlertModelById(long id);

}
